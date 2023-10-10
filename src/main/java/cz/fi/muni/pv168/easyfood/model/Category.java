package cz.fi.muni.pv168.easyfood.model;

/**
 * @author Tibor Pelegrin
 */
public class Category {
    private String name;
    public Category(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
