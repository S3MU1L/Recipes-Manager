package cz.fi.muni.pv168.easyfood.model;

public enum BaseUnit {
    MILLILITER("ml"),
    GRAM("g"),
    PIECE("pc.");

    private final String symbol;
    BaseUnit(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
