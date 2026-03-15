package demo;

public class Shop extends Thread {
    private String name;
    static int quantity=10;

    public Shop(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        while (quantity > 0) {
            System.out.println("còn lại trong kho: " + quantity);
            System.out.println(name + " đã bán 1");
            quantity--;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
