public class RestaurantStaff extends User {

    private int rid;
    public RestaurantStaff(int id, String userName, String firstName, String lastName, String email, String password, int rid) {
        super(id, userName, firstName, lastName, email, password);
        this.rid = rid;
    }

    public RestaurantStaff(String userName, String firstName, String lastName, String email, String password) {
        super(userName, firstName, lastName, email, password);
    }

    public int getRid() {
        return rid;
    }

    @Override
    public String getRole() {
        return "Restaurant";
    }
}
