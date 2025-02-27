import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class frmListRestaurants extends JFrame {
    private JPanel panel1;
    private JTable tblListRes;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnEdit;
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtCuisine;
    private JLabel lblRestaurantId;
    private JLabel lblName;
    private JLabel lblCuisine;

    public frmListRestaurants() {
        setTitle("List of Restaurants");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        loadRestaurantData();

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblListRes.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a restaurant.");
                    return;
                }

                int id = (int) tblListRes.getValueAt(selectedRow, 0);
                DBAccess dbAccess = DBAccess.getInstance();
                boolean isDeleted = dbAccess.deleteRestaurant(id);

                if (isDeleted) {
                    JOptionPane.showMessageDialog(null, "Restaurant deleted successfully.");
                    loadRestaurantData();
                } else {
                    JOptionPane.showMessageDialog(null, "Restaurant not deleted.");
                }
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblListRes.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a restaurant");
                    return;
                }

                int id = (int) tblListRes.getValueAt(selectedRow, 0);
                String name = txtName.getText();
                String cuisine = txtCuisine.getText();

                if (name.isEmpty() || cuisine.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                }

                Restaurant updatedRestaurant = new Restaurant(id, name, cuisine);
                DBAccess dbAccess = DBAccess.getInstance();

                if (dbAccess.updateRestaurant(updatedRestaurant)) {
                    JOptionPane.showMessageDialog(null, "Restaurant updated");
                    loadRestaurantData();
                } else {
                    JOptionPane.showMessageDialog(null, "Restaurant not updated");
                }
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                String cuisine = txtCuisine.getText();

                if (name.isEmpty() || cuisine.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields");
                    return;
                }

                DBAccess dbAccess = DBAccess.getInstance();

                Restaurant newRestaurant = new Restaurant(0, name, cuisine);

                if (dbAccess.addRestaurant(newRestaurant)) {
                    JOptionPane.showMessageDialog(null, "Restaurant added successfully");
                    loadRestaurantData();
                } else {
                    JOptionPane.showMessageDialog(null, "Error adding restaurant");
                }
            }
        });
    }

    public void loadRestaurantData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Cuisine Type"});

        try {
            DBAccess db = DBAccess.getInstance();
            List<Restaurant> restaurants = db.getAllRestaurants();
            model.setRowCount(0);

            for (Restaurant restaurant : restaurants) {
                model.addRow(new Object[]{
                        restaurant.getId(),
                        restaurant.getName(),
                        restaurant.getCuisine(),
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error loading restaurants", JOptionPane.ERROR_MESSAGE);
        }
        tblListRes.setModel(model);
        tblListRes.revalidate();
        tblListRes.repaint();
    }

    public static void main(String[] args) {
        new frmListRestaurants();
    }
}