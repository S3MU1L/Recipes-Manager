package cz.fi.muni.pv168.easyfood;

import cz.fi.muni.pv168.easyfood.data.IngredientDao;
import cz.fi.muni.pv168.easyfood.data.RecipeDao;
import cz.fi.muni.pv168.easyfood.data.TestDataGenerator;
import cz.fi.muni.pv168.easyfood.service.IngredientService;
import cz.fi.muni.pv168.easyfood.service.RecipeService;
import cz.fi.muni.pv168.easyfood.ui.windows.MainWindow;
import org.apache.derby.jdbc.EmbeddedDataSource;
import cz.fi.muni.pv168.easyfood.ui.windows.WindowsManager;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static boolean debug = false;

    public static boolean isDebug() {
        return debug;
    }

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("DEBUG")) {
                debug = true;
                break;
            }
        }
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        var dataSource = createDatasource();
        IngredientDao ingredientDao = new IngredientDao(dataSource, testDataGenerator);
        RecipeDao recipeDao = new RecipeDao(dataSource, ingredientDao, testDataGenerator);
        initNimbusLookAndFeel();
        var windowsManager = new WindowsManager();
        var recipeService = new RecipeService(recipeDao, windowsManager);
        var ingredientService = new IngredientService(ingredientDao, windowsManager, recipeService);
        windowsManager.setIngredientService(ingredientService);
        windowsManager.setRecipeService(recipeService);
        EventQueue.invokeLater(() -> windowsManager.openWindow(
                new MainWindow(recipeService, ingredientService)));
    }

    private static DataSource createDatasource() {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        String dbPath = System.getProperty("user.home") + "/dietary-assistant-pv168";
        dataSource.setDatabaseName(dbPath);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }

    private static void initNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "Nimbus layout initialization failed", ex);
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex2) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "CrossPlatform layout initialization failed", ex2);
            }
        }
    }
}
