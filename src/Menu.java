import java.util.List;

public class Menu
{
private int id;
private int restaurantID;
private String name;
private List<MenuItem> menuItems;

//private List<MenuItem> menuItems;


    public Menu(int restaurantID, String name) {
        this.restaurantID = restaurantID;
        this.name = name;
    }
}
