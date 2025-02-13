import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    private JPanel pnlContent;
    private JLabel lblWelcome;
    private JLabel lblLogo;
    private User loggedUser;

    public HomePage(User loggedUser) {
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
    }
}
