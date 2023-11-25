package cz.muni.fi.pv168.easyfood.ui.resources;

import cz.muni.fi.pv168.easyfood.ui.action.AddAction;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Icons {

    public static final Icon DELETE_ICON = createIcon("cancel.png");
    public static final Icon EDIT_ICON = createIcon("edit.png");
    public static final Icon ADD_ICON = createIcon("add.png");
    public static final Icon QUIT_ICON = createIcon("exit.png");
    public static final Icon SHOW_ICON = createIcon("show.png");
    public static final Icon FILTER_ICON = createIcon("filter.png");
    public static final Icon IMPORT_ICON = createIcon("import.png");
    public static final Icon EXPORT_ICON = createIcon("export.png");
    public static final Icon NUCLEAR_QUIT_ICON = createIcon("nuclear.png");
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
        ImageIcon imageIcon = new ImageIcon(Icons.class.getResource(name));
        Image image = imageIcon.getImage();
        return new ImageIcon(image.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, java.awt.Image.SCALE_SMOOTH));
    }
}
