// File: ThanhToanXuatVienService.java
package bt3;

import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    /**
     * Hàm xuất viện và thanh toán viện phí cho một bệnh nhân.
     * Nếu bất kỳ bước nào lỗi -> ROLLBACK toàn bộ.
     */
    public void xuatVienVaThanhToan(int maBenhNhan, double tienVienPhi) throws Exception {
        Connection conn = null;
        boolean oldAutoCommit = true;

        try {
            conn = DatabaseConnection.getConnection(); // hàm có sẵn trong project của em
            oldAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false); // gom tất cả vào 1 Transaction

            // ==========================
            // BƯỚC 1: BẪY 1 - KIỂM TRA SỐ DƯ
            // ==========================
            double soDuTamUng = laySoDuTamUng(conn, maBenhNhan);

            // Bẫy 1: logic nghiệp vụ - không cho phép số dư âm
            if (soDuTamUng < tienVienPhi) {
                // COMMENT BẪY 1:
                // Nếu số dư < tiền viện phí -> NÉM EXCEPTION và ROLLBACK transaction
                throw new Exception("So du tam ung khong du de thanh toan. So du = "
                        + soDuTamUng + ", tien vien phi = " + tienVienPhi);
            }

            // ==========================
            // BƯỚC 2: TRỪ TIỀN VÍ BỆNH NHÂN
            // ==========================
            String sqlTruTien = "UPDATE Patient_Wallet " +
                    "SET so_du_tam_ung = so_du_tam_ung - ? " +
                    "WHERE ma_benh_nhan = ?";
            int rowsTruTien;
            try (PreparedStatement ps = conn.prepareStatement(sqlTruTien)) {
                ps.setDouble(1, tienVienPhi);
                ps.setInt(2, maBenhNhan);
                rowsTruTien = ps.executeUpdate();
            }

            // Bẫy 2: DỮ LIỆU ẢO - không có dòng nào bị ảnh hưởng
            if (rowsTruTien == 0) {
                // COMMENT BẪY 2 (với bước trừ tiền):
                // Nếu Row Affected = 0 => maBenhNhan không tồn tại/không có ví
                // => NÉM EXCEPTION để ROLLBACK transaction
                throw new Exception("Khong tru duoc tien: khong co dong nao bi anh huong (maBenhNhan = "
                        + maBenhNhan + ")");
            }

            // ==========================
            // BƯỚC 3: GIẢI PHÓNG GIƯỜNG BỆNH
            // (ví dụ: lấy ma_giuong từ bảng Patient rồi set 'TRONG')
            // ==========================
            String sqlGiaiPhongGiuong = "UPDATE Bed b " +
                    "JOIN Patient p ON b.ma_giuong = p.ma_giuong " +
                    "SET b.trang_thai = 'TRONG' " +
                    "WHERE p.ma_benh_nhan = ?";
            int rowsGiuong;
            try (PreparedStatement ps = conn.prepareStatement(sqlGiaiPhongGiuong)) {
                ps.setInt(1, maBenhNhan);
                rowsGiuong = ps.executeUpdate();
            }

            // Bẫy 2: kiểm tra row affected
            if (rowsGiuong == 0) {
                // COMMENT BẪY 2 (với bước giải phóng giường):
                // Nếu 0 dòng bị update => bệnh nhân không có giường hoặc không tồn tại
                // => NÉM EXCEPTION để ROLLBACK transaction
                throw new Exception("Khong giai phong duoc giuong: khong co dong nao bi anh huong (maBenhNhan = "
                        + maBenhNhan + ")");
            }

            // ==========================
            // BƯỚC 4: CẬP NHẬT TRẠNG THÁI BỆNH NHÂN
            // ==========================
            String sqlCapNhatTrangThaiBN = "UPDATE Patient " +
                    "SET trang_thai = 'DA_XUAT_VIEN' " +
                    "WHERE ma_benh_nhan = ?";
            int rowsBN;
            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatTrangThaiBN)) {
                ps.setInt(1, maBenhNhan);
                rowsBN = ps.executeUpdate();
            }

            // Bẫy 2: kiểm tra row affected lần nữa
            if (rowsBN == 0) {
                // COMMENT BẪY 2 (với bước cập nhật trạng thái BN):
                // Nếu 0 dòng bị update => maBenhNhan không tồn tại
                // => NÉM EXCEPTION để ROLLBACK transaction
                throw new Exception("Khong cap nhat duoc trang thai benh nhan: "
                        + "khong co dong nao bi anh huong (maBenhNhan = " + maBenhNhan + ")");
            }

            // ==========================
            // BƯỚC 5: NẾU TẤT CẢ ĐỀU OK -> COMMIT
            // ==========================
            conn.commit();
            System.out.println("Xuat vien va thanh toan thanh cong cho benh nhan " + maBenhNhan);

        } catch (Exception e) {
            // Có lỗi ở bất kỳ bước nào -> rollback toàn bộ
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    System.err.println("Loi khi rollback transaction: " + rbEx.getMessage());
                }
            }
            // Ném lại cho tầng trên xử lý / log
            throw e;
        } finally {
            // Khôi phục lại auto-commit & đóng kết nối
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

    /**
     * Hàm phụ lấy số dư tạm ứng của bệnh nhân.
     * Có thể coi như 1 bước riêng để dễ comment Bẫy 1.
     */
    private double laySoDuTamUng(Connection conn, int maBenhNhan) throws Exception {
        String sql = "SELECT so_du_tam_ung FROM Patient_Wallet WHERE ma_benh_nhan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maBenhNhan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("so_du_tam_ung");
                } else {
                    // Không tìm thấy ví -> coi như dữ liệu không hợp lệ
                    throw new Exception("Khong tim thay vi benh nhan (maBenhNhan = " + maBenhNhan + ")");
                }
            }
        }
    }
}
