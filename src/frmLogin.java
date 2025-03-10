import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmLogin extends JFrame {
    private JPanel pnlContent;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton btnHome;
    private JButton btnChangePassword;

    public frmLogin() {
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(pnlContent);
        setSize(600, 800);
        pnlContent.setPreferredSize(new Dimension(600, 800));
        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
        btnHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmTest();
            }
        });
        btnChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User testUser = new Customer("testUser", "Test", "User", "test@example.com", "password");
                new frmChangePassword(testUser);
            }
        });
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        DBAccess db = DBAccess.getInstance();
        boolean isAuthenticated = db.authenticateUser(username, password);

        if (isAuthenticated) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            User loggedInUser = db.getUser(username);
            JOptionPane.showMessageDialog(this, "Welcome, " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName() + "!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
            new frmHomePage(loggedInUser);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new frmLogin();
    }
}