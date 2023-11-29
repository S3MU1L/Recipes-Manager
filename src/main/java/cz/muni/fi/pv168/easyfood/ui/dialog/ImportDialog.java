package cz.muni.fi.pv168.easyfood.ui.dialog;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Import;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

public class ImportDialog extends EntityDialog<Import> {
    private final Import importObj;
    private File backupFile = null;
    private static String lastPath = System.getProperty("user.dir");
    private final JLabel fileNameLabel = new JLabel("<none>");
    private final JButton fileSelectorButton = new JButton("Select file");

    public ImportDialog(Import importObj) {
        this.importObj = importObj;

        addFields();
        setValues();
    }

    @Override
    public Import getEntity() {
        return null;
    }

    @Override
    public boolean valid(Import entity) {
        return true;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ImportDialog(new Import());
    }

    @Override
    public EntityDialog<Import> createNewDialog(Import entity, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ImportDialog(entity);
    }

    private void addFields() {
        add("Selected: ", fileNameLabel);
        add("", fileSelectorButton);
    }

    private void setValues() {
        fileSelectorButton.addActionListener(this::openFileSelector);
    }

    private void openFileSelector(ActionEvent event) {
        JFileChooser chooser = new JFileChooser(lastPath);
        chooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        if (chooser.showOpenDialog(getPanel()) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        lastPath = file.getParent();
        fileNameLabel.setText(file.getName());
        backupFile = file;
    }
}
