import java.util.List;

public class Users {
    private DBAccess dbAccess;

    public Users() {

        dbAccess = DBAccess.getInstance(); // Get database instance
    }

    public boolean addUser(User user) {
        if (!isUserNameUnique(user.getUserName())) {
            System.out.println("❌ Username already in use.");
            return false;
        }
        if (!isEmailUnique(user.getEmail())) {
            System.out.println("❌ Email already in use.");
            return false;
        }
        if (!isPasswordStrong(user.getPassword())) {
            System.out.println("❌ Password is not strong enough.");
            return false;
        }

        // Insert into the database
        boolean inserted = dbAccess.addUser(user,"Customer");
        if (inserted) {
            System.out.println("✅ User added successfully!");
        } else {
            System.out.println("❌ Failed to add user.");
        }
        return inserted;
    }

    private boolean isUserNameUnique(String userName) {
        List<User> users = dbAccess.getAllUsers();
        for (User user : users) {
            if (user.getUserName().equalsIgnoreCase(userName)) {
                return false; // Username already exists
            }
        }
        return true;
    }

    private boolean isEmailUnique(String email) {
        List<User> users = dbAccess.getAllUsers();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return false; // Email already exists
            }
        }
        return true;
    }

    private boolean isPasswordStrong(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[0-9].*");
    }
}