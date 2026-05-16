package bg.tu_varna.sit.f24621656.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a specialty (program of study) in the university.
 * Contains a name, a list of disciplines offered, and a minimum required number of elective credits.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class Specialty {
    /** The name of the specialty (unique). */
    private final String name;

    /** List of disciplines that belong to this specialty. */
    private final List<Discipline> disciplines;

    /** Minimum number of elective credits required for graduation. */
    private final int minElectiveCredits;

    /**
     * Constructs a specialty with a given name and default minimum credits (0).
     *
     * @param name the name of the specialty
     */
    public Specialty(String name) {
        this.name = name;
        this.disciplines = new ArrayList<>();
        this.minElectiveCredits = 0;
    }

    /**
     * Constructs a specialty with a given name and minimum elective credits.
     *
     * @param name              the name of the specialty
     * @param minElectiveCredits the minimum required elective credits
     */
    public Specialty(String name, int minElectiveCredits) {
        this.name = name;
        this.disciplines = new ArrayList<>();
        this.minElectiveCredits = minElectiveCredits;
    }

    /**
     * Returns the name of the specialty.
     *
     * @return specialty name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of disciplines offered in this specialty.
     *
     * @return list of disciplines
     */
    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    /**
     * Returns the minimum number of elective credits required for graduation.
     *
     * @return minimum elective credits
     */
    public int getMinElectiveCredits() {
        return minElectiveCredits;
    }

    /**
     * Adds a discipline to this specialty if it is not already present.
     *
     * @param discipline the discipline to add
     */
    public void addDiscipline(Discipline discipline) {
        if (discipline == null) {
            return;
        }
        if (!disciplines.contains(discipline)) {
            disciplines.add(discipline);
        }
    }

    /**
     * Compares specialties by name.
     *
     * @param object the object to compare with
     * @return true if names are equal, false otherwise
     */
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

    /**
     * Returns hash code based on specialty name.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}