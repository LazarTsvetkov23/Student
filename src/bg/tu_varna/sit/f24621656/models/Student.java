package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.StudentStatus;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private final String name;
    private final String facultyNumber;
    private final int course;
    private final String specialty;
    private final int group;
    private StudentStatus status;
    private final double averageGrade;
    private final List<Grade> grades;

    public Student(String name, String facultyNumber, int course, String specialty, int group,
                   StudentStatus status, double averageGrade, List<Grade> grades) {
        this.name = name;
        this.facultyNumber = facultyNumber;
        this.course = course;
        this.specialty = specialty;
        this.group = group;
        this.status = StudentStatus.ENROLLED;
        this.averageGrade = averageGrade;
        this.grades = new ArrayList<>();
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

    public String getSpecialty() {
        return specialty;
    }

    public int getGroup() {
        return group;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }
}
