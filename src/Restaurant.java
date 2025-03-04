import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private int id;
    private String name;
    private String cuisine;
    private List<Menu> menus;

    public Restaurant(int id, String name, String cuisine) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.menus = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void addMenu(Menu menu) {
        this.menus.add(menu);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
}