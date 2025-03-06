import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class frmAssignStaff extends JFrame {
    private JPanel pnlContent;
    private JComboBox<String> cmbStaff;
    private JComboBox<String> cmbRestaurants;
    private JButton btnAssign;

    public frmAssignStaff() {
        setTitle("Assign Staff to Restaurant");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(pnlContent);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        loadStaffData();
        loadRestaurantData();

        btnAssign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignStaffToRestaurant();
            }
        });
    }

    private void loadStaffData() {
        DBAccess db = DBAccess.getInstance();
        List<User> staffMembers = db.getAllStaff();
        cmbStaff.removeAllItems();
        for (User staff : staffMembers) {
            cmbStaff.addItem(staff.getUserName());
        }
    }

    private void loadRestaurantData() {
        DBAccess db = DBAccess.getInstance();
        List<Restaurant> restaurants = db.getAllRestaurants();
        cmbRestaurants.removeAllItems();
        for (Restaurant restaurant : restaurants) {
            cmbRestaurants.addItem(restaurant.getName());
        }
    }

    private void assignStaffToRestaurant() {
        String selectedStaff = (String) cmbStaff.getSelectedItem();
        String selectedRestaurant = (String) cmbRestaurants.getSelectedItem();

        if (selectedStaff == null || selectedRestaurant == null) {
            JOptionPane.showMessageDialog(this, "Please select both staff and restaurant.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DBAccess db = DBAccess.getInstance();
        User staff = db.getUser(selectedStaff);
        Restaurant restaurant = db.getRestaurantByName(selectedRestaurant);

        if (staff != null && restaurant != null) {
            boolean success = db.updateStaffRestaurant(staff.getId(), restaurant.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Staff assigned to restaurant successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to assign staff to restaurant.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new frmAssignStaff();
    }
}