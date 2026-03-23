package bt3;

import utils.DatabaseConnection;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
//        CallableStatement cs = conn.prepareCall("{call GET_SURGERY_FEE(?, ?)}");
//
//        cs.setInt(1, 505);
//        cs.execute();
//
//        double cost = cs.getDouble(2);
//
//        //  LỖI: chưa register OUT parameter → lỗi index out of range
//        System.out.println(cost);

        CallableStatement cs = conn.prepareCall("{call GET_SURGERY_FEE(?, ?)}");

        cs.setInt(1, 505);
        cs.registerOutParameter(2, Types.DOUBLE); //  FIX

        cs.execute();

        double cost = cs.getDouble(2);

        System.out.println("Cost: " + cost);
    }
}