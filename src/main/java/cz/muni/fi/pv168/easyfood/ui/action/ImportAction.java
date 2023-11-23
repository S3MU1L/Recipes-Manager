package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.business.service.export.ImportService;
import cz.muni.fi.pv168.easyfood.ui.panels.EmployeeTablePanel;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;
import cz.muni.fi.pv168.easyfood.util.Filter;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

/**
 * Action responsible for importing employees from a file and adding them to the employee table.
 *
 * <p>This class only handles the UI-related part of the import (choosing a file, adding employees)
 * and delegates the import itself to an instance of {@link ImportService},
 * which also implies the file format to be used.
 */
public final class ImportAction extends AbstractAction {

    private final EmployeeTablePanel employeeTablePanel;
    private final ImportService importService;
    private final Runnable callback;

    public ImportAction(
            EmployeeTablePanel employeeTablePanel,
            ImportService importService,
            Runnable callback) {
        super("Import", Icons.IMPORT_ICON);
        this.employeeTablePanel = Objects.requireNonNull(employeeTablePanel);
        this.importService = Objects.requireNonNull(importService);
        this.callback = Objects.requireNonNull(callback);

        putValue(SHORT_DESCRIPTION, "Imports employees from a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        importService.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showOpenDialog(employeeTablePanel);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();

            importService.importData(importFile.getAbsolutePath());

            callback.run();
            JOptionPane.showMessageDialog(employeeTablePanel, "Import was done");
        }
    }
}
