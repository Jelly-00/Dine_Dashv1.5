import java.util.List;
import java.util.ArrayList;


public class Menu {
    private int id;
    private int restaurantId;
    private String name;
    private String description;
    private List<MenuItem> menuItems;

    public Menu(int id, int restaurantId, String name, String description) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.menuItems = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


}