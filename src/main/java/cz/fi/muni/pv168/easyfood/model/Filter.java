package cz.fi.muni.pv168.easyfood.model;

import java.util.List;

public class Filter {
    private String name;
    private List<String> ingredients;

    public static Filter createEmptyFilter() {
        return new Filter();
    }
}
