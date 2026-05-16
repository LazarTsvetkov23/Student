package bg.tu_varna.sit.f24621656.models;

/**
 * Represents a grade obtained by a student in a specific discipline.
 * Contains the discipline and the numeric grade value.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class Grade {
    /** The discipline for which the grade is given. */
    private final Discipline discipline;

    /** The numeric grade value (2.00 – 6.00). */
    private final double value;

    /**
     * Constructs a new grade.
     *
     * @param discipline the discipline to which the grade belongs
     * @param value      the numeric grade (2.00 – 6.00)
     */
    public Grade(Discipline discipline, double value) {
        this.discipline = discipline;
        this.value = value;
    }

    /**
     * Returns the discipline of this grade.
     *
     * @return the discipline
     */
    public Discipline getDiscipline() {
        return discipline;
    }

    /**
     * Returns the numeric grade value.
     *
     * @return grade value
     */
    public double getValue() {
        return value;
    }

    /**
     * Checks whether the grade is considered passed (>= 3.00).
     *
     * @return true if grade >= 3.00, false otherwise
     */
    public boolean isPassed() {
        return value >= 3.00;
    }
}