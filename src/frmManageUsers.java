import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class frmManageUsers extends JFrame {
    private JTable tblUsers;
    private JPanel pnlContent;
    private JPanel pnlInputFields;
    private JTextField txtUsername;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPanel pnlButtons;
    private JButton btnAddUser;
    private JButton btnEditUser;
    private JButton btnDeleteUser;
    private JLabel lblUsername;
    private JLabel lblFirstName;
    private JLabel lblLastName;
    private JLabel lblEmail;
    private JLabel lblPassword;

    public frmManageUsers() {
        this.setTitle("manageUser");
        setContentPane(pnlContent);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        this.setVisible(true);

        loadUserData();
        btnAddUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String firstName = txtFirstName.getText();
                String lastName = txtLastName.getText();
                String email = txtEmail.getText();
                String password = new String(txtPassword.getPassword());

                if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields");
                    return;
                }

                DBAccess dbAccess = DBAccess.getInstance();

                if (!dbAccess.isUserNameUnique(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists");
                    return;
                }

                Customer newUser = new Customer(username, firstName, lastName, email, password);



                if (dbAccess.addUser(newUser, "customer")) {
                    JOptionPane.showMessageDialog(null, "User added successfully");
                    loadUserData();
                } else {
                    JOptionPane.showMessageDialog(null, "Error adding user");
                }
            }
        });


        btnEditUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblUsers.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a user");
                    return;
                }

                int id = (int) tblUsers.getValueAt(selectedRow, 0);
                String username = txtUsername.getText();
                String firstName = txtFirstName.getText();
                String lastName = txtLastName.getText();
                String email = txtEmail.getText();
                String password = new String(txtPassword.getPassword());

                if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                }

                User updatedUser = new User(id, username, firstName, lastName, email, password) {
                    @Override
                    public String getRole() {
                        return "";
                    }
                };
                DBAccess dbAccess = DBAccess.getInstance();

                if (dbAccess.updateUser(updatedUser)) {
                    JOptionPane.showMessageDialog(null, "User updated");
                    loadUserData();
                } else {
                    JOptionPane.showMessageDialog(null, "User not updated");
                }
            }
        });

        btnDeleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblUsers.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a user.");
                    return;
                }

                int id = (int) tblUsers.getValueAt(selectedRow, 0);
                DBAccess dbAccess = DBAccess.getInstance();
                boolean isDeleted = dbAccess.deleteUser(id);

                if (isDeleted) {
                    JOptionPane.showMessageDialog(null, "User deleted successfully.");
                    loadUserData();
                } else {
                    JOptionPane.showMessageDialog(null, "User not deleted.");
                }
            }
        });
    }

    public void loadUserData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Username", "First Name", "Last Name", "Email"});

        try {
            DBAccess db = DBAccess.getInstance();
            List<User> users = db.getAllUsers();
            model.setRowCount(0); //  Intellisense recommended this

            for (User user : users) {
                model.addRow(new Object[]{
                        user.getId(),
                        user.getUserName(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPassword(),
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error loading users", JOptionPane.ERROR_MESSAGE);
        }
        tblUsers.setModel(model);
        tblUsers.revalidate();
        tblUsers.repaint();
    }

    public static void main(String[] args) {
        new frmManageUsers();
    }
}