import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmRegister extends JFrame {
    // Declare form components
    private JPanel pnlContent;
    private JTextField usernameField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField; // Added Confirm Password field
    private JButton registerButton;
    private JButton homePageButton;

    public frmRegister() {
        setSize(600, 800);
        setContentPane(pnlContent);
        pnlContent.setPreferredSize(new Dimension(600, 800)); // Set preferred size for the main panel
        pack(); // Resize the frame to fit the preferred size of its components
        setTitle("Register Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);

        // Debug: Check if registerButton is null
        if (registerButton == null) {
            System.out.println("registerButton is null!"); // Debug print
        } else {
            System.out.println("registerButton is initialised!"); // Debug print
        }

        // Add action listener to the button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Register button clicked!"); // Debug print
                registerUser();
            }
        });
        homePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Home Page button clicked!"); // Debug print
                new frmTest();//go home
                dispose();
            }
        });
    }

    private void registerUser() {
        // Retrieve user inputs from form fields
        String username = usernameField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword()); // Retrieve confirm password input


        if (username == null || username.trim().isEmpty()) {
            // Display error message if username is empty
            JOptionPane.showMessageDialog(this, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (firstName == null || firstName.trim().isEmpty()) {
            // Display error message if first name is empty
            JOptionPane.showMessageDialog(this, "First name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            //display error message if last name is empty
            JOptionPane.showMessageDialog(this, "Last name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            //display error message if email is empty
            JOptionPane.showMessageDialog(this, "Email cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (!email.contains("@") || !email.contains(".")) {
            //email validation
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Confirm Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        DBAccess db = DBAccess.getInstance();

        if (!db.isUserNameUnique(username)) {
            // Display error message if username already exists
            JOptionPane.showMessageDialog(this, "Username is already taken. Please choose another.");
            return;
        }


        User newUser = new User(username, firstName, lastName, email, password) {
            @Override
            public String getRole() {
                return "";
            }
        };


        boolean userAdded = db.addUser(newUser, "Customer");

        if (userAdded) {
            JOptionPane.showMessageDialog(this, "User registered successfully!");
            new frmLogin();
          //  dispose(); // ✅ Close the registration form after success
        } else {
            JOptionPane.showMessageDialog(this, "An error occurred with registration. Please try again.");

        }
    }
    public static void main(String[] args) {
        new frmRegister();
    }
}