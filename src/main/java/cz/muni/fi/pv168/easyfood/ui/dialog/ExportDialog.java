package cz.muni.fi.pv168.easyfood.ui.dialog;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Export;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import org.tinylog.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportDialog extends EntityDialog<Export> {
    private final Export export;
    private final List<Recipe> recipes;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;
    private static String lastPath = System.getProperty("user.dir");
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final JRadioButton xmlFormatButton = new JRadioButton("XML");
    private final JRadioButton pdfFormatButton = new JRadioButton("PDF");

    public ExportDialog(Export export, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        this.export = export;
        this.recipes = recipes;
        this.ingredients = ingredients;
        this.categories = categories;
        this.units = units;
        addFields();
        setValues();
    }

    public ExportDialog(List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        this(new Export(), recipes, ingredients, categories, units);
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

        if (pdfFormatButton.isSelected()) {
            writePDF(file);
            return null;
        }

        try(FileWriter writer = new FileWriter(file.getAbsolutePath())) {
            XmlMapper mapper = new XmlMapper();
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<EasyFood>\n");
            writeXMLBlock(mapper, "Units", writer, units);
            writeXMLBlock(mapper, "Categories", writer, categories);
            writeXMLBlock(mapper, "Ingredients", writer, ingredients);
            writeXMLBlock(mapper, "Recipes", writer, recipes);
            writer.write("</EasyFood>\n");
        } catch (IOException e) {
            Logger.error("Failed to save exported file");
        }
        return null;
    }

    private void writePDF(File file) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            for (Unit unit : units) {
                Font font = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);
                String unitText = String.format("%s (%s) is equivalent to %.2f %s", unit.getName(), unit.getAbbreviation(), unit.getConversion(), unit.getBaseUnit().toString());
                System.out.println(unitText);
                Chunk chunk = new Chunk(unitText, font);
                document.add(chunk);
            }

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Logger.error("Failed to create file");
        }
    }

    private <E> void writeXMLBlock(XmlMapper mapper, String blockName, FileWriter writer, List<E> values) throws IOException {
        writer.write("<" + blockName + ">\n");
        for (E value : values) {
            writer.write(mapper.writeValueAsString(value));
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
        return new ExportDialog(new Export(), recipes, ingredients, categories, units);
    }

    @Override
    public EntityDialog<Export> createNewDialog(Export entity, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ExportDialog(export, recipes, ingredients, categories, units);
    }

    private void addFields() {
        add("Format: ", xmlFormatButton);
        add("", pdfFormatButton);
    }

    private void setValues() {
        pdfFormatButton.setEnabled(false);
        buttonGroup.add(xmlFormatButton);
        buttonGroup.add(pdfFormatButton);
    }
}
