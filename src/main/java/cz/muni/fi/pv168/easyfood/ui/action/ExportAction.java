package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.business.service.export.ExportService;
import cz.muni.fi.pv168.easyfood.util.Filter;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

/**
 * Action responsible for exporting all the employees in the employee table to a file.
 *
 * <p>This class only handles the UI-related part of the export (choosing a file)
 * and delegates the export itself to an instance of {@link ExportService},
 * which also implies the file format to be used.
 */
public final class ExportAction extends AbstractAction {

    private final Component parent;
    private final ExportService exportService;

    public ExportAction(Component parent, ExportService exportService) {
        super("Export", Icons.EXPORT_ICON);
        this.parent = Objects.requireNonNull(parent);
        this.exportService = exportService;

        putValue(SHORT_DESCRIPTION, "Exports employees to a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl X"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        exportService.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showSaveDialog(parent);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            String exportFile = fileChooser.getSelectedFile().getAbsolutePath();
            var filter = fileChooser.getFileFilter();
            if (filter instanceof Filter) {
                exportFile = ((Filter) filter).decorate(exportFile);
            }

            exportService.exportData(exportFile);

            JOptionPane.showMessageDialog(parent, "Export has successfully finished.");
        }
    }
}
