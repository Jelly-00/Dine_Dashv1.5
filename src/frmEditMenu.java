import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class frmEditMenu extends JFrame {
    private JPanel pnlContent;
    private JTable tblMenuItems;
    private JPanel pnlInputFields;
    private JTextField txtName;
    private JTextField txtPrice;
    private JComboBox<String> cmbType;
    private JTextField txtDescription;
    private JTextField txtServingSize;
    private JCheckBox chkAlcoholic;
    private JPanel pnlButtons;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JLabel lblRestaurantName;

    // Add restaurant selection components
    private JPanel pnlRestaurantSelection;
    private JComboBox<String> cmbRestaurants;
    private JButton btnSelectRestaurant;

    private int restaurantId = -1;
    private int selectedMenuItemId = -1;
    private List<Restaurant> restaurants;

    public frmEditMenu() {
        setTitle("Edit Restaurant Menu");
        setSize(800, 600);
        setLocationRelativeTo(null);

        pnlContent = new JPanel(new BorderLayout());

        // Create restaurant selection panel
        pnlRestaurantSelection = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlRestaurantSelection.add(new JLabel("Select Restaurant:"));
        cmbRestaurants = new JComboBox<>();
        pnlRestaurantSelection.add(cmbRestaurants);
        btnSelectRestaurant = new JButton("Load Restaurant Menu");
        pnlRestaurantSelection.add(btnSelectRestaurant);

        // Create restaurant name label (initially empty)
        lblRestaurantName = new JLabel("No restaurant selected");
        lblRestaurantName.setFont(new Font("Arial", Font.BOLD, 16));
        lblRestaurantName.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table
        tblMenuItems = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblMenuItems);

        // Create input fields panel
        pnlInputFields = new JPanel(new GridLayout(6, 2, 5, 5));
        pnlInputFields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlInputFields.add(new JLabel("Name:"));
        txtName = new JTextField();
        pnlInputFields.add(txtName);

        pnlInputFields.add(new JLabel("Price:"));
        txtPrice = new JTextField();
        pnlInputFields.add(txtPrice);

        pnlInputFields.add(new JLabel("Type:"));
        cmbType = new JComboBox<>(new String[]{"Food", "Drink"});
        pnlInputFields.add(cmbType);

        pnlInputFields.add(new JLabel("Description:"));
        txtDescription = new JTextField();
        pnlInputFields.add(txtDescription);

        pnlInputFields.add(new JLabel("Serving Size:"));
        txtServingSize = new JTextField();
        pnlInputFields.add(txtServingSize);

        pnlInputFields.add(new JLabel("Alcoholic:"));
        chkAlcoholic = new JCheckBox();
        pnlInputFields.add(chkAlcoholic);

        // Create buttons panel
        pnlButtons = new JPanel(new FlowLayout());

        btnAdd = new JButton("Add Item");
        btnUpdate = new JButton("Update Item");
        btnDelete = new JButton("Delete Item");

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnUpdate);
        pnlButtons.add(btnDelete);

        // Add components to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(pnlRestaurantSelection, BorderLayout.NORTH);
        topPanel.add(lblRestaurantName, BorderLayout.CENTER);

        pnlContent.add(topPanel, BorderLayout.NORTH);
        pnlContent.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(pnlInputFields, BorderLayout.CENTER);
        bottomPanel.add(pnlButtons, BorderLayout.SOUTH);

        pnlContent.add(bottomPanel, BorderLayout.SOUTH);

        // Set content pane
        setContentPane(pnlContent);

        // Load restaurants into combo box
        loadRestaurants();

        // Initially disable menu item controls until restaurant is selected
        setMenuControlsEnabled(false);

        // Add listeners
        btnSelectRestaurant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cmbRestaurants.getSelectedIndex();
                if (selectedIndex >= 0) {
                    restaurantId = restaurants.get(selectedIndex).getId();
                    String restaurantName = restaurants.get(selectedIndex).getName();
                    lblRestaurantName.setText("Restaurant: " + restaurantName);
                    loadMenuItems();
                    setMenuControlsEnabled(true);
                }
            }
        });

        cmbType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) cmbType.getSelectedItem();
                txtServingSize.setEnabled("Food".equals(selectedType));
                chkAlcoholic.setEnabled("Drink".equals(selectedType));
            }
        });

        tblMenuItems.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblMenuItems.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedMenuItemId = (int) tblMenuItems.getValueAt(selectedRow, 0);
                    txtName.setText((String) tblMenuItems.getValueAt(selectedRow, 1));
                    txtPrice.setText(String.valueOf(tblMenuItems.getValueAt(selectedRow, 2)));
                    String type = (String) tblMenuItems.getValueAt(selectedRow, 3);
                    cmbType.setSelectedItem(type);
                    txtDescription.setText((String) tblMenuItems.getValueAt(selectedRow, 4));

                    if ("Food".equals(type)) {
                        txtServingSize.setText((String) tblMenuItems.getValueAt(selectedRow, 5));
                        chkAlcoholic.setSelected(false);
                    } else {
                        txtServingSize.setText("");
                        chkAlcoholic.setSelected(tblMenuItems.getValueAt(selectedRow, 5).toString().contains("Alcoholic"));
                    }
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMenuItem();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMenuItem();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMenuItem();
            }
        });

        // Initially disable serving size or alcoholic based on selected type
        cmbType.setSelectedIndex(0);
        txtServingSize.setEnabled(true);
        chkAlcoholic.setEnabled(false);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadRestaurants() {
        try {
            DBAccess db = DBAccess.getInstance();
            restaurants = db.getAllRestaurants();

            cmbRestaurants.removeAllItems();
            for (Restaurant restaurant : restaurants) {
                cmbRestaurants.addItem(restaurant.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error loading restaurants", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setMenuControlsEnabled(boolean enabled) {
        tblMenuItems.setEnabled(enabled);
        txtName.setEnabled(enabled);
        txtPrice.setEnabled(enabled);
        cmbType.setEnabled(enabled);
        txtDescription.setEnabled(enabled);
        txtServingSize.setEnabled(enabled && "Food".equals(cmbType.getSelectedItem()));
        chkAlcoholic.setEnabled(enabled && "Drink".equals(cmbType.getSelectedItem()));
        btnAdd.setEnabled(enabled);
        btnUpdate.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
    }

    private void loadMenuItems() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.setColumnIdentifiers(new String[]{"ID", "Name", "Price", "Type", "Description", "Specific Info"});

        try {
            if (restaurantId <= 0) {
                return;
            }

            DBAccess db = DBAccess.getInstance();
            List<MenuItem> menuItems = db.getMenuItemsByRestaurantId(restaurantId);
            model.setRowCount(0);

            for (MenuItem menuItem : menuItems) {
                String typeSpecificInfo;
                if (menuItem instanceof FoodItem) {
                    typeSpecificInfo = ((FoodItem) menuItem).getServingSize();
                } else {
                    typeSpecificInfo = ((DrinkItem) menuItem).isAlcoholic() ? "Alcoholic" : "Non-alcoholic";
                }

                model.addRow(new Object[]{
                        menuItem.getId(),
                        menuItem.getName(),
                        menuItem.getPrice(),
                        menuItem instanceof FoodItem ? "Food" : "Drink",
                        menuItem.getDescription(),
                        typeSpecificInfo
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error loading menu items", JOptionPane.ERROR_MESSAGE);
        }

        tblMenuItems.setModel(model);
        tblMenuItems.revalidate();
        tblMenuItems.repaint();
    }

    private void addMenuItem() {
        // Keep the original implementation but check restaurant selection first
        if (restaurantId <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a restaurant first");
            return;
        }

        try {
            String name = txtName.getText();
            double price = Double.parseDouble(txtPrice.getText());
            String type = (String) cmbType.getSelectedItem();
            String description = txtDescription.getText();

            if (name.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and description cannot be empty");
                return;
            }

            DBAccess db = DBAccess.getInstance();
            boolean result;

            if ("Food".equals(type)) {
                String servingSize = txtServingSize.getText();
                result = db.addMenuItem(restaurantId, name, price, "Food", description, servingSize, null);
            } else {
                boolean isAlcoholic = chkAlcoholic.isSelected();
                result = db.addMenuItem(restaurantId, name, price, "Drink", description, null, isAlcoholic);
            }

            if (result) {
                JOptionPane.showMessageDialog(this, "Menu item added successfully");
                clearFields();
                loadMenuItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add menu item");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a valid number");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // updateMenuItem and deleteMenuItem methods remain the same but add check for restaurant selection
    private void updateMenuItem() {
        if (restaurantId <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a restaurant first");
            return;
        }

        if (selectedMenuItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a menu item to update");
            return;
        }

        try {
            String name = txtName.getText();
            double price = Double.parseDouble(txtPrice.getText());
            String type = (String) cmbType.getSelectedItem();
            String description = txtDescription.getText();

            if (name.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and description cannot be empty");
                return;
            }

            DBAccess db = DBAccess.getInstance();
            boolean result;

            if ("Food".equals(type)) {
                String servingSize = txtServingSize.getText();
                result = db.updateMenuItem(selectedMenuItemId, name, price, "Food", description, servingSize, null);
            } else {
                boolean isAlcoholic = chkAlcoholic.isSelected();
                result = db.updateMenuItem(selectedMenuItemId, name, price, "Drink", description, null, isAlcoholic);
            }

            if (result) {
                JOptionPane.showMessageDialog(this, "Menu item updated successfully");
                clearFields();
                loadMenuItems();
                selectedMenuItemId = -1;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update menu item");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a valid number");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteMenuItem() {
        if (restaurantId <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a restaurant first");
            return;
        }

        if (selectedMenuItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a menu item to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this menu item?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DBAccess db = DBAccess.getInstance();
            boolean result = db.deleteMenuItem(selectedMenuItemId);

            if (result) {
                JOptionPane.showMessageDialog(this, "Menu item deleted successfully");
                clearFields();
                loadMenuItems();
                selectedMenuItemId = -1;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete menu item");
            }
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        txtServingSize.setText("");
        chkAlcoholic.setSelected(false);
    }

    public static void main(String[] args) {
        new frmEditMenu();
    }
}
