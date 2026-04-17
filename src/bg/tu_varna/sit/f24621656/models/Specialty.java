package bg.tu_varna.sit.f24621656.models;

import java.util.ArrayList;
import java.util.List;

public class Specialty {
    private final String name;
    private final List<Discipline> disciplines;

    public Specialty(String name) {
        this.name = name;
        this.disciplines = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public void addDiscipline(Discipline discipline) {
        disciplines.add(discipline);
    }
}
