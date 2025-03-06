import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class frmEditMenu extends JFrame {
    private JPanel pnlContent;
    private JTable tblMenuItems;
    private JButton btnAddItem;
    private JButton btnEditItem;
    private JButton btnDeleteItem;
    private JButton btnBack;
    private DefaultTableModel tableModel;
    private JPanel pnlInputFields;
    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtDescription;
    private JComboBox<String> cmbType;
    private JTextField txtServingSize;
    private JCheckBox chkAlcoholic;
    private JPanel pnlButtons;

    private int restaurantId;
    private DBAccess db;

    public frmEditMenu(int restaurantId) {
        this.restaurantId = restaurantId;
        this.db = DBAccess.getInstance();

        setTitle("Edit Restaurant Menu");
        setContentPane(pnlContent);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeTable();
        loadMenuItems();

        btnAddItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showItemDialog(null); // Pass null for new item
            }
        });

        btnEditItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblMenuItems.getSelectedRow();
                if (selectedRow >= 0) {
                    int itemId = (int) tableModel.getValueAt(selectedRow, 0);
                    MenuItem selectedItem = getMenuItemById(itemId);
                    if (selectedItem != null) {
                        showItemDialog(selectedItem);
                    }
                } else {
                    JOptionPane.showMessageDialog(frmEditMenu.this,
                            "Please select an item to edit",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnDeleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblMenuItems.getSelectedRow();
                if (selectedRow >= 0) {
                    int itemId = (int) tableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(
                            frmEditMenu.this,
                            "Are you sure you want to delete this item?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (db.deleteMenuItem(itemId)) {
                            loadMenuItems(); // Refresh the table
                            JOptionPane.showMessageDialog(frmEditMenu.this,
                                    "Item deleted successfully");
                        } else {
                            JOptionPane.showMessageDialog(frmEditMenu.this,
                                    "Failed to delete item",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frmEditMenu.this,
                            "Please select an item to delete",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void initializeTable() {
        String[] columnNames = {"ID", "Name", "Price", "Type", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 2) return Double.class;
                return String.class;
            }
        };

        tblMenuItems.setModel(tableModel);
        tblMenuItems.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblMenuItems.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblMenuItems.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblMenuItems.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblMenuItems.getColumnModel().getColumn(4).setPreferredWidth(250);
    }

    private void loadMenuItems() {
        tableModel.setRowCount(0); // Clear existing data

        List<MenuItem> items = db.getMenuItemsByRestaurantId(restaurantId);
        for (MenuItem item : items) {
            tableModel.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getPrice(),
                    (item instanceof FoodItem) ? "Food" : "Drink",
                    item.getDescription()
            });
        }
    }

    private MenuItem getMenuItemById(int id) {
        List<MenuItem> items = db.getMenuItemsByRestaurantId(restaurantId);
        for (MenuItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    private void showItemDialog(MenuItem item) {
        JDialog dialog = new JDialog(this, "Menu Item", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField(20);
        JTextField priceField = new JTextField(10);
        JTextArea descField = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(descField);

        String[] typeOptions = {"Food", "Drink"};
        JComboBox<String> typeCombo = new JComboBox<>(typeOptions);

        JTextField servingSizeField = new JTextField(10);
        JCheckBox alcoholicCheck = new JCheckBox("Alcoholic");

        JPanel typeSpecificPanel = new JPanel(new CardLayout());
        JPanel foodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        foodPanel.add(new JLabel("Serving Size:"));
        foodPanel.add(servingSizeField);

        JPanel drinkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        drinkPanel.add(alcoholicCheck);

        typeSpecificPanel.add(foodPanel, "Food");
        typeSpecificPanel.add(drinkPanel, "Drink");

        // Set initial values if editing
        if (item != null) {
            nameField.setText(item.getName());
            priceField.setText(String.valueOf(item.getPrice()));
            descField.setText(item.getDescription());

            if (item instanceof FoodItem) {
                typeCombo.setSelectedItem("Food");
                servingSizeField.setText(((FoodItem) item).getServingSize());
            } else if (item instanceof DrinkItem) {
                typeCombo.setSelectedItem("Drink");
                alcoholicCheck.setSelected(((DrinkItem) item).isAlcoholic());
            }
        }

        // Show appropriate panel based on type selection
        typeCombo.addActionListener(e -> {
            CardLayout cl = (CardLayout) (typeSpecificPanel.getLayout());
            cl.show(typeSpecificPanel, (String) typeCombo.getSelectedItem());
        });

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Type Options:"));
        panel.add(typeSpecificPanel);
        panel.add(new JLabel("Description:"));
        panel.add(descScroll);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                String type = (String) typeCombo.getSelectedItem();
                String description = descField.getText().trim();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Name cannot be empty");
                    return;
                }

                boolean success;
                if (item == null) {
                    // Adding new item
                    success = db.addMenuItem(
                            restaurantId,
                            name,
                            price,
                            type,
                            description,
                            type.equals("Food") ? servingSizeField.getText().trim() : null,
                            type.equals("Drink") ? alcoholicCheck.isSelected() : null
                    );
                } else {
                    // Updating existing item
                    success = db.updateMenuItem(
                            item.getId(),
                            name,
                            price,
                            type,
                            description,
                            type.equals("Food") ? servingSizeField.getText().trim() : null,
                            type.equals("Drink") ? alcoholicCheck.isSelected() : null
                    );
                }

                if (success) {
                    loadMenuItems();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Failed to save item",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Please enter a valid price",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set initial view based on current type selection
        CardLayout cl = (CardLayout) (typeSpecificPanel.getLayout());
        cl.show(typeSpecificPanel, (String) typeCombo.getSelectedItem());

        dialog.setVisible(true);
    }

    // Default constructor that shows error message
    public frmEditMenu() {
        JOptionPane.showMessageDialog(null,
                "No restaurant ID provided. Please login as restaurant staff.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        dispose();
    }

    public static void main(String[] args) {
        new frmEditMenu();
    }
}