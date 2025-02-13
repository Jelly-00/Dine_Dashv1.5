public class Admin extends User {

    public Admin(String userName, String firstName, String lastName, String email, String password) {
        super(userName, firstName, lastName, email, password);
    }

    public Admin(int id, String userName, String firstName, String lastName, String email, String password) {
        super(id, userName, firstName, lastName, email, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}
