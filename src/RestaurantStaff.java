public class RestaurantStaff extends User {
    private int restaurantId;

    public RestaurantStaff(int id, String userName, String firstName, String lastName, String email, String password, int restaurantId) {
        super(id, userName, firstName, lastName, email, password);
        this.restaurantId = restaurantId; // Fix: assign the parameter to the instance variable
    }

    @Override
    public String getRole() {
        return "Staff";
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

}