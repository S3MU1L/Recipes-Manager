package cz.fi.muni.pv168.easyfood.ui;

import cz.fi.muni.pv168.easyfood.ui.action.AddIngredientAction;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Icons {

    public static final Icon DELETE_ICON = createIcon("icons/cancel.png");
    public static final Icon EDIT_ICON = createIcon("icons/edit.png");
    public static final Icon ADD_INGREDIENT_ICON = createIcon("icons/add_ingredient.png");
    public static final Icon ADD_RECIPE_ICON = createIcon("icons/add_recipe.png");
    public static final Icon QUIT_ICON = createIcon("icons/exit.png");
    public static final Icon SHOW_ICON = createIcon("icons/show.png");

    public static final int ICON_WIDTH = 30;
    public static final int ICON_HEIGHT = 30;

    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    static <E extends Enum<E>> Map<E, Icon> createEnumIcons(Class<E> clazz, int width) {
        return Stream.of(clazz.getEnumConstants())
                .collect(Collectors.toUnmodifiableMap(
                        e -> e,
                        e -> createIcon(clazz.getSimpleName() + "." + e.name() + "-" + width + ".png")));
    }

    private static ImageIcon createIcon(String name) {
        ImageIcon imageIcon = new ImageIcon(AddIngredientAction.class.getResource(name));
        Image image = imageIcon.getImage();
        return new ImageIcon(image.getScaledInstance(ICON_WIDTH, ICON_HEIGHT,  java.awt.Image.SCALE_SMOOTH));
    }
}
