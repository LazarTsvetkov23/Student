package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.DisciplineType;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.validators.DisciplineEnrollmentValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Клас, представящ студент.
 *
 * <p>Този клас съдържа цялата информация за студент:
 * име, факултетен номер, курс, специалност, група, статус,
 * оценки и записани дисциплини.
 *
 * <p>Средният успех се изчислява автоматично при промяна на оценките
 * или записаните дисциплини, като невзетите изпити се броят за двойки.
 *
 * @author Student
 * @version 1.0
 */
public class Student {
    private final String name;
    private final String facultyNumber;
    private int course;
    private Specialty specialty;
    private int group;
    private StudentStatus status;
    private final List<Grade> grades;
    private final List<Discipline> enrolledDisciplines;
    private double averageGrade;

    /**
     * Конструктор за създаване на нов студент.
     *
     * @param name име на студента
     * @param facultyNumber факултетен номер (уникален)
     * @param course текущ курс (1-4)
     * @param specialty специалност
     * @param group група
     */
    public Student(String name, String facultyNumber, int course, Specialty specialty, int group) {
        this.name = name;
        this.facultyNumber = facultyNumber;
        this.course = course;
        this.specialty = specialty;
        this.group = group;
        this.status = StudentStatus.ENROLLED;
        this.grades = new ArrayList<>();
        this.enrolledDisciplines = new ArrayList<>();
        this.averageGrade = 0.0;
        recalculateAverageGrade();
    }

    // Getters
    /** @return името на студента */
    public String getName() {
        return name;
    }

    /** @return факултетния номер на студента */
    public String getFacultyNumber() {
        return facultyNumber;
    }

    /** @return текущия курс на студента */
    public int getCourse() {
        return course;
    }

    /** @return специалността на студента */
    public Specialty getSpecialty() {
        return specialty;
    }

    /** @return групата на студента */
    public int getGroup() {
        return group;
    }

    /** @return статуса на студента (ENROLLED, INTERRUPTED, GRADUATED) */
    public StudentStatus getStatus() {
        return status;
    }

    /** @return списък с всички оценки на студента */
    public List<Grade> getGrades() {
        return grades;
    }

    /** @return списък със записаните дисциплини на студента */
    public List<Discipline> getEnrolledDisciplines() {
        return enrolledDisciplines;
    }

    /** @return средния успех на студента (изчислен автоматично) */
    public double getAverageGrade() {
        return averageGrade;
    }

    // Setters
    /** @param course нов курс */
    public void setCourse(int course) {
        this.course = course;
    }

    /** @param specialty нова специалност */
    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    /** @param group нова група */
    public void setGroup(int group) {
        this.group = group;
    }

