package cz.muni.fi.pv168.easyfood.business.model;

import java.util.Objects;

public class Department extends Entity {

    private String name;
    private String number;

    public Department(String guid, String name, String number) {
        super(guid);
        setName(name);
        setNumber(number);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = Objects.requireNonNull(number, "number must not be null");
    }

    @Override
    public String toString()
    {
        return "Department{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}
