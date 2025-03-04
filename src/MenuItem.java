public abstract class MenuItem {
    private int id;
    private int menuId;
    private String name;
    private double price;
    private String description;

    public MenuItem(int id, int menuId, String name, double price) {
        this.id = id;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.description = "";
    }

    public int getId() {
        return id;
    }

    public int getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}