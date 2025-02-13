import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmTest extends JFrame {
    private JButton btnRegister;
    private JButton btnLogin;
    private JPanel pnlContent;
    private JLabel lblLogo;

    frmTest(){
        setSize(600, 800);
        pnlContent.setPreferredSize(new Dimension(600, 800)); // Set preferred size for the main panel
        pack(); // Resize the frame to fit the preferred size of its components
        setContentPane(pnlContent);
        setTitle("Dine Dash Home Page");
        pack();
        setLocationRelativeTo(null);// center the frame
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmRegister();
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmLogin();
            }
        });
    }

    public static void main(String[] args) {
        new frmTest();
    }
}