    /** @param status нов статус */
    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    /**
     * Проверява дали студентът има оценка по дадена дисциплина.
     *
     * @param discipline дисциплината за проверка
     * @return true ако има оценка, false в противен случай
     */
    private boolean hasGradeForDiscipline(Discipline discipline) {
        for (Grade grade : grades) {
            if (grade.getDiscipline().equals(discipline)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Връща оценката по дадена дисциплина.
     *
     * @param discipline дисциплината
     * @return оценката или null ако няма
     */
    private Grade getGradeForDiscipline(Discipline discipline) {
        for (Grade grade : grades) {
            if (grade.getDiscipline().equals(discipline)) {
                return grade;
            }
        }
        return null;
    }

    /**
     * Преизчислява средния успех на студента.
     * Невзетите изпити се броят за 2.00.
     */
    private void recalculateAverageGrade() {
        double sum = 0.0;
        int count = 0;

        for (Grade grade : grades) {
            sum += grade.getValue();
            count++;
        }

        for (Discipline discipline : enrolledDisciplines) {
            if (!hasGradeForDiscipline(discipline)) {
                sum += 2.00;
                count++;
            }
        }

        if (count == 0) {
            averageGrade = 0.0;
        } else {
            averageGrade = sum / count;
        }
    }

    /**
     * Добавя оценка на студента.
     *
     * @param grade оценката за добавяне
     * @return true ако оценката е добавена успешно, false в противен случай
     */
    public boolean addGrade(Grade grade) {
        if (status != StudentStatus.ENROLLED) {
            return false;
        }
        if (!enrolledDisciplines.contains(grade.getDiscipline())) {
            return false;
        }
        grades.add(grade);
        recalculateAverageGrade();
        return true;
    }

    /**
     * Записва студент в дисциплина.
     *
     * @param discipline дисциплината за записване
     * @return true ако записването е успешно, false в противен случай
     */
    public boolean enrollInDiscipline(Discipline discipline) {
        if (!DisciplineEnrollmentValidator.canEnrollInDiscipline(this, discipline)) {
            return false;
        }
        enrolledDisciplines.add(discipline);
        recalculateAverageGrade();
        return true;
    }

    /**
     * Проверява дали студентът може да премине в следващ курс.
     * Условие: максимум 2 невзети задължителни предмета от минали курсове.
     *
     * @return true ако може да премине, false в противен случай
     */
    public boolean canAdvance() {
        int failedMandatory = 0;

        for (Discipline discipline : specialty.getDisciplines()) {
            if (discipline.getType() == DisciplineType.MANDATORY) {
                boolean isFromPreviousCourse = false;
                for (int c : discipline.getAvailableCourses()) {
                    if (c < course) {
                        isFromPreviousCourse = true;
                        break;
                    }
                }

                if (isFromPreviousCourse) {
                    Grade grade = getGradeForDiscipline(discipline);
                    if (grade == null || !grade.isPassed()) {
                        failedMandatory++;
                    }
                }
            }
        }
        return failedMandatory <= 2;
    }

    /**
     * Проверява дали студентът може да завърши.
     * Условие: всички задължителни и записани предмети са взети.
     *
     * @return true ако може да завърши, false в противен случай
     */
    public boolean canGraduate() {
        for (Discipline discipline : specialty.getDisciplines()) {
            if (discipline.getType() == DisciplineType.MANDATORY) {
                Grade grade = getGradeForDiscipline(discipline);
                if (grade == null || !grade.isPassed()) {
                    return false;
                }
            }
        }

        for (Discipline discipline : enrolledDisciplines) {
            Grade grade = getGradeForDiscipline(discipline);
            if (grade == null || !grade.isPassed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Изчислява спечелените кредити от избираеми дисциплини.
     *
     * @return брой спечелени кредити
     */
    public int getEarnedElectiveCredits() {
        int credits = 0;
        for (Grade grade : grades) {
            Discipline discipline = grade.getDiscipline();
            if (discipline.getType() == DisciplineType.ELECTIVE && grade.isPassed()) {
                credits += discipline.getCredits();
            }
        }
        return credits;
    }

    /**
     * Изчислява оставащите кредити за дипломиране.
     *
     * @return брой оставащи кредити
     */
    public int getRemainingElectiveCredits() {
        int needed = specialty.getMinElectiveCredits();
        int earned = getEarnedElectiveCredits();

        int remaining = needed - earned;

        if (remaining > 0) {
            return remaining;
        } else {
            return 0;
        }
    }

    /**
     * Връща списък с взетите изпити на студента.
     *
     * @return списък с взетите оценки
     */
    public List<Grade> getPassedExams() {
        List<Grade> result = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.isPassed()) {
                result.add(grade);
            }
        }
        return result;
    }

    /**
     * Връща списък с невзетите изпити на студента.
     *
     * @return списък с дисциплини, които не са взети
     */
    public List<Discipline> getFailedExams() {
        List<Discipline> result = new ArrayList<>();
        for (Discipline discipline : enrolledDisciplines) {
            Grade grade = getGradeForDiscipline(discipline);
            if (grade == null || !grade.isPassed()) {
                result.add(discipline);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Student that = (Student) object;
        return Objects.equals(facultyNumber, that.facultyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facultyNumber);
    }
}