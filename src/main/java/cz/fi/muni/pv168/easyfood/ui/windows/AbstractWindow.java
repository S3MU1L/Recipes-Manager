package cz.fi.muni.pv168.easyfood.ui.windows;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.utility.Util;

import javax.swing.*;
import java.awt.event.WindowListener;

public abstract class AbstractWindow {
    protected JFrame frame;
    protected JFrame parentFrame;

    public AbstractWindow(String title, JFrame parentFrame) {
        this.frame = new JFrame(title);
        this.parentFrame = parentFrame;
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void showWindow() {
        focus();
        show();
    }

    public void show() {
        frame.setVisible(true);
    }

    public void addWindowListener(WindowListener listener) {
        frame.addWindowListener(listener);
    }

    public void focus() {
        if (parentFrame != null) {
            Util.setCenterLocation(frame, parentFrame);
        }
        frame.toFront();
    }

    public void close() {
        frame.dispose();
    }

    public void onAddedIngredient() {}
    public void onAddedRecipe() {}
    public void onAddedRecipeIngredient(Recipe recipe) {}

    public void onDeletedIngredient(Ingredient ingredient, int index) {}
    public void onDeletedRecipe(Recipe recipe, int index) {}
    public void onDeletedRecipeIngredient(Recipe recipe, Ingredient ingredient, int index) {}

    public void onUpdatedRecipe(Recipe recipe, int index) {}

    public void onUpdateAll() {}
}
