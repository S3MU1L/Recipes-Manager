package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

public class CategoryCellRenderer extends DefaultTableCellRenderer {
    private final CategoryTableModel model;
    public CategoryCellRenderer(CategoryTableModel model) {
        this.model = model;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(isSelected){
            cell.setBackground(table.getSelectionBackground());
            cell.setForeground(table.getSelectionForeground());
        }else{
            if (row >= 0) {
                cell.setBackground(model.getEntity(row).getColor());
            }
            cell.setForeground(Color.BLACK);
        }
        table.setRowHeight(30);
        return cell;
    }
}
