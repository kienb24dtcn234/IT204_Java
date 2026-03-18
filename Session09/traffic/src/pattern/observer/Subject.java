package pattern.observer;

public interface Subject {
    void addObserver(Observer o);
    void removeObserver(Observer o); // 🔥 thêm dòng này
    void notifyObservers();
}