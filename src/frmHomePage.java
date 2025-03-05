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
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(pnlContent);
        setSize(800, 600);
        setLocationRelativeTo(null);


        //dynamically set label to user deets
        setVisible(true);
        lblWelcome.setText("Welcome " + loggedUser.getFirstName() + " " + loggedUser.getLastName());
        JOptionPane.showMessageDialog(null, "Welcome " + loggedUser.getRole() );
        //lblLogo.setIcon(new ImageIcon("images/logo.png"));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setPreferredSize(new Dimension(400,300));
        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmBrowseRestaurants();
            }
        });
        if (loggedUser != null && "Admin".equals(loggedUser.getRole())) {
            btnAdmin.setVisible(true);
            btnAdmin.setEnabled(true);
        } else {
            btnAdmin.setVisible(false);
            btnAdmin.setEnabled(false);
        }

        btnAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        btnRestaurantManage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loggedUser != null && "Staff".equals(loggedUser.getRole())) {
                    // Get the restaurant ID from the staff user
                    RestaurantStaff staffUser = (RestaurantStaff) loggedUser;
                    int restaurantId = staffUser.getRestaurantId();

                    if (restaurantId > 0) {
                        // Open the menu management form for this restaurant
                        new frmEditMenu();
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
            }
        });
        btnChangePassword = new JButton("Change Password");
        pnlContent.add(btnChangePassword);

        btnChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmChangePassword(loggedUser);
            }
        });
    }
}