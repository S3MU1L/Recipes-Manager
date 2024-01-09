package cz.muni.fi.pv168.easyfood.ui.dialog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.pv168.easyfood.business.model.BaseUnit;
import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Import;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.storage.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.IngredientDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.IngredientWithAmountDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.RecipeDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.UnitDao;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.IngredientEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.RecipeEntity;
import cz.muni.fi.pv168.easyfood.wiring.DependencyProvider;
import org.tinylog.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ImportDialog extends EntityDialog<Import> {
    private final Import importObj;
    private final DependencyProvider dependencyProvider;
    private final Repository<Recipe> recipeRepository;
    private final Repository<Ingredient> ingredientRepository;
    private final Repository<Category> categoryRepository;
    private final Repository<Unit> unitRepository;
    private final List<Recipe> recipes;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;
    private File backupFile = null;
    private static String lastPath = System.getProperty("user.dir");
    private final JLabel fileNameLabel = new JLabel("<none>");
    private final JButton fileSelectorButton = new JButton("Select file");

    public ImportDialog(Import importObj, DependencyProvider dependencyProvider, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        this.importObj = importObj;
        this.dependencyProvider = dependencyProvider;

        this.recipeRepository = dependencyProvider.getRecipeRepository();
        this.ingredientRepository = dependencyProvider.getIngredientRepository();
        this.categoryRepository = dependencyProvider.getCategoryRepository();
        this.unitRepository = dependencyProvider.getUnitRepository();

        this.recipes = recipes;
        this.ingredients = ingredients;
        this.categories = categories;
        this.units = units;

        addFields();
        setValues();
    }

    @Override
    public Import getEntity() {
        if (backupFile == null) {
            return null;
        }

        XmlMapper mapper = new XmlMapper();
        try {
            Scanner scanner = new Scanner(backupFile);
            List<String> recipeNames = recipeRepository.findAll().stream().map(Recipe::getName).toList();
            List<String> ingredientNames = ingredientRepository.findAll().stream().map(Ingredient::getName).toList();
            List<String> categoryNames = categoryRepository.findAll().stream().map(Category::getName).toList();
            Map<BaseUnit, String> unitGuid = unitRepository.findAll().stream().collect(Collectors.toMap(Unit::getBaseUnit, Unit::getGuid));

            IngredientWithAmountDao ingredientWithAmountDao = dependencyProvider.getIngredientWithAmountDao();
            RecipeDao recipeDao = dependencyProvider.getRecipeDao();
            IngredientDao ingredientDao = dependencyProvider.getIngredientDao();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("<Ingredient>")) {
                    Ingredient i = mapper.readValue(line, Ingredient.class);
                    if (!ingredientNames.contains(i.getName())) {
                        i.getUnit().setGuid(unitGuid.get(i.getUnit().getBaseUnit()));
                        ingredientRepository.create(i);
                        ingredients.add(i);
                    }
                } else if (line.startsWith("<Recipe>")) {
                    Recipe r = mapper.readValue(line.replace("\\n", "\n"), Recipe.class);
                    if (!recipeNames.contains(r.getName())) {
                        recipeRepository.create(r);
                        recipes.add(r);
                        RecipeEntity recipeEntity = recipeDao.findByGuid(r.getGuid()).get();
                        for (IngredientWithAmount ingredientWithAmount : r.getIngredients()) {
                            IngredientEntity ingredientEntity = ingredientDao.findByGuid(ingredientWithAmount.getIngredient().getGuid()).get();
                            ingredientWithAmountDao.addRecipeIngredient(ingredientWithAmount, recipeEntity.id(), ingredientEntity.id());
                        }
                    }
                }
            }
        } catch (JsonMappingException e) {
            Logger.error("Failed to map XML");
        } catch (JsonProcessingException e) {
            Logger.error("Failed to process XML");
        } catch (IOException e) {
            Logger.error("Failed to read file");
        }

        return null;
    }

    @Override
    public boolean valid(Import entity) {
        return true;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ImportDialog(new Import(), dependencyProvider, recipes, ingredients, categories, units);
    }

    @Override
    public EntityDialog<Import> createNewDialog(Import entity, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ImportDialog(entity, dependencyProvider, recipes, ingredients, categories, units);
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
