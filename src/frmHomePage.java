import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmHomePage extends JFrame {
    private JPanel pnlContent;
    private JLabel lblWelcome;
    private JLabel lblLogo;
    private JButton btnBrowse;
    private JButton btnEditMenu;
    private JButton btnAdmin;
    private JButton btnRestaurantManage;
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
        btnAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmAdminDashboard(loggedUser);
            }
        });
        btnRestaurantManage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new frmEditMenu();
            }
        });
    }

}
