package service;

import model.MenuItem;
import repository.MenuRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MenuService {

    private MenuRepository repo;

    public MenuService(MenuRepository repo) {
        this.repo = repo;
    }

    public void addItem(MenuItem item) {
        repo.add(item);
    }

    public List<MenuItem> getAll() {
        return repo.getAll();
    }

    public void delete(String id) {
        repo.delete(id);
    }

    public List<MenuItem> searchByName(String name) {
        return repo.getAll()
                .stream()
                .filter(i -> i.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<MenuItem> searchByPrice(double min, double max) {
        return repo.getAll()
                .stream()
                .filter(i -> i.calculatePrice() >= min && i.calculatePrice() <= max)
                .collect(Collectors.toList());
    }
}