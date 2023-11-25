package cz.muni.fi.pv168.easyfood.ui.renderers;

import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.EntityTableModel;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

public class CustomTableCellRenderer<E> extends DefaultTableCellRenderer {
    private final EntityTableModel<E> model;

    public CustomTableCellRenderer(EntityTableModel<E> model) {
        this.model = model;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            cell.setBackground(table.getSelectionBackground());
            cell.setForeground(table.getSelectionForeground());
        } else {
            cell.setBackground(Color.WHITE);
            cell.setForeground(Color.BLACK);
            if (row >= 0) {
                //general settings for every row

                //custom settings for every row
                model.customizeTableCell(cell, value, row, table);
            }

        }
        //general settings for table
        table.setRowHeight(30);

        //custom setting for table for every tableModel
        model.customizeTable(table);
        return cell;
    }
}
