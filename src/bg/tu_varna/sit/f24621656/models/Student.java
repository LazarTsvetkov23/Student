package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.DisciplineType;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.validators.DisciplineEnrollmentValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a student in the university system.
 * Stores personal information, academic status, enrolled disciplines, grades and computed average.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class Student {
    /** Full name of the student. */
    private final String name;

    /** Unique faculty number (e.g., "12345"). */
    private final String facultyNumber;

    /** Current course (1-4). */
    private int course;

    /** Specialty (program) in which the student is enrolled. */
    private Specialty specialty;

    /** Group number (positive integer). */
    private int group;

    /** Current academic status (ENROLLED, INTERRUPTED, GRADUATED). */
    private StudentStatus status;

    /** List of grades received by the student. */
    private final List<Grade> grades;

    /** List of disciplines the student is currently enrolled in. */
    private final List<Discipline> enrolledDisciplines;

    /** Cached average grade (recalculated on every change). */
    private double averageGrade;

    /**
     * Constructs a new student with the given data.
     * Initially status is ENROLLED and average grade is 0.0 (will be recalculated).
     *
     * @param name          full name
     * @param facultyNumber unique faculty number
     * @param course        starting course (usually 1)
     * @param specialty     the specialty
     * @param group         group number
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

    /** Returns the student's name. */
    public String getName() {
        return name;
    }

    /** Returns the faculty number. */
    public String getFacultyNumber() {
        return facultyNumber;
    }

    /** Returns the current course. */
    public int getCourse() {
        return course;
    }

    /** Returns the specialty. */
    public Specialty getSpecialty() {
        return specialty;
    }

    /** Returns the group number. */
    public int getGroup() {
        return group;
    }

    /** Returns the academic status. */
    public StudentStatus getStatus() {
        return status;
    }

    /** Returns the list of grades. */
    public List<Grade> getGrades() {
        return grades;
    }

    /** Returns the list of enrolled disciplines. */
    public List<Discipline> getEnrolledDisciplines() {
        return enrolledDisciplines;
    }

    /** Returns the cached average grade. */
    public double getAverageGrade() {
        return averageGrade;
    }

    // Setters

    /** Sets the course. */
    public void setCourse(int course) {
        this.course = course;
    }

    /** Sets the specialty. */
    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    /** Sets the group number. */
    public void setGroup(int group) {
        this.group = group;
    }

    /** Sets the academic status. */
    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    // Helper methods

    /**
     * Checks whether the student has a grade for the given discipline.
     *
     * @param discipline the discipline to check
     * @return true if a grade exists, false otherwise
     */
    public boolean hasGradeForDiscipline(Discipline discipline) {
        for (Grade grade : grades) {
            if (grade.getDiscipline().equals(discipline)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the grade for the given discipline, or null if not found.
     *
     * @param discipline the discipline
     * @return the Grade object or null
     */
    public Grade getGradeForDiscipline(Discipline discipline) {
        for (Grade grade : grades) {
            if (grade.getDiscipline().equals(discipline)) {
                return grade;
            }
        }
        return null;
    }

    /**
     * Checks whether the student has passed a given discipline (grade exists and >=3.00).
     *
     * @param discipline the discipline
     * @return true if passed, false otherwise
     */
    private boolean hasPassedDiscipline(Discipline discipline) {
        Grade grade = getGradeForDiscipline(discipline);
        return grade != null && grade.isPassed();
    }

    /**
     * Recalculates the average grade.
     * Failed disciplines (those without a grade or with grade <3.00) are counted as 2.00.
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
     * Adds a grade to the student.
     * Only allowed if student is ENROLLED and is enrolled in the discipline.
     *
     * @param grade the grade to add
     * @return true if added, false otherwise
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
     * Directly adds a grade (used during XML loading).
     *
     * @param grade the grade to add
     */
    public void addGradeDirectly(Grade grade) {
        grades.add(grade);
        recalculateAverageGrade();
    }

    /**
     * Directly adds an enrolled discipline (used during XML loading).
     *
     * @param discipline the discipline to add
     */
    public void addEnrolledDisciplineDirectly(Discipline discipline) {
        if (!enrolledDisciplines.contains(discipline)) {
            enrolledDisciplines.add(discipline);
            recalculateAverageGrade();
        }
    }

    /**
     * Enrolls the student in a discipline after validation.
     *
     * @param discipline the discipline to enroll in
     * @return true if successful, false otherwise
     */
    public boolean enrollInDiscipline(Discipline discipline) {
        if (!DisciplineEnrollmentValidator.canEnrollInDiscipline(this, discipline)) {
            return false;
        }
        if (!enrolledDisciplines.contains(discipline)) {
            enrolledDisciplines.add(discipline);
            recalculateAverageGrade();
        }
        return true;
    }

    /**
     * Checks whether the student can advance to the next course.
     * Condition: at most 2 mandatory disciplines from past courses are failed.
     *
     * @return true if can advance, false otherwise
     */
    public boolean canAdvance() {
        int failedMandatoryCount = 0;

        for (Discipline discipline : specialty.getDisciplines()) {
            if (discipline.getType() == DisciplineType.MANDATORY) {
                if (discipline.getCourse() <= course) {
                    if (!hasPassedDiscipline(discipline)) {
                        failedMandatoryCount++;
                    }
                }
            }
        }

        return failedMandatoryCount <= 2;
    }

    /**
     * Checks whether the student can graduate.
     * Conditions: all enrolled disciplines are passed AND remaining elective credits = 0.
     *
     * @return true if can graduate, false otherwise
     */
    public boolean canGraduate() {
        for (Discipline discipline : enrolledDisciplines) {
            if (!hasPassedDiscipline(discipline)) {
                return false;
            }
        }

        if (getRemainingElectiveCredits() > 0) {
            return false;
        }

        return true;
    }

    /**
     * Computes the total elective credits earned from passed elective disciplines.
     *
     * @return sum of credits of passed elective disciplines
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
     * Computes the remaining elective credits needed to meet the specialty's minimum.
     *
     * @return max(0, minRequired - earned)
     */
    public int getRemainingElectiveCredits() {
        int needed = specialty.getMinElectiveCredits();
        int earned = getEarnedElectiveCredits();
        return Math.max(needed - earned, 0);
    }

    /**
     * Returns a list of all passed exams (grades with value >=3.00).
     *
     * @return list of passed grades
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
     * Returns a list of disciplines that are failed (no grade or grade <3.00).
     *
     * @return list of failed disciplines
     */
    public List<Discipline> getFailedExams() {
        List<Discipline> result = new ArrayList<>();
        for (Discipline discipline : enrolledDisciplines) {
            if (!hasPassedDiscipline(discipline)) {
                result.add(discipline);
            }
        }
        return result;
    }

    /**
     * Compares students by faculty number.
     *
     * @param object the object to compare with
     * @return true if faculty numbers are equal, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Student student = (Student) object;
        return Objects.equals(facultyNumber, student.facultyNumber);
    }

    /**
     * Returns hash code based on faculty number.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(facultyNumber);
    }
}