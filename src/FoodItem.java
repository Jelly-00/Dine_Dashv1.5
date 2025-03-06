public class FoodItem extends MenuItem {
    private String servingSize;

    public FoodItem(int id, int menuId, String name, double price, String servingSize) {
        super(id, menuId, name, price);
        this.servingSize = servingSize;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }
}