package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.ui.model.EmployeeTableModel;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public final class DeleteAction extends AbstractAction {

    private final JTable employeeTable;

    public DeleteAction(JTable employeeTable) {
        super("Delete", Icons.DELETE_ICON);
        this.employeeTable = employeeTable;
        putValue(SHORT_DESCRIPTION, "Deletes selected employees");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
        Arrays.stream(employeeTable.getSelectedRows())
                // view row index must be converted to model row index
                .map(employeeTable::convertRowIndexToModel)
                .boxed()
                // We need to delete rows in descending order to not change index of rows
                // which are not deleted yet
                .sorted(Comparator.reverseOrder())
                .forEach(employeeTableModel::deleteRow);
    }
}
