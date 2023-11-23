package cz.muni.fi.pv168.easyfood.ui.resources;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Icons {

    public static final Icon DELETE_ICON = createIcon("Crystal_Clear_action_button_cancel.png");
    public static final Icon EDIT_ICON = createIcon("Crystal_Clear_action_edit.png");
    public static final Icon ADD_ICON = createIcon("Crystal_Clear_action_edit_add.png");
    public static final Icon QUIT_ICON = createIcon("Crystal_Clear_action_exit.png");

    public static final Icon EXPORT_ICON = createIcon("Crystal_Clear_action_export.png");
    public static final Icon IMPORT_ICON = createIcon("Crystal_Clear_action_import.png");

    public static final Icon NUCLEAR_QUIT_ICON = createIcon("Nuclear.png");

    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    public static <E extends Enum<E>> Map<E, Icon> createEnumIcons(Class<E> clazz, int width) {
        return Stream.of(clazz.getEnumConstants())
                .collect(Collectors.toUnmodifiableMap(
                        Function.identity(),
                        e -> createIcon(clazz.getSimpleName() + "." + e.name() + "-" + width + ".png")));
    }

    private static ImageIcon createIcon(String name) {
        URL url = Icons.class.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Icon resource not found on classpath: " + name);
        }
        return new ImageIcon(url);
    }
}
