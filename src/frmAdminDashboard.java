import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmAdminDashboard extends JFrame {
    private JPanel pnlContent;
    private JLabel lblWelcome;
    private JButton btnMangeUsers;
    private JButton btnManageRestuarants;
    private JLabel lblUserCount;
    private JLabel lblRestaurantCount;
    private User loggedUser;

    public frmAdminDashboard(User user) {
        this.loggedUser = user;

        setTitle("Admin Dashboard");
        setContentPane(pnlContent);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Ensure loggedUser is initialized before using it
        if (loggedUser != null) {
            lblWelcome.setText("Welcome, Admin " + loggedUser.getFirstName() + " " + loggedUser.getLastName());
        } else {
            lblWelcome.setText("Welcome, Admin");
        }

        // Load and display system statistics
        loadSystemStatistics();

        btnMangeUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmManageUsers();
            }
        });

        btnManageRestuarants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmManageRestaurants();
            }
        });

        setVisible(true);
    }

    private void loadSystemStatistics() {
        DBAccess db = DBAccess.getInstance();

        // Get user count
        int userCount = getUserCount(db);
        lblUserCount.setText("Total Users: " + userCount);

        // Get restaurant count
        int restaurantCount = getRestaurantCount(db);
        lblRestaurantCount.setText("Total Restaurants: " + restaurantCount);
    }

    private int getUserCount(DBAccess db) {
        int count = 0;
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users");
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching user count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return count;
    }

    private int getRestaurantCount(DBAccess db) {
        int count = 0;
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM restaurants");
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching restaurant count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return count;
    }

    // Constructor for testing without a user
    public frmAdminDashboard() {
        setTitle("Admin Dashboard");
        setContentPane(pnlContent);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        lblWelcome.setText("Welcome, Admin");

        // Load and display system statistics
        loadSystemStatistics();

        btnMangeUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmManageUsers();
            }
        });

        btnManageRestuarants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmManageRestaurants();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new frmAdminDashboard();
    }
}
