package cz.muni.fi.pv168.easyfood.business.model;

public class Unit extends Entity {
    private String name;
    private String abbreviation;
    private BaseUnit baseUnit;
    private double conversion;

    public Unit(
            String guid,
            String name,
            String abbreviation,
            double conversion
    ) {
        super(guid);
        setName(name);
        setAbbreviation(abbreviation);
        setConversion(conversion);
    }

    public Unit(
            String name,
            String abbreviation,
            double conversion
    ) {
        setName(name);
        setAbbreviation(abbreviation);
        setConversion(conversion);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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

    @Override
    public String toString() {
        return "Unit{" +
                "name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", baseUnit=" + baseUnit +
                ", conversion=" + conversion +
                '}';
    }
}
