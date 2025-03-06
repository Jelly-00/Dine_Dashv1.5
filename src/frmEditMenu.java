import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JButton btnAddItem;
    private JButton btnEditItem;
    private JButton btnDeleteItem;
    private JPanel pnlButtons;
    private JButton btnBack;

    private int restaurantId;
    private DBAccess db;
    private DefaultTableModel tableModel;

    // Default constructor - shows error message when no restaurant ID is provided
    public frmEditMenu() {
        JOptionPane.showMessageDialog(null,
                "No restaurant ID provided. Please login as restaurant staff.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        dispose();
    }

    public frmEditMenu(int restaurantId) {
        this.restaurantId = restaurantId;
        this.db = DBAccess.getInstance();

        setTitle("Edit Restaurant Menu");
        setContentPane(pnlContent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initializeComponents();
        setupListeners();
        loadMenuItems();

        setVisible(true);
    }

    private void initializeComponents() {

        String[] columnNames = {"ID", "Name", "Price", "Type", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMenuItems.setModel(tableModel);
        tblMenuItems.getColumnModel().getColumn(0).setMinWidth(0);
        tblMenuItems.getColumnModel().getColumn(0).setMaxWidth(0);


        cmbType.addItem("Food");
        cmbType.addItem("Drink");


        updateTypeSpecificFields();
    }

    private void setupListeners() {
        btnAddItem.addActionListener(e -> addMenuItem());
        btnEditItem.addActionListener(e -> editMenuItem());
        btnDeleteItem.addActionListener(e -> deleteMenuItem());
        btnBack.addActionListener(e -> dispose());

        cmbType.addActionListener(e -> updateTypeSpecificFields());

        tblMenuItems.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblMenuItems.getSelectedRow() != -1) {
                populateFields(tblMenuItems.getSelectedRow());
            }
        });
    }

    private void updateTypeSpecificFields() {
        String selectedType = (String) cmbType.getSelectedItem();
        if ("Food".equals(selectedType)) {
            txtServingSize.setEnabled(true);
            chkAlcoholic.setEnabled(false);
            chkAlcoholic.setSelected(false);
        } else {
            txtServingSize.setEnabled(false);
            txtServingSize.setText("");
            chkAlcoholic.setEnabled(true);
        }
    }

    private void loadMenuItems() {
        tableModel.setRowCount(0);
        List<MenuItem> items = db.getMenuItemsByRestaurantId(restaurantId);

        for (MenuItem item : items) {
            Object[] row = new Object[5];
            row[0] = item.getId();
            row[1] = item.getName();
            row[2] = item.getPrice();
            row[3] = (item instanceof FoodItem) ? "Food" : "Drink";
            row[4] = item.getDescription();
            tableModel.addRow(row);
        }
    }

    private void populateFields(int selectedRow) {
        if (selectedRow >= 0) {
            int itemId = (int) tableModel.getValueAt(selectedRow, 0);
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            double price = (double) tableModel.getValueAt(selectedRow, 2);
            String type = (String) tableModel.getValueAt(selectedRow, 3);
            String description = (String) tableModel.getValueAt(selectedRow, 4);

            txtName.setText(name);
            txtPrice.setText(String.valueOf(price));
            cmbType.setSelectedItem(type);
            txtDescription.setText(description);


            List<MenuItem> items = db.getMenuItemsByRestaurantId(restaurantId);
            for (MenuItem item : items) {
                if (item.getId() == itemId) {
                    if (item instanceof FoodItem) {
                        txtServingSize.setText(((FoodItem) item).getServingSize());
                        chkAlcoholic.setSelected(false);
                    } else if (item instanceof DrinkItem) {
                        txtServingSize.setText("");
                        chkAlcoholic.setSelected(((DrinkItem) item).isAlcoholic());
                    }
                    break;
                }
            }
        }
    }

    private void addMenuItem() {
        try {
            validateInputs();

            String name = txtName.getText().trim();
            double price = Double.parseDouble(txtPrice.getText().trim());
            String type = (String) cmbType.getSelectedItem();
            String description = txtDescription.getText().trim();
            String servingSize = null;
            Boolean isAlcoholic = null;

            if ("Food".equals(type)) {
                servingSize = txtServingSize.getText().trim();
            } else {
                isAlcoholic = chkAlcoholic.isSelected();
            }

            boolean success = db.addMenuItem(restaurantId, name, price, type, description, servingSize, isAlcoholic);

            if (success) {
                JOptionPane.showMessageDialog(this, "Menu item added successfully");
                clearFields();
                loadMenuItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add menu item", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editMenuItem() {
        int selectedRow = tblMenuItems.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            validateInputs();

            int itemId = (int) tableModel.getValueAt(selectedRow, 0);
            String name = txtName.getText().trim();
            double price = Double.parseDouble(txtPrice.getText().trim());
            String type = (String) cmbType.getSelectedItem();
            String description = txtDescription.getText().trim();
            String servingSize = null;
            Boolean isAlcoholic = null;

            if ("Food".equals(type)) {
                servingSize = txtServingSize.getText().trim();
            } else {
                isAlcoholic = chkAlcoholic.isSelected();
            }

            boolean success = db.updateMenuItem(itemId, name, price, type, description, servingSize, isAlcoholic);

            if (success) {
                JOptionPane.showMessageDialog(this, "Menu item updated successfully");
                clearFields();
                loadMenuItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update menu item", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMenuItem() {
        int selectedRow = tblMenuItems.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int itemId = (int) tableModel.getValueAt(selectedRow, 0);
        String itemName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + itemName + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = db.deleteMenuItem(itemId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Menu item deleted successfully");
                clearFields();
                loadMenuItems();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete menu item", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void validateInputs() throws Exception {
        String name = txtName.getText().trim();
        String priceText = txtPrice.getText().trim();
        String type = (String) cmbType.getSelectedItem();

        if (name.isEmpty()) {
            throw new Exception("Name cannot be empty");
        }

        try {
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                throw new Exception("Price must be greater than zero");
            }
        } catch (NumberFormatException e) {
            throw new Exception("Please enter a valid price");
        }

        if ("Food".equals(type) && txtServingSize.getText().trim().isEmpty()) {
            throw new Exception("Serving size is required for food items");
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        txtServingSize.setText("");
        chkAlcoholic.setSelected(false);
        cmbType.setSelectedIndex(0);
        tblMenuItems.clearSelection();
    }

    public static void main(String[] args) {
        new frmEditMenu();
    }
}