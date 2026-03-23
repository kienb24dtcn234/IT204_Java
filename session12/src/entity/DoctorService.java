package entity;

import utils.DatabaseConnection;
import java.sql.*;

public class DoctorService {

    // ================= BT1: UPDATE STOCK =================
    public void updateMedicineStock(int id, int addedQuantity) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "UPDATE medicines SET stock = stock + ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, addedQuantity); // ✅ đúng
            ps.setInt(2, id);

            ps.executeUpdate();

            System.out.println("Update stock OK");

        } catch (Exception e) {
            e.printStackTrace();
        }

        //  LỖI hay gặp:
        // set sai thứ tự → update sai dữ liệu
    }

    // ================= BT2: SEARCH PRICE =================
    public void findMedicinesByPrice(double min, double max) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "SELECT * FROM medicines WHERE price BETWEEN ? AND ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDouble(1, min);
            ps.setDouble(2, max);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " - " +
                                rs.getString("name") + " - " +
                                rs.getDouble("price")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //  LỖI:
        // nối chuỗi SQL → dễ lỗi format + SQL Injection
    }

    // ================= BT3: CALL PROCEDURE =================
    public void calculatePrescriptionTotal(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            CallableStatement cs =
                    conn.prepareCall("{call CalculatePrescriptionTotal(?, ?)}");

            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.DOUBLE); // ✅ bắt buộc

            cs.execute();

            double total = cs.getDouble(2);
            System.out.println("Total: " + total);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //  LỖI:
        // quên registerOutParameter → crash
    }

    // ================= BONUS: DELETE DOCTOR =================
    public void deleteDoctor(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            CallableStatement cs =
                    conn.prepareCall("{call proc_delete_doctor(?)}");

            cs.setInt(1, id);
            cs.execute();

            System.out.println("Delete OK");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= BONUS: AVG EXPERIENCE =================
    public void avgExperience() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            CallableStatement cs =
                    conn.prepareCall("{call avg_exp(?)}");

            cs.registerOutParameter(1, Types.INTEGER); // ✅ OUT param

            cs.execute();

            int avg = cs.getInt(1);
            System.out.println("Avg exp: " + avg);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //  LỖI:
        // quên register OUT → lỗi index
    }
}