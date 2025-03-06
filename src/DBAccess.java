import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBAccess {

    private static final String URL = "jdbc:h2:file:./DineDashDB/usersdb";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private static DBAccess instance;
    private Connection connection;

    private DBAccess() {
        getConnection();
    }

    public static DBAccess getInstance() {
        if (instance == null) {
            instance = new DBAccess();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error obtaining database connection: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, first_name, last_name, role, email, password FROM users";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password")
                ) {
                    @Override
                    public String getRole() {
                        return "";
                    }
                });
            }
        } catch (SQLException e) {
            System.err.println(" Fetch users error: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT id, name, cuisine FROM restaurants";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                restaurants.add(new Restaurant(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cuisine")
                ));
            }
        } catch (SQLException e) {
            System.err.println(" Fetch restaurants error: " + e.getMessage());
            e.printStackTrace();
        }
        return restaurants;
    }

    public boolean deleteRestaurant(int id) {
        String sql = "DELETE FROM restaurants WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
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

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
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

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getCuisine());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding restaurant: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUserNameUnique(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            System.err.println(" Username check error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println(" Authentication error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean addUser(User user, String role) {
        String sql = "INSERT INTO users (username, first_name, last_name, email, password, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, role.toLowerCase());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println(" Add user error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    switch (rs.getString("role").toLowerCase()) {
                        case "customer":
                            return new Customer(
                                    rs.getInt("id"),
                                    rs.getString("username"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("email"),
                                    rs.getString("password")
                            );
                        case "staff":
                            return new RestaurantStaff(
                                    rs.getInt("id"),
                                    rs.getString("username"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("email"),
                                    rs.getString("password"),
                                    rs.getInt("restaurant_id")
                            );
                        case "admin":
                            return new Admin(
                                    rs.getInt("id"),
                                    rs.getString("username"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("email"),
                                    rs.getString("password")
                            );
                    }
                }
            } catch (SQLException e) {
                System.err.println(" Authentication error: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
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

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
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

    public boolean addMenuItem(int restaurantId, String name, double price, String type,
                               String description, String servingSize, Boolean isAlcoholic) {
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

    private int getOrCreateMenuForRestaurant(int restaurantId) {
        String checkRestaurantSql = "SELECT id FROM restaurants WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkRestaurantSql)) {

            checkStmt.setInt(1, restaurantId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    return -1;
                }
            }

            String getMenuSql = "SELECT id FROM menus WHERE restaurant_id = ? LIMIT 1";
            try (PreparedStatement getMenuStmt = conn.prepareStatement(getMenuSql)) {
                getMenuStmt.setInt(1, restaurantId);
                try (ResultSet rs = getMenuStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }

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

    public boolean updateUserPassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllStaff() {
        List<User> staffMembers = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'Staff'";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                staffMembers.add(new RestaurantStaff(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("restaurant_id")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Fetch staff members error: " + e.getMessage());
            e.printStackTrace();
        }
        return staffMembers;
    }

    public Restaurant getRestaurantByName(String name) {
        String sql = "SELECT id, name, cuisine FROM restaurants WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Restaurant(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("cuisine")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println(" Error fetching restaurant by name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStaffRestaurant(int userId, int restaurantId) {
        String sql = "UPDATE users SET restaurant_id = ? WHERE id = ? AND role = 'Staff'";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            stmt.setInt(2, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
