package HN_KS24_CNTT5_NguyenTheKien;

import java.util.ArrayList;
import java.util.List;

public class ProductDatabase {
                                                                        
        private static ProductDatabase instance;

        private List<Product> products;

        private ProductDatabase() {
            products = new ArrayList<>();
        }

        public static ProductDatabase getInstance() {
            if (instance == null) {
                instance = new ProductDatabase();
            }
            return instance;
        }

        public void addProduct(Product p) {
            products.add(p);
        }

        public List<Product> getAll() {
            return products;
        }

        public Product findById(String id) {
            for (Product p : products) {
                if (p.getId().equalsIgnoreCase(id)) {
                    return p;
                }
            }
            return null;
        }

        public boolean deleteById(String id) {
            Product p = findById(id);
            if (p != null) {
                products.remove(p);
                return true;
            }
            return false;
        }
    }

