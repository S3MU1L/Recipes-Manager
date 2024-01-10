package cz.muni.fi.pv168.easyfood.ui.dialog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.pv168.easyfood.business.model.BaseUnit;
import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Import;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.IngredientDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.IngredientWithAmountDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.RecipeDao;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.IngredientEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.RecipeEntity;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.IngredientTableModel;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.RecipeTableModel;
import cz.muni.fi.pv168.easyfood.wiring.DependencyProvider;
import org.tinylog.Logger;
import org.xml.sax.SAXException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

public class ImportDialog extends EntityDialog<Import> {
    private final DependencyProvider dependencyProvider;
    private final Repository<Recipe> recipeRepository;
    private final Repository<Ingredient> ingredientRepository;
    private final Repository<Category> categoryRepository;
    private final Repository<Unit> unitRepository;
    private final RecipeTableModel recipeTableModel;
    private final IngredientTableModel ingredientTableModel;
    private File backupFile = null;
    private static String lastPath = System.getProperty("user.dir");
    private final JLabel fileNameLabel = new JLabel("<none>");
    private final JButton fileSelectorButton = new JButton("Select file");

    public ImportDialog(
            DependencyProvider dependencyProvider,
            RecipeTableModel recipeTableModel,
            IngredientTableModel ingredientTableModel) {
        this.dependencyProvider = dependencyProvider;

        this.recipeRepository = dependencyProvider.getRecipeRepository();
        this.ingredientRepository = dependencyProvider.getIngredientRepository();
        this.categoryRepository = dependencyProvider.getCategoryRepository();
        this.unitRepository = dependencyProvider.getUnitRepository();

        this.recipeTableModel = recipeTableModel;
        this.ingredientTableModel = ingredientTableModel;

        addFields();
        setValues();
    }

    @Override
    public Import getEntity() {
        if (backupFile == null) {
            return null;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // `.parse()` validates XML, return is not needed
            builder.parse(backupFile);
        } catch (ParserConfigurationException e) {
            Logger.error("XML parser configuration error");
            // Just continue without validation
        } catch (SAXException e) {
            Logger.error("Invalid XML format");
            JOptionPane.showMessageDialog(null, "The selected file is not valid XML.", "XML Error", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (IOException e) {
            Logger.error("Couldn't open backup file for parsing");
            JOptionPane.showMessageDialog(null, "Failed to open XML file for parsing, make sure you have the permissions to do so.", "File Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        XmlMapper mapper = new XmlMapper();

        new Thread(() -> {
            threadImport(mapper);
            JOptionPane.showMessageDialog(null, "Import complete", "Notice", JOptionPane.INFORMATION_MESSAGE);
        }).start();

        return null;
    }

    private void threadImport(XmlMapper mapper) {
        try {
            Scanner scanner = new Scanner(backupFile);
            Map<String, Recipe> recipeNames = recipeRepository.findAll().stream().collect(Collectors.toMap(Recipe::getName, r -> r));
            Map<String, Ingredient> ingredientNames = ingredientRepository.findAll().stream().collect(Collectors.toMap(Ingredient::getName, i -> i));
            var importedCategoryOptional = categoryRepository.findAll().stream().filter(c -> c.getName().equals("Imported")).findFirst();
            Map<BaseUnit, String> unitGuid = unitRepository.findAll().stream().filter(u -> u.getName().equals(u.getBaseUnit().toString())).collect(Collectors.toMap(Unit::getBaseUnit, Unit::getGuid));

            IngredientWithAmountDao ingredientWithAmountDao = dependencyProvider.getIngredientWithAmountDao();
            RecipeDao recipeDao = dependencyProvider.getRecipeDao();
            IngredientDao ingredientDao = dependencyProvider.getIngredientDao();

            Category importedCategory = importedCategoryOptional
                    .orElseGet(
                            () -> new Category(UUID.randomUUID().toString(), "", Color.WHITE)
                    );
            if (importedCategoryOptional.isEmpty()) categoryRepository.create(importedCategory);
            Map<String, Ingredient> importedIngredients = new HashMap<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("<Ingredient>")) {
                    Ingredient i = mapper.readValue(line, Ingredient.class);
                    i.getUnit().setGuid(unitGuid.get(i.getUnit().getBaseUnit()));
                    if (ingredientNames.containsKey(i.getName())) {
                        int decision = JOptionPane.showConfirmDialog(null, "Ingredient " + i.getName() + " already exists. Would you like to overwrite it?");
                        switch (decision) {
                            case 0:
                                Ingredient oldIngredient = ingredientRepository.findByName(i.getName()).get();
                                i.setGuid(oldIngredient.getGuid());
                                ingredientRepository.update(i);
                            case 1:
                                continue;
                            default:
                                return;
                        }
                    }
                    ingredientRepository.create(i);
                    importedIngredients.put(i.getName(), i);
                } else if (line.startsWith("<Recipe>")) {
                    Recipe r = mapper.readValue(line.replace("\\n", "\n"), Recipe.class);
                    r.setCategory(importedCategory);
                    if (recipeNames.containsKey(r.getName())) {
                        int decision = JOptionPane.showConfirmDialog(null, "Recipe " + r.getName() + " already exists. Would you like to overwrite it?");
                        switch (decision) {
                            case 0:
                                Recipe oldRecipe = recipeRepository.findByName(r.getName()).get();
                                r.setGuid(oldRecipe.getGuid());
                                recipeRepository.update(r);
                            case 1:
                                continue;
                            default:
                                return;
                        }
                    }
                    recipeRepository.create(r);
                    RecipeEntity recipeEntity = recipeDao.findByGuid(r.getGuid()).get();
                    for (IngredientWithAmount ingredientWithAmount : r.getIngredients()) {
                        ingredientWithAmount.setIngredient(importedIngredients.get(ingredientWithAmount.getIngredient().getName()));
                        IngredientEntity ingredientEntity = ingredientDao.findByGuid(ingredientWithAmount.getIngredient().getGuid()).get();
                        ingredientWithAmountDao.addRecipeIngredient(ingredientWithAmount, recipeEntity.id(), ingredientEntity.id());
                    }
                }
            }
        } catch (JsonProcessingException e) {
            Logger.error("Failed to process XML");
            JOptionPane.showMessageDialog(null, "An error occurred while reading the file, not everything could be imported", "Processing Error", JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException e) {
            Logger.error("Failed to read file");
            JOptionPane.showMessageDialog(null, "An error occurred while opening the selected file, make sure you have the permissions to do so", "File Error", JOptionPane.ERROR_MESSAGE);
        }

        recipeTableModel.updateRecipes();
        ingredientTableModel.updateIngredients();
    }

    @Override
    public boolean valid(Import entity) {
        return true;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ImportDialog(dependencyProvider, recipeTableModel, ingredientTableModel);
    }

    @Override
    public EntityDialog<Import> createNewDialog(Import entity, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ImportDialog(dependencyProvider, recipeTableModel, ingredientTableModel);
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
