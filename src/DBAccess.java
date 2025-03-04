import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBAccess {

    // Database connection URL for H2 database
    private static final String URL = "jdbc:h2:file:./DineDashDB/usersdb";

    // Database username and password
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    // Singleton instance of DBAccess
    private static DBAccess instance;

    // Database connection object
    private Connection connection;

    // Private constructor to enforce Singleton pattern
    private DBAccess() {
        connect(); // Establish database connection when the instance is created
    }

    // Method to get the Singleton instance of DBAccess
    public static DBAccess getInstance() {
        if (instance == null) {
            instance = new DBAccess(); // Create a new instance if it doesn't exist
        }
        return instance;
    }

    public static boolean addRestaurant(String name, String cuisine) {
        return false;
    }

    // Method to get the database connection
    public Connection getConnection() {
        try {
            // Check if the connection is null or closed
            if (connection == null || connection.isClosed()) {
                // Establish a new connection if necessary
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            // Handle connection errors
            System.err.println("❌ Error obtaining database connection: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    // Method to establish a connection to the database
    private void connect() {
        try {
            // Check if the connection is null or closed
            if (connection == null || connection.isClosed()) {
                // Establish a new connection
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ Database connection established.");
            }
        } catch (SQLException e) {
            // Handle connection errors
            System.err.println("❌ Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to fetch all users from the database
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(); // List to store users
        String sql = "SELECT id, username, first_name, last_name, role, email, password FROM users"; // SQL query

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password")   //showing password
                        // "*****"  // Masking password for security
                ) {
                    @Override
                    public String getRole() {
                        return "";
                    }
                });
            }
        } catch (SQLException e) {
            // Handle SQL errors
            System.err.println("❌ Fetch users error: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
    public  List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>(); // List to store restaurants
        String sql = "SELECT id, name, cuisine FROM restaurants"; // Corrected SQL query

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            // Execute the query and get the ResultSet

            // rs.next() moves the cursor to the next row in the ResultSet
            // It returns `true` if there is a next row, and `false` if there are no more rows
            while (rs.next()) {
                // rs.getInt("id"): Retrieves the value of the "id" column as an integer
                // rs.getString("name"): Retrieves the value of the "name" column as a String
                // rs.getString("cuisine"): Retrieves the value of the "cuisine" column as a String
                restaurants.add(new Restaurant(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cuisine")
                ));
            }
        } catch (SQLException e) {
            // Handle SQL errors
            System.err.println("❌ Fetch restaurants error: " + e.getMessage());
            e.printStackTrace();
        }
        return restaurants;
    }

    public boolean deleteRestaurant(int id) {
        String sql = "DELETE FROM restaurants WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting restaurant: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRestaurant(Restaurant updatedRestaurant) {
        String sql = "UPDATE restaurants SET name = ?, cuisine = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, updatedRestaurant.getName());
            stmt.setString(2, updatedRestaurant.getCuisine());
            stmt.setInt(3, updatedRestaurant.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating restaurant: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public boolean addRestaurant(Restaurant restaurant) {
        String sql = "INSERT INTO restaurants (name, cuisine) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getCuisine());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding restaurant: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Method to check if a username is unique
    public boolean isUserNameUnique(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?"; // SQL query
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username); // Set the username parameter in the query

            // Execute the query and get the ResultSet
            try (ResultSet rs = stmt.executeQuery()) {
                // rs.next() moves the cursor to the first row of the ResultSet
                // Since this query returns a single row with a count, rs.next() will return true if the count is available
                if (rs.next()) {
                    // rs.getInt(1): Retrieves the value of the first column in the ResultSet as an integer
                    // This is the count of rows where the username matches
                    // If the count is 0, the username is unique
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            // Handle SQL errors
            System.err.println("❌ Username check error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean authenticateUser(String username, String password) {
        // SQL query to check if a user with the given username and password exists in the "users" table.
        // The COUNT(*) function returns the number of matching rows (should be 1 if credentials are correct).
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";

        // Using try-with-resources to automatically close the PreparedStatement and ResultSet after execution.
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set the first parameter (?) in the query to the provided username.
            stmt.setString(1, username);
            // Set the second parameter (?) in the query to the provided password.
            stmt.setString(2, password);

            // Execute the query and store the result set.
            try (ResultSet rs = stmt.executeQuery()) {
                // Move to the first row of the result set and check if the count is greater than 0.
                // If a matching record exists, authentication is successful (returns true).
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            // Handle any SQL errors that may occur during execution.
            System.err.println("❌ Authentication error: " + e.getMessage());
            e.printStackTrace();
        }

        // Return false if an exception occurs or if no matching user was found.
        return false;
    }


    // Method to add a user with a specific role
    public boolean addUser(User user, String role) {
        String sql = "INSERT INTO users (username, first_name, last_name, email, password, role) VALUES (?, ?, ?, ?, ?, ?)"; // SQL query
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters for the SQL query
            stmt.setString(1, user.getUserName()); // Set the username parameter
            stmt.setString(2, user.getFirstName()); // Set the first name parameter
            stmt.setString(3, user.getLastName()); // Set the last name parameter
            stmt.setString(4, user.getEmail()); // Set the email parameter
            stmt.setString(5, user.getPassword()); // Set the password parameter
            stmt.setString(6, role.toLowerCase()); // Set the role parameter (converted to lowercase)

            // stmt.executeUpdate() executes the SQL INSERT statement
            // It returns the number of rows affected by the query
            int rowsAffected = stmt.executeUpdate();

            // If rowsAffected > 0, the user was successfully added
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Handle SQL errors
            System.err.println("❌ Add user error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    // Method to get a user by username
    public User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ? "; // SQL query
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username); // Set the username parameter

            // Execute the query and get the ResultSet
            try (ResultSet rs = stmt.executeQuery()) {
                // rs.next() moves the cursor to the first row of the ResultSet
                // If a row exists, create a User object
                if (rs.next()) {
                    switch (rs.getString("role").toLowerCase()) {
                      case "customer":

                          return new Customer(
                                  rs.getInt("id"),
                                  rs.getString("username"),
                                  rs.getString("first_name"),
                                  rs.getString("last_name"),
                                  rs.getString("email"),
                                  rs.getString("password") // Not masking password
                          );

                      case "staff":

                          return new RestaurantStaff(
                                  rs.getInt("id"),
                                  rs.getString("username"),
                                  rs.getString("first_name"),
                                  rs.getString("last_name"),
                                  rs.getString("email"),
                                  rs.getString("password") ,
                                  rs.getInt("restaurant_id")// Not masking password
                          );

                        case "admin":
                            return new Admin(
                                    rs.getInt("id"),
                                    rs.getString("username"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("email"),
                                    rs.getString("password") // Not masking password
                            );


                }
            }
        } catch (SQLException e) {
            // Handle SQL errors
            System.err.println("❌ Authentication error: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Return null if the user is not found
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User updatedUser) {
        String sql = "UPDATE users SET username = ?, first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, updatedUser.getUserName());
            stmt.setString(2, updatedUser.getFirstName());
            stmt.setString(3, updatedUser.getLastName());
            stmt.setString(4, updatedUser.getEmail());
            stmt.setString(5, updatedUser.getPassword());
            stmt.setInt(6, updatedUser.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<MenuItem> getMenuItemsByRestaurantId(int restaurantId) {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT mi.*, m.id as menu_id FROM menu_items mi " +
                "JOIN menus m ON mi.menu_id = m.id " +
                "WHERE m.restaurant_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int menuId = rs.getInt("menu_id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    String type = rs.getString("type");
                    String description = rs.getString("description");

                    MenuItem item;
                    if ("Food".equalsIgnoreCase(type)) {
                        String servingSize = rs.getString("serving_size");
                        item = new FoodItem(id, menuId, name, price, servingSize);
                    } else {
                        boolean isAlcoholic = rs.getBoolean("is_alcoholic");
                        item = new DrinkItem(id, menuId, name, price, isAlcoholic);
                    }
                    item.setDescription(description);
                    menuItems.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    /**
     * Add a menu item to a restaurant
     */
    public boolean addMenuItem(int restaurantId, String name, double price, String type,
                               String description, String servingSize, Boolean isAlcoholic) {
        // First get the menu ID for this restaurant (or create one if it doesn't exist)
        int menuId = getOrCreateMenuForRestaurant(restaurantId);
        if (menuId == -1) {
            return false;
        }

        String sql = "INSERT INTO menu_items (menu_id, name, price, type, description, serving_size, is_alcoholic) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, menuId);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.setString(4, type);
            stmt.setString(5, description);

            // Handle type-specific fields
            if ("Food".equalsIgnoreCase(type)) {
                stmt.setString(6, servingSize);
                stmt.setNull(7, java.sql.Types.BOOLEAN);
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
                stmt.setBoolean(7, isAlcoholic != null && isAlcoholic);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update an existing menu item
     */
    public boolean updateMenuItem(int itemId, String name, double price, String type,
                                  String description, String servingSize, Boolean isAlcoholic) {
        String sql = "UPDATE menu_items SET name = ?, price = ?, type = ?, " +
                "description = ?, serving_size = ?, is_alcoholic = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, type);
            stmt.setString(4, description);

            // Handle type-specific fields
            if ("Food".equalsIgnoreCase(type)) {
                stmt.setString(5, servingSize);
                stmt.setNull(6, java.sql.Types.BOOLEAN);
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
                stmt.setBoolean(6, isAlcoholic != null && isAlcoholic);
            }

            stmt.setInt(7, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a menu item
     */
    public boolean deleteMenuItem(int itemId) {
        String sql = "DELETE FROM menu_items WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get or create a menu for a restaurant
     */
    private int getOrCreateMenuForRestaurant(int restaurantId) {
        // First check if the restaurant exists
        String checkRestaurantSql = "SELECT id FROM restaurants WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkRestaurantSql)) {

            checkStmt.setInt(1, restaurantId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    // Restaurant doesn't exist
                    return -1;
                }
            }

            // Get existing menu if available
            String getMenuSql = "SELECT id FROM menus WHERE restaurant_id = ? LIMIT 1";
            try (PreparedStatement getMenuStmt = conn.prepareStatement(getMenuSql)) {
                getMenuStmt.setInt(1, restaurantId);
                try (ResultSet rs = getMenuStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }

            // Create new menu if none exists
            String createMenuSql = "INSERT INTO menus (restaurant_id, name, description) VALUES (?, ?, ?)";
            try (PreparedStatement createMenuStmt = conn.prepareStatement(createMenuSql,
                    Statement.RETURN_GENERATED_KEYS)) {

                createMenuStmt.setInt(1, restaurantId);
                createMenuStmt.setString(2, "Main Menu");
                createMenuStmt.setString(3, "Primary menu for restaurant #" + restaurantId);

                if (createMenuStmt.executeUpdate() > 0) {
                    try (ResultSet generatedKeys = createMenuStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
