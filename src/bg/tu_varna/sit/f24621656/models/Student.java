package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.DisciplineType;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.validators.DisciplineEnrollmentValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public String getName() {
        return name;
    }

    public String getFacultyNumber() {
        return facultyNumber;
    }

    public int getCourse() {
        return course;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public int getGroup() {
        return group;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public List<Discipline> getEnrolledDisciplines() {
        return enrolledDisciplines;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    // Setters
    public void setCourse(int course) {
        this.course = course;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    private boolean hasGradeForDiscipline(Discipline discipline) {
        for (Grade grade : grades) {
            if (grade.getDiscipline().equals(discipline)) {
                return true;
            }
        }
        return false;
    }

    private Grade getGradeForDiscipline(Discipline discipline) {
        for (Grade grade : grades) {
            if (grade.getDiscipline().equals(discipline)) {
                return grade;
            }
        }
        return null;
    }

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

    public boolean enrollInDiscipline(Discipline discipline) {
        if (!DisciplineEnrollmentValidator.canEnrollInDiscipline(this, discipline)) {
            return false;
        }
        enrolledDisciplines.add(discipline);
        recalculateAverageGrade();

        return true;
    }

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

    public List<Grade> getPassedExams() {
        List<Grade> result = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.isPassed()) {
                result.add(grade);
            }
        }
        return result;
    }

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