public class FoodItem  extends MenuItem{

    private String servingSize;


public FoodItem( int quantity, double price, String name, int menu_id,  String servingSize) {
        super( quantity, price, name, menu_id);
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }
}
