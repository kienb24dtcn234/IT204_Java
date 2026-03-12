package HN_KS24_CNTT5_NguyenTheKien;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductManager manager = new ProductManager(sc);

        int choice;

        do {
            System.out.println("========= PRODUCT MANAGEMENT SYSTEM =========");
            System.out.println("1. Thêm sản phẩm mới");
            System.out.println("2. Hiển thị danh sách sản phẩm");
            System.out.println("3. Cập nhật số lượng theo ID");
            System.out.println("4. Xóa sản phẩm đã hết hàng");
            System.out.println("5. Thoát chương trình");
            System.out.println("=============================================");
            System.out.print("Lựa chọn của bạn: ");

            while (!sc.hasNextInt()) {
                System.out.print("Lựa chọn của bạn: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    manager.addProduct();
                    break;
                case 2:
                    manager.displayProducts();
                    break;
                case 3:
                    manager.updateQuantityById();
                    break;
                case 4:
                    manager.deleteOutOfStock();
                    break;
                case 5:
                    System.out.println("Đang thoát chương trình");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }

            System.out.println();

        } while (choice != 5);

        sc.close();
    }
}
