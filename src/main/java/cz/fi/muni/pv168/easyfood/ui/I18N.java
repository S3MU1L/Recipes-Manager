package cz.fi.muni.pv168.easyfood.ui;

import java.util.ResourceBundle;

public final class I18N {

    private final ResourceBundle bundle;
    private final String prefix;

    public I18N(Class<?> clazz) {
        bundle = ResourceBundle.getBundle("cz/fi/muni/pv168/easyfood/ui/i18n");
        prefix = clazz.getSimpleName() + ".";
    }

    public String getString(String key) {
        return bundle.getString(prefix + key);
    }

    public <E extends Enum<E>> String getString(E key) {
        return bundle.getString(key.getClass().getSimpleName() + "." + key.name());
    }
}