package cz.muni.fi.pv168.easyfood.ui;

import java.util.HashMap;

/**
 * Class responsible for converting plural form of a word to a singular form
 */
public class SingularConverter {
    public static final HashMap<String, String> dictionary = new HashMap<>();

    static {
        dictionary.put("recipes", "recipe");
        dictionary.put("categories", "category");
        dictionary.put("units", "unit");
        dictionary.put("ingredients", "ingredient");
    }
}