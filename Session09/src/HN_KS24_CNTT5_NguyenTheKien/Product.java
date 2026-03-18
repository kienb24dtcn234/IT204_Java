package HN_KS24_CNTT5_NguyenTheKien;

public abstract class Product {
        protected String id;
        protected String name;
        protected double price;

        public Product() {
        }

        public Product(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public abstract void displayInfo();

}
