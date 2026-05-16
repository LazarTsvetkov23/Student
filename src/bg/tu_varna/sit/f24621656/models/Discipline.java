package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.DisciplineType;

import java.util.Objects;

/**
 * Represents an academic discipline (course).
 * In this simplified model, each discipline is taught in exactly one course (1-4).
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class Discipline {
    /** The name of the discipline (unique). */
    private final String name;

    /** The type of the discipline – MANDATORY or ELECTIVE. */
    private final DisciplineType type;

    /** Number of credits (only meaningful for ELECTIVE disciplines). */
    private int credits;

    /** The course (year) in which this discipline is taught (1-4). */
    private final int course;

    /**
     * Constructs a new discipline with the given name, type and course.
     * Credits are initially 0.
     *
     * @param name   the name of the discipline
     * @param type   the discipline type (MANDATORY or ELECTIVE)
     * @param course the course (1-4) in which the discipline is taught
     */
    public Discipline(String name, DisciplineType type, int course) {
        this.name = name;
        this.type = type;
        this.credits = 0;
        this.course = course;
    }

    /**
     * Returns the name of the discipline.
     *
     * @return discipline name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the discipline.
     *
     * @return MANDATORY or ELECTIVE
     */
    public DisciplineType getType() {
        return type;
    }

    /**
     * Returns the number of credits (for ELECTIVE disciplines).
     * For MANDATORY disciplines this is always 0.
     *
     * @return credit value
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the credits for this discipline. Only allowed for ELECTIVE disciplines.
     *
     * @param credits the new credit value
     */
    public void setCredits(int credits) {
        if (type == DisciplineType.ELECTIVE) {
            this.credits = credits;
        }
    }

    /**
     * Returns the course (year) in which this discipline is taught.
     *
     * @return course number (1-4)
     */
    public int getCourse() {
        return course;
    }

    /**
     * Compares disciplines by name.
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
        Discipline that = (Discipline) object;
        return Objects.equals(name, that.name);
    }

    /**
     * Returns hash code based on discipline name.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}