package demo;

public class Main {
    static void main(String[] args) {
        Shop s1=new Shop("s1");
        Shop s2=new Shop("s2");
        s2.start();
        s1.start();
    }
}
