package behaviouralpatterns.mediator.case2_UserAdmin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import java.util.List;

/**
 * Mediator Pattern - User Table Colleague
 *
 * Concrete colleague that wraps the user JTable.
 * Displays user data and notifies mediator when selection changes.
 *
 * Role: ConcreteColleague
 */
public class UserTableColleague implements UserAdminColleague {

    private final JTable userTable;
    private final DefaultTableModel tableModel;
    private UserAdminMediator mediator;

    public UserTableColleague(JTable userTable, DefaultTableModel tableModel) {
        this.userTable = userTable;
        this.tableModel = tableModel;
        setupListener();
    }

    /**
     * Sets up a ListSelectionListener to notify mediator when selection changes.
     */
    private void setupListener() {
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && mediator != null) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    int columnCount = tableModel.getColumnCount();
                    Object[] userData = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        userData[i] = tableModel.getValueAt(selectedRow, i);
                    }
                    mediator.notifyTableSelectionChanged(userData);
                }
            }
        });
    }

    @Override
    public void setMediator(UserAdminMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onMediatorEvent(String event, Object data) {
        if ("REFRESH_TABLE".equals(event)) {
            clearTable();
            if (data instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<Object[]> users = (List<Object[]>) data;
                for (Object[] user : users) {
                    tableModel.addRow(user);
                }
            }
        } else if ("CLEAR_TABLE".equals(event)) {
            clearTable();
        } else if ("REMOVE_ROW".equals(event)) {
            if (data instanceof Integer) {
                int row = (Integer) data;
                if (row >= 0 && row < tableModel.getRowCount()) {
                    tableModel.removeRow(row);
                }
            }
        }
    }

    /**
     * Clears all rows from the table.
     */
    private void clearTable() {
        tableModel.setRowCount(0);
    }

    @Override
    public Object getState() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            return tableModel.getValueAt(selectedRow, 0); // Returns user ID
        }
        return null;
    }

    /**
     * Gets the selected row index.
     */
    public int getSelectedRow() {
        return userTable.getSelectedRow();
    }

    /**
     * Gets the user table for initial setup.
     */
    public JTable getUserTable() {
        return userTable;
    }

    /**
     * Gets the table model for initial setup.
     */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
