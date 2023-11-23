package cz.muni.fi.pv168.easyfood.business.model;

public enum BaseUnit {
    MILLILITER("Milliliter"),
    GRAM("Gram"),
    PIECE("Piece");

    private final String symbol;

    BaseUnit(String symbol) {
        this.symbol = symbol;
    }

    public static BaseUnit getBaseUnitFormSymbol(String symbol) {
        switch (symbol) {
            case "Milliliter" -> {
                return MILLILITER;
            }
            case "Gram" -> {
                return GRAM;
            }
            case "Piece" -> {
                return PIECE;
            }
            default -> throw new RuntimeException("Unexpected symbol");
        }
    }

    public static String getAbbreviation(BaseUnit baseUnit) {
        switch (baseUnit) {
            case MILLILITER -> {
                return "ml";
            }
            case GRAM -> {
                return "g";
            }
            case PIECE -> {
                return "pc";
            }
            default -> throw new RuntimeException("Unexpected symbol");
        }
    }

    @Override
    public String toString() {
        return symbol;
    }
}
