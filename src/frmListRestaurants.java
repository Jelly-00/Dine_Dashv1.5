import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmListRestaurants {
    private JPanel panel1;
    private JTable tblListRes;
    private JButton btnViewDetails;

    public void loadRestaurantData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Cuisine Type"});

    }
}