import javax.swing.*;
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
    private JButton btnLogout;

    public frmHomePage(User loggedInUser) {
        setTitle("Home Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(pnlContent);
        setSize(600, 800);
        setLocationRelativeTo(null);
        setVisible(true);

        lblWelcome.setText("Welcome, " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName());

        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmBrowseRestaurants();
            }
        });

        btnAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmAdminDashboard(loggedInUser);
            }
        });

        btnRestaurantManage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmManageRestaurants();
            }
        });

        btnChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmChangePassword(loggedInUser);
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }

    private void logout() {
        JOptionPane.showMessageDialog(this, "You have been logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new frmLogin();
    }

    public static void main(String[] args) {
        User testUser = new Customer("testUser", "Test", "User", "test@example.com", "password");
        new frmHomePage(testUser);
    }
}