public class Customer extends User {
    public Customer(String userName, String firstName, String lastName, String email, String password) {
        super(userName, firstName, lastName, email, password);
    }

    public Customer(int id, String userName, String firstName, String lastName, String email, String password) {
        super(id, userName, firstName, lastName, email, password);
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    public int getId() {
        return super.getId();
    }

    public String getFirstName() {
        return super.getFirstName();
    }
    public String getLastName() {
        return super.getLastName();

    }
    public String getEmail() {
        return super.getEmail();
    }
    public String getPassword() {
        return super.getPassword();
    }
}
