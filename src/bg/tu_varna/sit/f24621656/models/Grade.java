package bg.tu_varna.sit.f24621656.models;

/**
 * Клас, представящ оценка.
 *
 * <p>Всяка оценка е свързана с дисциплина и има числена стойност
 * в интервала [2.00, 6.00].
 *
 * @author Student
 * @version 1.0
 */
public class Grade {
    private final Discipline discipline;
    private final double value;

    /**
     * Конструктор за оценка.
     *
     * @param disciplineName дисциплината, по която е оценката
     * @param value числена стойност на оценката (2.00 - 6.00)
     */
    public Grade(Discipline disciplineName, double value) {
        this.discipline = disciplineName;
        this.value = value;
    }

    /** @return дисциплината */
    public Discipline getDiscipline() {
        return discipline;
    }

    /** @return числената стойност на оценката */
    public double getValue() {
        return value;
    }

    /**
     * Проверява дали оценката е успешна (>= 3.00).
     *
     * @return true ако оценката е >= 3.00
     */
    public boolean isPassed() {
        return value >= 3.00;
    }
}