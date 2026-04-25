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

    public String getName() {
        return name;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public int getMinElectiveCredits() {
        return minElectiveCredits;
    }

    public void setMinElectiveCredits(int minElectiveCredits) {
        this.minElectiveCredits = minElectiveCredits;
    }

    public void addDiscipline(Discipline discipline) {
        disciplines.add(discipline);
    }

    public Discipline findDisciplineByName(String name) {
        for(Discipline discipline : disciplines) {
            if(discipline.getName().equalsIgnoreCase(name)) {
                return discipline;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Specialty that = (Specialty) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
