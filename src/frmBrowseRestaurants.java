import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class frmBrowseRestaurants extends JFrame {
    private JTable tblBrowse;
    private JButton btnSelect;
    private JLabel lblText;
    private JPanel panel1;
    private JTable tblMenuItems;

    public frmBrowseRestaurants() {
        setTitle("Browse Restaurants");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        loadRestaurantData();

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblBrowse.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a restaurant.");
                    return;
                }

                Object value = tblBrowse.getValueAt(selectedRow, 0); // Assuming the ID is in the first column

                try {
                    int restaurantId = Integer.parseInt(value.toString());
                    loadMenuItems(restaurantId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid selection. Please select a valid restaurant.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void loadRestaurantData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Cuisine"});

        try {
            DBAccess db = DBAccess.getInstance();
            List<Restaurant> restaurants = db.getAllRestaurants();
            model.setRowCount(0);

            for (Restaurant restaurant : restaurants) {
                model.addRow(new Object[]{
                        restaurant.getId(),
                        restaurant.getName(),
                        restaurant.getCuisine()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error loading restaurants", JOptionPane.ERROR_MESSAGE);
        }
        tblBrowse.setModel(model);
        tblBrowse.revalidate();
        tblBrowse.repaint();
    }

    private void loadMenuItems(int restaurantId) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Price", "Type", "Description"});

        try {
            DBAccess db = DBAccess.getInstance();
            List<MenuItem> menuItems = db.getMenuItemsByRestaurantId(restaurantId);
            model.setRowCount(0);

            for (MenuItem item : menuItems) {
                model.addRow(new Object[]{
                        item.getId(),
                        item.getName(),
                        item.getPrice(),
                        (item instanceof FoodItem) ? "Food" : "Drink",
                        item.getDescription()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error loading menu items", JOptionPane.ERROR_MESSAGE);
        }
        tblMenuItems.setModel(model);
        tblMenuItems.revalidate();
        tblMenuItems.repaint();
    }

    public static void main(String[] args) {
        new frmBrowseRestaurants();
    }
}