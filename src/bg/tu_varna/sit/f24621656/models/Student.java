package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.DisciplineType;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;

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
    private double averageGrade;
    private final List<Grade> grades;
    private final List<Discipline> enrolledDisciplines;

    public Student(String name, String facultyNumber, int course, Specialty specialty, int group) {
        this.name = name;
        this.facultyNumber = facultyNumber;
        this.course = course;
        this.specialty = specialty;
        this.group = group;
        this.status = StudentStatus.ENROLLED;
        this.averageGrade = 0.0;
        this.grades = new ArrayList<>();
        this.enrolledDisciplines = new ArrayList<>();
        recalculateAverageGrade();
    }

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

    public double getAverageGrade() {
        return averageGrade;
    }

    public List<Discipline> getEnrolledDisciplines() {
        return enrolledDisciplines;
    }

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
            if (grade.getDisciplineName().equals(discipline)) {
                return true;
            }
        }
        return false;
    }

    private void recalculateAverageGrade() {
        double sum = 0;
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
        if(count == 0) {
            averageGrade = 0.0;
        } else {
            averageGrade = sum / count;
        }
    }

    public boolean addGrade(Grade grade) {
        if (status != StudentStatus.ENROLLED) {
            return false;
        }
        if (!enrolledDisciplines.contains(grade.getDisciplineName())) {
            return false;
        }
        grades.add(grade);
        recalculateAverageGrade();
        return true;
    }

    public boolean enrollInDiscipline(Discipline discipline) {
        if (status != StudentStatus.ENROLLED) {
            return false;
        }
        if (!discipline.isAvailableForCourse(course)) {
            return false;
        }
        if (!specialty.getDisciplines().contains(discipline)) {
            return false;
        }
        if (enrolledDisciplines.contains(discipline) || hasGradeForDiscipline(discipline)) {
            return false;
        }
        enrolledDisciplines.add(discipline);
        recalculateAverageGrade();
        return true;
    }

    public boolean canAdvanceToNextCourse() {
        if (status != StudentStatus.ENROLLED) {
            return false;
        }

        int failed = 0;

        for (Discipline discipline : specialty.getDisciplines()) {
            if (discipline.getType() == DisciplineType.MANDATORY) {
                boolean fromPrevCourses = false;
                for (int c : discipline.getAvailableCourses()) {
                    if (c < course) {
                        fromPrevCourses = true;
                        break;
                    }
                }
                if (fromPrevCourses) {
                    boolean passed = false;
                    for (Grade grade : grades) {
                        if (grade.getDisciplineName().equals(discipline) && grade.isPassed()) {
                            passed = true;
                            break;
                        }
                    }
                    if (!passed) {
                        failed++;
                    }
                }
            }
        }
        return failed <= 2;
    }

    public boolean canGraduate() {
        if (status != StudentStatus.ENROLLED) {
            return false;
        }
        for (Discipline discipline : specialty.getDisciplines()) {
            if (discipline.getType() == DisciplineType.MANDATORY) {
                boolean passed = false;
                for (Grade grade : grades) {
                    if (grade.getDisciplineName().equals(discipline) && grade.isPassed()) {
                        passed = true;
                        break;
                    }
                }
                if (!passed) {
                    return false;
                }
            }
        }

        for (Discipline discipline : enrolledDisciplines) {
            boolean passed = false;
            for (Grade grade : grades) {
                if (grade.getDisciplineName().equals(discipline) && grade.isPassed()) {
                    passed = true;
                    break;
                }
            }
            if (!passed) {
                return false;
            }
        }
        return true;
    }

    public int getEarnedElectiveCredits() {
        int credits = 0;

        for (Grade grade : grades) {
            Discipline discipline = grade.getDisciplineName();
            if (discipline.getType() == DisciplineType.ELECTIVE && grade.isPassed()) {
                credits += discipline.getCredits();
            }
        }
        return credits;
    }

    public int getRemainingElectiveCredits() {
        return Math.max(0, specialty.getMinElectiveCredits() - getEarnedElectiveCredits());
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
            boolean hasPassed = false;
            for (Grade grade : grades) {
                if (grade.getDisciplineName().equals(discipline) && grade.isPassed()) {
                    hasPassed = true;
                    break;
                }
            }
            if (!hasPassed) {
                result.add(discipline);
            }
        }
        return result;
    }

    public boolean canChangeToSpecialty(Specialty newSpecialty) {
        if (status != StudentStatus.ENROLLED) {
            return false;
        }
        for (Discipline discipline : newSpecialty.getDisciplines()) {
            if (discipline.getType() == DisciplineType.MANDATORY) {
                boolean fromPrevCourses = false;
                for (int c : discipline.getAvailableCourses()) {
                    if (c < course) {
                        fromPrevCourses = true;
                        break;
                    }
                }
                if (fromPrevCourses) {
                    boolean passed = false;
                    for (Grade grade : grades) {
                        if (grade.getDisciplineName().equals(discipline) && grade.isPassed()) {
                            passed = true;
                            break;
                        }
                    }
                    if (!passed) {
                        return false;
                    }
                }
            }
        }
        return true;
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
