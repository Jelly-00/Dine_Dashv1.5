import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmHomePage extends JFrame {
    private JPanel pnlContent;
    private JLabel lblWelcome;
    private JLabel lblLogo;
    private JButton btnBrowse;
    private JButton btnAdmin;
    private JButton btnRestaurantManage;
    private JButton btnChangePassword;
    private User loggedUser;

    public frmHomePage(User loggedUser) {
        this.loggedUser = loggedUser;

        // Set up frame
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(pnlContent);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Set welcome message
        lblWelcome.setText("Welcome " + loggedUser.getFirstName() + " " + loggedUser.getLastName());

        // Set up logo
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setPreferredSize(new Dimension(400, 300));

        // Configure button visibility based on user role
        setupButtonVisibility();

        // Add action listeners to buttons
        setupActionListeners();

        // Show the frame
        setVisible(true);
    }

    private void setupButtonVisibility() {
        String userRole = loggedUser.getRole();

        // Admin button is only visible for Admin users
        btnAdmin.setVisible("Admin".equals(userRole));
        btnAdmin.setEnabled("Admin".equals(userRole));

        // Restaurant Management button is only visible for Staff
        btnRestaurantManage.setVisible("Staff".equals(userRole));
        btnRestaurantManage.setEnabled("Staff".equals(userRole));
    }

    private void setupActionListeners() {
        // Browse button action
        btnBrowse.addActionListener(e -> {
            new frmBrowseRestaurants();
        });

        // Admin button action
        btnAdmin.addActionListener(e -> {
            if (loggedUser != null && "Admin".equals(loggedUser.getRole())) {
                new frmAdminDashboard(loggedUser);
            } else {
                JOptionPane.showMessageDialog(
                        frmHomePage.this,
                        "You do not have permission to access the Admin Dashboard.",
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Restaurant Management button action
        btnRestaurantManage.addActionListener(e -> {
            if (loggedUser != null && "Staff".equals(loggedUser.getRole())) {
                RestaurantStaff staffUser = (RestaurantStaff) loggedUser;
                int restaurantId = staffUser.getRestaurantId();

                if (restaurantId > 0) {
                    new frmEditMenu(restaurantId);
                } else {
                    JOptionPane.showMessageDialog(
                            frmHomePage.this,
                            "You are not associated with any restaurant.",
                            "Restaurant Not Found",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                        frmHomePage.this,
                        "You do not have permission to manage restaurant menus.",
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Change Password button action
        btnChangePassword.addActionListener(e -> {
            new frmChangePassword(loggedUser);
        });
    }
}