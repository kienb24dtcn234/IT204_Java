package HN_KS24_CNTT5_NguyenTheKien;
import HN_KS24_CNTT5_NguyenTheKien.DatabaseConnection;

import java.sql.*;

public class Main {
    public static void main(String[] args){
        String fromAccount = "ACC01";
        String toAccount   = "ACC02";
        double amount      = 1000.0;

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            if (!checkBalanceEnough(conn, fromAccount, amount)) {
                System.out.println("Tài khoản gửi không tồn tại hoặc số dư không đủ");
                conn.rollback();
                return;
            }

            callUpdateBalance(conn, fromAccount, -amount);

            callUpdateBalance(conn, toAccount, amount);

            conn.commit();
            System.out.println("Chuyển khoản thành công");

            printAccount(conn, fromAccount);
            printAccount(conn, toAccount);

        } catch (Exception e) {
            System.out.println("Lỗi trong quá trình chuyển khoản:  " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Lỗi khi rollback : " + ex.getMessage());
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean checkBalanceEnough(Connection conn, String accountId, double amount) throws SQLException {
        String sql = "SELECT Balance FROM Accounts WHERE AccountId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, accountId);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            return false;
        }

        double balance = rs.getDouble("Balance");
        rs.close();
        ps.close();

        return balance >= amount;
    }

    private static void callUpdateBalance(Connection conn, String accountId, double amount) throws SQLException {
        String callSql = "{CALL sp_UpdateBalance(?, ?)}";
        CallableStatement cs = conn.prepareCall(callSql);
        cs.setString(1, accountId);
        cs.setDouble(2, amount);
        cs.executeUpdate();
        cs.close();
    }

    private static void printAccount(Connection conn, String accountId) throws SQLException {
        String sql = "SELECT AccountId, FullName, Balance FROM Accounts WHERE AccountId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, accountId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String id = rs.getString("AccountId");
            String name = rs.getString("FullName");
            double balance = rs.getDouble("Balance");
            System.out.println(id + " - " + name + " - Balance: " + balance);
        } else {
            System.out.println("Không tìm thấy tài khoản: " + accountId);
        }

        rs.close();
        ps.close();
    }
}
