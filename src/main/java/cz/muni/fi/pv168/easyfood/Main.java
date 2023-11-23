package cz.muni.fi.pv168.easyfood;

import cz.muni.fi.pv168.easyfood.ui.ApplicationErrorHandler;
import cz.muni.fi.pv168.easyfood.ui.MainWindow;
import cz.muni.fi.pv168.easyfood.ui.action.NuclearQuitAction;
import cz.muni.fi.pv168.easyfood.ui.action.QuitAction;
import cz.muni.fi.pv168.easyfood.wiring.ProductionDependencyProvider;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        var errorHandler = new ApplicationErrorHandler();
        Thread.setDefaultUncaughtExceptionHandler(errorHandler);

        initNimbusLookAndFeel();

        EventQueue.invokeLater(() -> {
            try {
                new MainWindow(new ProductionDependencyProvider()).show();
            } catch (Exception ex) {
                showInitializationFailedDialog(ex);
            }
        });
    }

    private static void initNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Nimbus layout initialization failed", ex);
        }
    }

    private static void showInitializationFailedDialog(Exception ex) {
        EventQueue.invokeLater(() -> {
            ex.printStackTrace();
            Object[] options = {
                    new JButton(new QuitAction()),
                    new JButton(new NuclearQuitAction())
            };
            JOptionPane.showOptionDialog(
                    null,
                    "Application initialization failed.\nWhat do you want to do?",
                    "Initialization Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    options,
                    options[0]
            );
        });
    }
}
