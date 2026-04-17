package bg.tu_varna.sit.f24621656.models;

import java.util.ArrayList;
import java.util.List;

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
}
