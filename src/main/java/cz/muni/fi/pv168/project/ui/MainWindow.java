package cz.muni.fi.pv168.project.ui;


import javax.swing.*;

public class MainWindow {

    private final JFrame frame;


    public MainWindow() {
        frame = createFrame();
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame("Easy food");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }
}
