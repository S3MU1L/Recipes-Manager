package cz.fi.muni.pv168.easyfood.ui.windows;

import javax.swing.*;
import java.awt.*;

public class InfoWindow extends AbstractWindow {

    public InfoWindow(String text, JFrame parentFrame) {
        super("Info", parentFrame);
        frame.setMinimumSize(new Dimension(200, 50));

        JLabel textLabel = new JLabel(text);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textLabel, BorderLayout.CENTER);

        JButton button = new JButton("OK");
        button.addActionListener(e -> frame.dispose());
        mainPanel.add(button, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.pack();
    }
}
