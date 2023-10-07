package cz.fi.muni.pv168.dietaryAssistantApp.model;

public enum Unit {
    MILLILITER("ml"),
    GRAM("g"),
    PIECE("pc.");

    private final String symbol;
    Unit(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
