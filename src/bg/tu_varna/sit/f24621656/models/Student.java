package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.StudentStatus;

import java.util.ArrayList;

public class Student {
    private final String name;
    private final String facultyNumber;
    private int course;
    private Specialty specialty;
    private int group;
    private StudentStatus status;
    private final double averageGrade;
    private final List<Grade> grades;
    private final List<Discipline> enrolledDisciplines;

    public Student(String name, String facultyNumber, int course, Specialty specialty, int group, StudentStatus status, double averageGrade) {
        this.name = name;
        this.facultyNumber = facultyNumber;
        this.course = course;
        this.specialty = specialty;
        this.group = group;
        this.status = StudentStatus.ENROLLED;
        this.averageGrade = 0.0;
        this.grades = new ArrayList<>(Grade);
        this.enrolledDisciplines = new ArrayList<>(Discipline);
    }

    //getters
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

    public double getAverageGrade() {
        return averageGrade;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public List<Discipline> getEnrolledDisciplines() {
        return enrolledDisciplines;
    }

    //setters
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
}
