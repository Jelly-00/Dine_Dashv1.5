import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //DBSetUp.initializeDatabase();
// Get the DBAccess Singleton instance (this initializes the DB)
        DBAccess db = DBAccess.getInstance();

        // Fetch and display users before adding a new one
        System.out.println("\n Fetching users BEFORE adding a new one...");
        List<User> usersBefore = db.getAllUsers();//returns a list of all users
        for (User user : usersBefore) {//for each user in the list
            System.out.println(user);//print the user
        }


        // Fetch and display users after adding the new one
        System.out.println("\n Fetching users AFTER adding a new one...");

    }

}