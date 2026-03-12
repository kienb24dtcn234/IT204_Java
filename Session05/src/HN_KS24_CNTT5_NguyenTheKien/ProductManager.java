package HN_KS24_CNTT5_NguyenTheKien;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProductManager {

    private List<Product> products = new ArrayList<>();
    private Scanner scanner;

    public ProductManager(Scanner scanner) {
        this.scanner = scanner;
    }
    public void addProduct() {
        try {
            System.out.println("Thêm sản phẩm mới ");
            int id = readInt("Nhập ID: ");

            boolean isDuplicate = products.stream()
                    .anyMatch(p -> p.getId() == id);

            if (isDuplicate) {
                throw new InvalidProductException("ID đã tồn tại");
            }

            String name = readNonEmptyString("Nhập tên sản phẩm: ");
            double price = readDouble("Nhập giá: ");
            int quantity = readInt("Nhập số lượng: ");
            String category = readNonEmptyString("Nhập danh mục: ");

            Product product = new Product(id, name, price, quantity, category);
            products.add(product);

            System.out.println("Thêm sản phẩm thành công");
        } catch (InvalidProductException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
    public void displayProducts() {
        System.out.println("Danh sách sản phẩm");
        if (products.isEmpty()) {
            System.out.println("Danh sách trống");
            return;
        }
        products.stream()
                .forEach(p -> System.out.printf("%-5d %-20s %-10.2f %-10d %-15s%n",
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getQuantity(),
                        p.getCategory()));
    }
    public void updateQuantityById() {
        System.out.println("Cập nhật số lượng theo ID");
        int id = readInt("Nhập ID sản phẩm cần cập nhật: ");

        Optional<Product> optionalProduct = products.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        try {
            Product product = optionalProduct.orElseThrow(
                    () -> new InvalidProductException("Không tìm thấy sản phẩm với ID: " + id)
            );

            int newQuantity = readInt("Nhập số lượng mới: ");
            product.setQuantity(newQuantity);

            System.out.println("Cập nhật số lượng thành công");
        } catch (InvalidProductException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void deleteOutOfStock() {
        System.out.println("Xóa sản phẩm đã hết hàng");

        int before = products.size();
        products.removeIf(p -> p.getQuantity() == 0);
        int after = products.size();
        int deleted = before - after;

        if (deleted > 0) {
            System.out.println("Đã xóa " + deleted + " sản phẩm hết hàng");
        } else {
            System.out.println("Không có sản phẩm nào có quantity = 0.");
        }
    }
    private int readInt(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên hợp lệ");
            }
        }
    }

    private double readDouble(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số tiền hợp lệ");
            }
        }
    }

    private String readNonEmptyString(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Chuỗi không được để trống. Vui lòng nhập lại.");
        }
    }
}
