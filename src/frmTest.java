import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmTest extends JFrame {
    private JPanel pnlContent;
    private JButton btnRegister;
    private JButton btnLogin;
    private JLabel lblLogo;

    public frmTest() {
        setTitle("DineDash");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(pnlContent);
        setSize(600, 800);
        setLocationRelativeTo(null);
        setVisible(true);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmRegister();
                dispose();
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmLogin();
                dispose();
            }
        });

        pack();
    }

    public static void main(String[] args) {
        try {
            DBSetUp.initializeDatabase();
            new frmTest();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error initializing the application: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}