package cz.fi.muni.pv168.easyfood.ui.utility;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Util {

    public static void setCenterLocation(JFrame frame, JFrame parentFrame){
        var parentTopLeftPoint = parentFrame.getLocation();
        double x = parentTopLeftPoint.getX() + (parentFrame.getSize().getWidth() - frame.getSize().getWidth()) / 2;
        double y = parentTopLeftPoint.getY() + (parentFrame.getSize().getHeight() - frame.getSize().getHeight()) / 2;
        frame.setLocation(new Point((int)x, (int) y));
    }

    public static void setAcceptNumbersOnly (JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                field.setEditable( (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' && field.getText().length() < 9)
                        || e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                        || ((e.getKeyChar() == '.' || e.getKeyChar() == ',') && (!field.getText().contains(",") && !field.getText().contains("."))));
                field.setText(field.getText().replace(',', '.'));
            }
        });
    }

    public static MouseAdapter createDoubleClickListener(Action action) {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);
                JTable table = (JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    action.actionPerformed(new ActionEvent(mouseEvent.getSource(), mouseEvent.getID(), mouseEvent.paramString()));
                }
            }
        };
    }
}
