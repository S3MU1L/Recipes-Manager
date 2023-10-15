package cz.fi.muni.pv168.easyfood.model;

public enum Unit {
    MILLILITER("ml"),
    GRAM("g"),
    PIECE("pc."),
    EMPTY_UNIT("");

    private final String symbol;
    Unit(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
