package repository;

import model.MenuItem;
import java.util.*;

public class MenuRepository {

    private List<MenuItem> menu = new ArrayList<>();

    public void add(MenuItem item) {
        menu.add(item);
    }

    public List<MenuItem> getAll() {
        return menu;
    }

    public Optional<MenuItem> findById(String id) {
        return menu.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    public void delete(String id) {
        menu.removeIf(i -> i.getId().equals(id));
    }
}