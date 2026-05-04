package bg.tu_varna.sit.f24621656.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Specialty {
    private final String name;
    private final List<Discipline> disciplines;
    private int minElectiveCredits;

    public Specialty(String name) {
        this.name = name;
        this.disciplines = new ArrayList<>();
        this.minElectiveCredits = 0;
    }

    public Specialty(String name, int minElectiveCredits) {
        this.name = name;
        this.disciplines = new ArrayList<>();
        this.minElectiveCredits = minElectiveCredits;
    }

    public String getName() { return name; }
    public List<Discipline> getDisciplines() { return disciplines; }
    public int getMinElectiveCredits() { return minElectiveCredits; }
    public void setMinElectiveCredits(int minElectiveCredits) { this.minElectiveCredits = minElectiveCredits; }

    public void addDiscipline(Discipline discipline) {
        if (!disciplines.contains(discipline)) {
            disciplines.add(discipline);
        }
    }

    public Discipline findDisciplineByName(String name) {
        for (Discipline discipline : disciplines) {
            if (discipline.getName().equalsIgnoreCase(name)) {
                return discipline;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Specialty that = (Specialty) object;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}