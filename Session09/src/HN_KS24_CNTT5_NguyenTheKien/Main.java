package HN_KS24_CNTT5_NguyenTheKien;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductDatabase db = ProductDatabase.getInstance();

        int choice;
        do {
            System.out.println("----------- QUẢN LÝ SẢN PHẨM -----------");
            System.out.println("1. Thêm mới sản phẩm");
            System.out.println("2. Xem danh sách sản phẩm");
            System.out.println("3. Cập nhật thông tin sản phẩm");
            System.out.println("4. Xóa sản phẩm");
            System.out.println("5. Thoát");
            System.out.println("----------------------------------------");
            System.out.print("Lựa chọn của bạn: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("THÊM MỚI SẢN PHẨM");
                    System.out.println("Chọn loại sản phẩm: ");
                    System.out.println("1. Sản phẩm vật lý (PhysicalProduct)");
                    System.out.println("2. Sản phẩm kỹ thuật số (DigitalProduct)");
                    System.out.print("Loại: ");
                    int type = Integer.parseInt(sc.nextLine());

                    System.out.print("Nhập mã sản phẩm (ID): ");
                    String id = sc.nextLine();
                    System.out.print("Nhập tên sản phẩm: ");
                    String name = sc.nextLine();
                    System.out.print("Nhập giá: ");
                    double price = Double.parseDouble(sc.nextLine());

                    double extra;
                    if (type == 1) {
                        System.out.print("Nhập trọng lượng (kg): ");
                        extra = Double.parseDouble(sc.nextLine());
                    } else if (type == 2) {
                        System.out.print("Nhập dung lượng (MB): ");
                        extra = Double.parseDouble(sc.nextLine());
                    } else {
                        System.out.println("Loại sản phẩm không hợp lệ, không thêm được.");
                        break;
                    }

                    Product p = ProductFactory.createProduct(type, id, name, price, extra);
                    if (p != null) {
                        db.addProduct(p);
                        System.out.println("Thêm sản phẩm thành công");
                    } else {
                        System.out.println("Không tạo được sản phẩm");
                    }
                    break;

                case 2:
                    List<Product> list = db.getAll();
                    if (list.isEmpty()) {
                        System.out.println("Chưa có sản phẩm nào trong kho");
                    } else {
                        for (Product pr : list) {
                            pr.displayInfo();
                        }
                    }
                    break;

                case 3:
                    System.out.print("Nhập ID sản phẩm cần cập nhật: ");
                    String uid = sc.nextLine();
                    Product up = db.findById(uid);
                    if (up == null) {
                        System.out.println("Không tìm thấy sản phẩm với ID đã nhập.");
                    } else {
                        System.out.print("Nhập tên mới: ");
                        String newName = sc.nextLine();
                        System.out.print("Nhập giá mới: ");
                        double newPrice = Double.parseDouble(sc.nextLine());
                        up.setName(newName);
                        up.setPrice(newPrice);

                        if (up instanceof PhysicalProduct) {
                            System.out.print("Nhập trọng lượng mới (kg): ");
                            double newWeight = Double.parseDouble(sc.nextLine());
                            ((PhysicalProduct) up).setWeight(newWeight);
                        } else if (up instanceof DigitalProduct) {
                            System.out.print("Nhập dung lượng mới (MB): ");
                            double newSize = Double.parseDouble(sc.nextLine());
                            ((DigitalProduct) up).setSize(newSize);
                        }

                        System.out.println("Cập nhật sản phẩm thành công");
                    }
                    break;

                case 4:
                    System.out.print("Nhập ID sản phẩm cần xóa: ");
                    String did = sc.nextLine();
                    boolean ok = db.deleteById(did);
                    if (ok) {
                        System.out.println("Xóa sản phẩm thành công");
                    } else {
                        System.out.println("Không tìm thấy sản phẩm để xóa");
                    }
                    break;

                case 5:
                    System.out.println(" Thoát chương trình ");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn từ lại");
            }

            System.out.println();
        } while (choice != 5);

        sc.close();
    }
}
