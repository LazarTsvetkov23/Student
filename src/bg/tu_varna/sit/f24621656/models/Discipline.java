package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.DisciplineType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Клас, представящ учебна дисциплина.
 *
 * <p>Всяка дисциплина има име, тип (задължителна/избираема),
 * кредити (само за избираеми) и списък от курсове, в които може да се записва.
 *
 * @author Student
 * @version 1.0
 */
public class Discipline {
    private final String name;
    private final DisciplineType type;
    private int credits;
    private final List<Integer> availableCourses;

    /**
     * Конструктор за дисциплина.
     *
     * @param name име на дисциплината
     * @param type тип на дисциплината (MANDATORY или ELECTIVE)
     */
    public Discipline(String name, DisciplineType type) {
        this.name = name;
        this.type = type;
        this.credits = 0;
        this.availableCourses = new ArrayList<>();
    }

    /** @return името на дисциплината */
    public String getName() {
        return name;
    }

    /** @return типа на дисциплината */
    public DisciplineType getType() {
        return type;
    }

    /** @return броя кредити (за избираеми дисциплини) */
    public int getCredits() {
        return credits;
    }

    /**
     * Задава броя кредити (само за избираеми дисциплини).
     *
     * @param credits нов брой кредити
     */
    public void setCredits(int credits) {
        if (type == DisciplineType.ELECTIVE) {
            this.credits = credits;
        }
    }

    /** @return списък с курсовете, в които може да се записва дисциплината */
    public List<Integer> getAvailableCourses() {
        return availableCourses;
    }

    /**
     * Добавя курс, в който дисциплината може да се записва.
     *
     * @param course номер на курс (1-4)
     */
    public void addAvailableCourse(int course) {
        if (!availableCourses.contains(course)) {
            availableCourses.add(course);
        }
    }

    /**
     * Проверява дали дисциплината е достъпна за даден курс.
     *
     * @param course номер на курс
     * @return true ако е достъпна, false в противен случай
     */
    public boolean isAvailableForCourse(int course) {
        for (int c : availableCourses) {
            if (c == course) {
                return true;
            }
        }
        return false;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}