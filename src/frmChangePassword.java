import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmChangePassword extends JFrame {
    private JPanel pnlContent;
    private JPasswordField txtCurrentPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnChangePassword;
    private User loggedUser;
    private JLabel lblCurrent;
    private JLabel lblNew;
    private JLabel lblConfirm;


    public frmChangePassword(User user) {
        this.loggedUser = user;

        setTitle("Change Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(pnlContent);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        btnChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String currentPassword = new String(txtCurrentPassword.getPassword());
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DBAccess db = DBAccess.getInstance();
        if (!db.authenticateUser(loggedUser.getUserName(), currentPassword)) {
            JOptionPane.showMessageDialog(this, "Current password is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (db.updateUserPassword(loggedUser.getId(), newPassword)) {
            JOptionPane.showMessageDialog(this, "Password changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to change password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // For testing purposes
        User testUser = new Customer("testUser", "Test", "User", "test@example.com", "password");
        new frmChangePassword(testUser);
    }
}