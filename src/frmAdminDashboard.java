import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmAdminDashboard extends JFrame {
    private JPanel pnlContent;
    private JLabel lblWelcome;
    private JButton btnMangeUsers;
    private JButton btnManageRestuarants;
    private User loggedUser;

    public frmAdminDashboard(User user) {
        this.loggedUser = user;

        setTitle("Admin Dashboard");
        setContentPane(pnlContent);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        // Ensure loggedUser is initialized before using it
        if (loggedUser != null) {
            lblWelcome.setText("Welcome, Admin " + loggedUser.getFirstName() + " " + loggedUser.getLastName());
        } else {
            lblWelcome.setText("Welcome, Admin");
        }

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
    }

    // Constructor for testing without a user
    public frmAdminDashboard() {
        setTitle("Admin Dashboard");
        setContentPane(pnlContent);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        lblWelcome.setText("Welcome, Admin");

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
    }

    public static void main(String[] args) {
        new frmAdminDashboard();
    }
}