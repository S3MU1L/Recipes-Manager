package cz.fi.muni.pv168.easyfood.model;

public class Unit {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private BaseUnit baseUnit;
    private double conversion;


    public Unit(String name, BaseUnit baseUnit, double conversion) {
        this.name = name;
        this.baseUnit = baseUnit;
        this.conversion = conversion;
    }

    public static Unit createEmptyUnit(){
        return new Unit("", BaseUnit.GRAM, 1);
    }

    public BaseUnit getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(BaseUnit baseUnit) {
        this.baseUnit = baseUnit;
    }

    public double getConversion() {
        return conversion;
    }

    public void setConversion(double conversion) {
        this.conversion = conversion;
    }

    public String getFormattedBaseUnit(){
        return baseUnit.toString();
    }

    public String getFormattedConversionRatio(){
        return String.valueOf(conversion);
    }
}
