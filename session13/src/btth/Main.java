// File: Main.java
package btth;

import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        int patientId = 101;
        double amount = 1_000_000;

        // TEST CASE 1: Happy Path (SQL đúng)
        System.out.println("===== TEST CASE 1: HAPPY PATH =====");
        try {
            dischargePatient(patientId, amount);
            System.out.println("Xuất viện thành công. Kiểm tra DB: "
                    + "có hóa đơn mới, PATIENTS.status='Đã xuất viện', BEDS='Trống'.");
        } catch (Exception e) {
            System.out.println("Test 1 thất bại: " + e.getMessage());
        }

        // TEST CASE 2: Rollback
        // Để test rollback: vào hàm freeBed() bên dưới, tạm sửa SQL
        // từ UPDATE BEDS... thành UPDATE BEDZZZ...
        System.out.println("\n===== TEST CASE 2: ROLLBACK =====");
        try {
            dischargePatient(patientId, amount);
            System.out.println("Nếu không có lỗi thì SQL rollback test chưa sửa (chưa đúng yêu cầu bài).");
        } catch (Exception e) {
            System.out.println("Đã bắt được lỗi như mong đợi: " + e.getMessage());
            System.out.println("Kiểm tra DB: không có hóa đơn mới, "
                    + "PATIENTS.status vẫn 'Đang điều trị', BEDS vẫn 'Đang sử dụng'.");
        }
    }

    /**
     * Xuất viện bệnh nhân:
     * 1. Lập hóa đơn
     * 2. Cập nhật trạng thái PATIENTS
     * 3. Giải phóng giường BEDS
     * Tất cả nằm trong MỘT TRANSACTION. Lỗi bất kỳ bước nào -> rollback toàn bộ.
     */
    public static void dischargePatient(int patientId, double amount) throws Exception {
        Connection conn = null;
        boolean oldAutoCommit = true;

        try {
            // Dùng hàm getConnection có sẵn trong project
            conn = DatabaseConnection.getConnection();
            oldAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false); // bắt đầu transaction

            // 1. Lập hóa đơn
            insertInvoice(conn, patientId, amount);

            // 2. Cập nhật hồ sơ bệnh nhân
            updatePatientStatus(conn, patientId);

            // 3. Giải phóng giường
            freeBed(conn, patientId);

            // Nếu cả 3 đều ok -> commit
            conn.commit();

        } catch (Exception e) {
            // Lỗi ở bất kỳ bước nào -> rollback ALL
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    System.err.println("Lỗi khi rollback: " + rbEx.getMessage());
                }
            }
            throw e; // ném ra cho main biết là fail
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(oldAutoCommit);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 1. Lập hóa đơn INVOICES
    private static void insertInvoice(Connection conn, int patientId, double amount) throws SQLException {
        String sql = "INSERT INTO INVOICES(patient_id, amount, created_at) " +
                "VALUES (?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setDouble(2, amount);
            ps.executeUpdate();
        }
    }

    // 2. Cập nhật trạng thái bệnh nhân trong PATIENTS
    private static void updatePatientStatus(Connection conn, int patientId) throws SQLException {
        String sql = "UPDATE PATIENTS " +
                "SET status = 'Đã xuất viện' " +
                "WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                // Chủ động coi đây là lỗi logic để rollback (bệnh nhân không tồn tại)
                throw new SQLException("Không tìm thấy bệnh nhân id = " + patientId + " để cập nhật trạng thái.");
            }
        }
    }

    // 3. Giải phóng giường trong BEDS
    private static void freeBed(Connection conn, int patientId) throws SQLException {
        String sql = "UPDATE BEDS " +
                "SET patient_id = NULL, status = 'Trống' " +
                "WHERE patient_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                // Không có giường nào đang gán cho bệnh nhân này -> lỗi, rollback
                throw new SQLException("Không có giường nào gắn với bệnh nhân " + patientId + " để giải phóng.");
            }
        }
    }
}
