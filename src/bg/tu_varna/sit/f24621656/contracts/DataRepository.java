package bg.tu_varna.sit.f24621656.contracts;

import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.models.Student;

import java.util.List;

public interface DataRepository {
    void addStudent(Student student);
    Student findStudentByFacultyNumber(String fn);
    List<Student> getAllStudents();
    List<Student> getStudentsBySpecialtyAndCourse(String specialtyName, int course);
    List<Student> getStudentsForProtocol(Discipline discipline);

    void addSpecialty(Specialty specialty);
    void removeSpecialty(Specialty specialty);
    Specialty findSpecialtyByName(String name);
    List<Specialty> getAllSpecialties();

    void addDiscipline(Discipline discipline);
    void removeDiscipline(Discipline discipline);
    Discipline findDisciplineByName(String name);
    List<Discipline> getAllDisciplines();

    void clear();
    boolean isEmpty();
}