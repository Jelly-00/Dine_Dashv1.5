public class DrinkItem extends MenuItem {
    private boolean isAlcoholic;

    public DrinkItem(int id, int menuId, String name, double price, boolean isAlcoholic) {
        super(id, menuId, name, price);
        this.isAlcoholic = isAlcoholic;
    }

    public boolean isAlcoholic() {
        return isAlcoholic;
    }

    public void setAlcoholic(boolean alcoholic) {
        isAlcoholic = alcoholic;
    }
}