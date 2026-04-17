package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.StudentStatus;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private final String name;
    private final String facultyNumber;
    private int course;
    private Specialty specialty;
    private int group;
    private StudentStatus status;
    private final double averageGrade;
    private final List<Grade> grades;
    private final List<Discipline> enrolledDisciplines;          // записани, но без оценка

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

    public double getAverageGrade() {
        return averageGrade;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public List<Discipline> getEnrolledDisciplines() {
        return enrolledDisciplines;
    }

    // Setters (за променливите, които не са final)
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

    // Добавя оценка (ако дисциплината е записана)
    public boolean addGrade(Grade grade) {
        if(status != StudentStatus.ENROLLED) {
            return false;       // прекъснал или завършил не може да добавя оценки
        }
        // Проверка дали дисциплината е записана
        if(enrolledDisciplines.contains(grade.getName())) {
            grades.add(grade);
            return true;
        }
        return false;
    }
}
