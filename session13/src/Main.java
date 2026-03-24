import utils.DatabaseConnection;

import java.sql.*;

public class Main{
    static void main(String[] args) {
        Connection con= DatabaseConnection.getConnection();

        try{
            con.setAutoCommit(false);
            String sql="update accounts set balance=balance-? where id=?";
            String sql2="update accounts set balance=balance+? where id=?";
            String sql3="insert into transfers (sender_id,receive_id,amount,transfer_date) values (?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setDouble(1,10000);
            ps.setInt(2,1);

            PreparedStatement ps2=con.prepareStatement(sql2);
            ps2.setDouble(1,10000);
            ps2.setInt(2,2);

            PreparedStatement ps3=con.prepareStatement(sql3);
            ps3.setInt(1,1);
            ps3.setInt(2,1);
            ps3.setDouble(3,1000);
            ps3.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
            con.commit();


        } catch (SQLException e) {
            if(con!=null){
                try {
                    con.close();
                } catch (SQLException ex) {}
                throw new RuntimeException();
            }
            System.err.println(e.getMessage());
        }finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}