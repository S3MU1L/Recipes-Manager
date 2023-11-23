package cz.fi.muni.pv168.easyfood.bussiness.model;

public enum BaseUnit {
    MILLILITER("Milliliter", "ml"),
    GRAM("Gram", "g"),
    PIECE("Piece", "pc");

    private final String symbol;
    private final String abbreviation;

    BaseUnit(String symbol, String abbreviation) {
        this.symbol = symbol;
        this.abbreviation = abbreviation;
    }

    public static String getAbbreviation(BaseUnit baseUnit) {
        return baseUnit.abbreviation;
    }


    @Override
    public String toString() {
        return symbol;
    }
}
