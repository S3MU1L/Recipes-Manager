package cz.muni.fi.pv168.easyfood.ui.dialog;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import cz.muni.fi.pv168.easyfood.business.model.BaseUnit;
import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Export;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.wiring.DependencyProvider;
import org.tinylog.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExportDialog extends EntityDialog<Export> {
    private final Export export;
    private final DependencyProvider dependencyProvider;
    private final Repository<Recipe> recipeRepository;
    private final Repository<Ingredient> ingredientRepository;
    private final Repository<Category> categoryRepository;
    private final Repository<Unit> unitRepository;
    private static String lastPath = System.getProperty("user.dir");
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final JRadioButton xmlFormatButton = new JRadioButton("XML");
    private final JRadioButton pdfFormatButton = new JRadioButton("PDF");

    public ExportDialog(Export export, DependencyProvider dependencyProvider) {
        this.export = export;
        this.dependencyProvider = dependencyProvider;
        this.recipeRepository = dependencyProvider.getRecipeRepository();
        this.ingredientRepository = dependencyProvider.getIngredientRepository();
        this.categoryRepository = dependencyProvider.getCategoryRepository();
        this.unitRepository = dependencyProvider.getUnitRepository();

        addFields();
        setValues();
    }

    public ExportDialog(DependencyProvider dependencyProvider) {
        this(new Export(), dependencyProvider);
    }

    @Override
    public Export getEntity() {
        JFileChooser chooser = new JFileChooser(lastPath);
        String format;
        if (xmlFormatButton.isSelected()) {
            format = "xml";
        } else if (pdfFormatButton.isSelected()) {
            format = "pdf";
        } else {
            return null;
        }

        chooser.setFileFilter(new FileNameExtensionFilter(format.toUpperCase(), format));
        if (chooser.showSaveDialog(getPanel()) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File file = chooser.getSelectedFile();
        lastPath = file.getParent();
        if (!file.getName().endsWith("." + format)) {
            file = new File(file.getAbsolutePath() + "." + format);
        }

        List<Recipe> recipes = recipeRepository.findAll().stream()
                .map(this::getBaseUnitRecipe)
                .collect(Collectors.toList());
        List<Ingredient> ingredients = ingredientRepository.findAll().stream()
                .map(this::getBaseUnitIngredient)
                .collect(Collectors.toList());

        if (pdfFormatButton.isSelected()) {
            writePDF(file, recipes, ingredients);
            return null;
        }

        try(FileWriter writer = new FileWriter(file.getAbsolutePath())) {
            XmlMapper mapper = new XmlMapper();
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<EasyFood>\n");
            writeXMLBlock(mapper, "Ingredients", writer, ingredients);
            writeXMLBlock(mapper, "Recipes", writer, recipes);
            writer.write("</EasyFood>\n");
        } catch (IOException e) {
            Logger.error("Failed to save exported file");
            JOptionPane.showMessageDialog(null, "An error occurred while trying to write the selected file, make sure you have the permissions to do so", "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private Recipe getBaseUnitRecipe(Recipe recipe) {
        recipe = new Recipe(recipe);
        List<IngredientWithAmount> ingredients = new ArrayList<>();
        for (IngredientWithAmount ingredientWithAmount : recipe.getIngredients()) {
            Ingredient ingredient = ingredientWithAmount.getIngredient();
            BaseUnit baseUnit = ingredient.getUnit().getBaseUnit();
            double conversion = ingredient.getUnit().getConversion();
            ingredients.add(new IngredientWithAmount(
                    ingredientWithAmount.getGuid(),
                    ingredientWithAmount.getName(),
                    ingredient.getCalories(),
                    new Unit("", "", baseUnit, 1),
                    ingredientWithAmount.getAmount() * conversion
            ));
        }
        recipe.setIngredients(ingredients);
        return recipe;
    }

    private Ingredient getBaseUnitIngredient(Ingredient ingredient) {
        ingredient = new Ingredient(ingredient);
        ingredient.setUnit(new Unit("", "", ingredient.getUnit().getBaseUnit(), 1));
        return ingredient;
    }

    private void writePDF(File file, List<Recipe> recipes, List<Ingredient> ingredients) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            Chapter chapter = new Chapter(new Paragraph(new Anchor("Ingredients")), 1);
            chapter.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);
            writeHeader(table, "Name", "Calories", "Unit");
            for (Ingredient ingredient : ingredients) {
                table.addCell(ingredient.getName());
                table.addCell(String.valueOf(ingredient.getCalories()));
                table.addCell(ingredient.getUnit().getBaseUnit().toString());
            }
            chapter.add(table);
            document.add(chapter);

            chapter = new Chapter(new Paragraph(new Anchor("Recipes")), 2);
            chapter.add(new Paragraph(" "));

            table = new PdfPTable(5);
            writeHeader(table, "Name", "Ingredients", "Description", "Preparation Time", "Portions");
            for (Recipe recipe : recipes) {
                table.addCell(recipe.getName());
                table.addCell(
                        String.join("\n",
                            recipe.getIngredients().stream()
                                    .map(
                                            i -> String.format(i.getFormattedAmount() + " " + i.getIngredient().getName())
                                    )
                                    .toList()
                        )
                );
                table.addCell(recipe.getDescription());
                table.addCell(recipe.getFormattedPreparationTime());
                table.addCell(String.valueOf(recipe.getPortions()));
            }
            chapter.add(table);
            document.add(chapter);

            document.close();
        } catch (DocumentException e) {
            Logger.error("Failed to write PDF");
            JOptionPane.showMessageDialog(null, "An error occurred while trying to write into the PDF file", "PDF Error", JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException e) {
            Logger.error("Failed to create file");
            JOptionPane.showMessageDialog(null, "An error occurred while trying to write the selected file, make sure you have the permissions to do so", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeHeader(PdfPTable table, String... titles) {
        Arrays.stream(titles)
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(title));
                    table.addCell(header);
                });
    }

    private <E> void writeXMLBlock(XmlMapper mapper, String blockName, FileWriter writer, List<E> values) throws IOException {
        writer.write("<" + blockName + ">\n");
        for (E value : values) {
            writer.write(mapper.writeValueAsString(value).replace("\n", "\\n"));
            writer.write('\n');
        }
        writer.write("</" + blockName + ">\n");
    }

    @Override
    public boolean valid(Export entity) {
        return true;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ExportDialog(new Export(), dependencyProvider);
    }

    @Override
    public EntityDialog<Export> createNewDialog(Export entity, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ExportDialog(export, dependencyProvider);
    }

    private void addFields() {
        add("Format: ", xmlFormatButton);
        add("", pdfFormatButton);
    }

    private void setValues() {
        buttonGroup.add(xmlFormatButton);
        buttonGroup.add(pdfFormatButton);
    }
}
