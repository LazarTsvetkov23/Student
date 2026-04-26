package bg.tu_varna.sit.f24621656.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Клас, представящ специалност.
 *
 * <p>Всяка специалност има име, списък от дисциплини
 * и минимален брой кредити от избираеми дисциплини за дипломиране.
 *
 * @author Student
 * @version 1.0
 */
public class Specialty {
    private final String name;
    private final List<Discipline> disciplines;
    private int minElectiveCredits;

    /**
     * Конструктор за специалност без минимални кредити.
     *
     * @param name име на специалността
     */
    public Specialty(String name) {
        this.name = name;
        this.disciplines = new ArrayList<>();
        this.minElectiveCredits = 0;
    }

    /**
     * Конструктор за специалност с минимални кредити.
     *
     * @param name име на специалността
     * @param minElectiveCredits минимален брой кредити от избираеми дисциплини
     */
    public Specialty(String name, int minElectiveCredits) {
        this.name = name;
        this.disciplines = new ArrayList<>();
        this.minElectiveCredits = minElectiveCredits;
    }

    /** @return името на специалността */
    public String getName() {
        return name;
    }

    /** @return списък с дисциплините за специалността */
    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    /** @return минималния брой кредити от избираеми дисциплини */
    public int getMinElectiveCredits() {
        return minElectiveCredits;
    }

    /**
     * Задава минималния брой кредити от избираеми дисциплини.
     *
     * @param minElectiveCredits нов минимален брой кредити
     */
    public void setMinElectiveCredits(int minElectiveCredits) {
        this.minElectiveCredits = minElectiveCredits;
    }

    /**
     * Добавя дисциплина към специалността.
     *
     * @param discipline дисциплината за добавяне
     */
    public void addDiscipline(Discipline discipline) {
        if (!disciplines.contains(discipline)) {
            disciplines.add(discipline);
        }
    }

    /**
     * Търси дисциплина по име.
     *
     * @param name името на дисциплината
     * @return дисциплината или null ако не е намерена
     */
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