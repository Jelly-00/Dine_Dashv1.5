public abstract class MenuItem
{

    private int id;
    private int menu_id;
    private String name;
    private double price;
    private int quantity;
    ;

    public MenuItem( int quantity, double price, String name, int menu_id) {

        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.menu_id = menu_id;

    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getMenu_id() {
        return menu_id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

