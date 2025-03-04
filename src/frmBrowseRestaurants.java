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

                    int restaurantId = (int) tblBrowse.getValueAt(selectedRow, 0);
                    loadMenuItems(restaurantId);
                }
            });
        }

        private void loadRestaurantData() {
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
            tblBrowse.setModel(model);
            tblBrowse.revalidate();
            tblBrowse.repaint();
        }

    private void loadMenuItems(int restaurantId) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Name", "Price", "Type", "Description", "Serving Size/Alcoholic"});

        try {
            DBAccess db = DBAccess.getInstance();
            List<MenuItem> menuItems = db.getMenuItemsByRestaurantId(restaurantId);
            model.setRowCount(0);

            for (MenuItem menuItem : menuItems) {
                String typeSpecificInfo = "";
                if (menuItem instanceof FoodItem) {
                    typeSpecificInfo = ((FoodItem) menuItem).getServingSize();
                } else if (menuItem instanceof DrinkItem) {
                    typeSpecificInfo = ((DrinkItem) menuItem).isAlcoholic() ? "Alcoholic" : "Non-alcoholic";
                }

                model.addRow(new Object[]{
                        menuItem.getName(),
                        menuItem.getPrice(),
                        menuItem instanceof FoodItem ? "Food" : "Drink",
                        menuItem.getDescription(), // Add description here
                        typeSpecificInfo
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

//    public frmBrowseRestaurants() {
//        setTitle("Browse Restaurants");
//        setContentPane(panel1);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(800, 600);
//        setLocationRelativeTo(null);
//        setVisible(true);
//
//        loadRestaurantData();
//    }
//
//    private void loadRestaurantData() {
//        DefaultTableModel model = new DefaultTableModel();
//        model.setColumnIdentifiers(new String[]{"ID", "Name", "Cuisine Type"});
//
//        try {
//            DBAccess db = DBAccess.getInstance();
//            List<Restaurant> restaurants = db.getAllRestaurants();
//            model.setRowCount(0);
//
//            for (Restaurant restaurant : restaurants) {
//                model.addRow(new Object[]{
//                        restaurant.getId(),
//                        restaurant.getName(),
//                        restaurant.getCuisine(),
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, e.getMessage(), "Error loading restaurants", JOptionPane.ERROR_MESSAGE);
//        }
//        tblBrowse.setModel(model);
//        tblBrowse.revalidate();
//        tblBrowse.repaint();
//    }
//
//    public static void main(String[] args) {
//        new frmBrowseRestaurants();
//    }
//}