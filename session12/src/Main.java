import com.sun.security.jgss.GSSUtil;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try(Connection com=DatabaseConnection.getConnection()){

                String sql="insert into doctors(full_name,specialty,experience_years,base_salary,password) values(?,?,?,?,?)";
            PreparedStatement ps=com.prepareStatement(sql);
            System.out.println("Nhâp tên bác sĩ: ");
            ps.setString(1, sc.nextLine());
            System.out.println("Nhập chuyên ngành: ");
            ps.setString(2, sc.nextLine());
            System.out.println("Nhập năm kinh nghiệm: ");
            ps.setInt(3, Integer.parseInt(sc.nextLine()));
            System.out.println("Nhập lương cơ bản: ");
            ps.setDouble(4, Double.parseDouble(sc.nextLine()));
            System.out.println("Nhập mật khẩu: ");
            ps.setString(5, sc.next());
            int row=ps.executeUpdate();
            ps.clearParameters();
            System.out.println(row);


        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        System.out.println("Database connection established");


        try(Connection con=DatabaseConnection.getConnection()) {
            System.out.println("Nhập id muốn xóa: ");
            int idDelete=Integer.parseInt(sc.nextLine());
            CallableStatement callableStatement=con.prepareCall("{call proc_delete_doctor(?)}");
            callableStatement.setInt(1, idDelete);
            int row=callableStatement.executeUpdate();
            System.out.println(row);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        try(Connection con=DatabaseConnection.getConnection()) {
          CallableStatement callStmt = con.prepareCall("{call avg_exp(?)}");
          callStmt.registerOutParameter(1, Types.INTEGER);
            callStmt.executeQuery();
            Integer result=callStmt.getInt(1);
            System.out.println(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        }
}