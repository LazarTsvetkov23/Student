package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.DisciplineType;

public class Discipline {
    private final String name;
    private final DisciplineType type;

    public Discipline(String name, DisciplineType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public DisciplineType getType() {
        return type;
    }
}
