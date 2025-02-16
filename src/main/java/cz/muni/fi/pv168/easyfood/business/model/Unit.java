package cz.muni.fi.pv168.easyfood.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.DecimalFormat;
import java.util.Objects;

public class Unit extends Entity {
    private String name;
    private String abbreviation;
    private BaseUnit baseUnit;
    private double conversion;

    public Unit() {}

    public Unit(
            String guid,
            String name,
            String abbreviation,
            BaseUnit baseUnit,
            double conversion
    ) {
        super(guid);
        this.name = name;
        this.abbreviation = abbreviation;
        this.baseUnit = baseUnit;
        this.conversion = conversion;
    }

    public Unit(
            String name,
            String abbreviation,
            BaseUnit baseUnit,
            double conversion) {
        this.name = name;
        this.baseUnit = baseUnit;
        this.abbreviation = abbreviation;
        this.conversion = conversion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }


    public static Unit createEmptyUnit() {
        return new Unit("", "", BaseUnit.GRAM, 1);
    }

    public BaseUnit getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(BaseUnit baseUnit) {
        this.baseUnit = Objects.requireNonNull(baseUnit, "base unit must not be null");
    }

    public double getConversion() {
        return conversion;
    }

    public void setConversion(double conversion) {
        this.conversion = conversion;
    }

    @JsonIgnore
    public String getFormattedBaseUnit() {
        return new DecimalFormat("#.##").format(conversion) + " " + BaseUnit.getAbbreviation(baseUnit);
    }

    @JsonIgnore
    public String getFormattedConversionRatio() {
        return String.valueOf(conversion);
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = Objects.requireNonNull(abbreviation, "abbreviation must not be null");
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
