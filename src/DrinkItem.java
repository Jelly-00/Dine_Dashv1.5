public class DrinkItem extends MenuItem {
 private String servingSize;
 private boolean isAlcholic;

    public DrinkItem( int quantity, double price, String name, int menu_id, String servingSize,boolean isAlcholic) {
        super( quantity, price, name, menu_id);
    }

    public boolean isAlcholic() {
        return isAlcholic;
    }

    public void setAlcholic(boolean alcholic) {
        isAlcholic = alcholic;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }
}
