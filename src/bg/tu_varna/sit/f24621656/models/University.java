package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.contracts.DataRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of the DataRepository interface.
 * Stores students, specialties and disciplines in ArrayLists.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class University implements DataRepository {

    /** List of all students. */
    private final List<Student> students;

    /** List of all specialties. */
    private final List<Specialty> specialties;

    /** List of all disciplines. */
    private final List<Discipline> disciplines;

    /**
     * Constructs an empty university repository.
     */
    public University() {
        this.students = new ArrayList<>();
        this.specialties = new ArrayList<>();
        this.disciplines = new ArrayList<>();
    }


    @Override
    public void addStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
        }
    }

    @Override
    public Student findStudentByFacultyNumber(String fn) {
        for (Student student : students) {
            if (student.getFacultyNumber().equals(fn)) {
                return student;
            }
        }
        return null;
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Returns students enrolled in the given discipline, sorted by faculty number (bubble sort).
     *
     * @param discipline the discipline to filter by
     * @return sorted list of students
     */
    @Override
    public List<Student> getStudentsForProtocol(Discipline discipline) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getEnrolledDisciplines().contains(discipline)) {
                result.add(student);
            }
        }
        for (int i = 0; i < result.size() - 1; i++) {
            for (int j = 0; j < result.size() - i - 1; j++) {
                if (result.get(j).getFacultyNumber().compareTo(result.get(j + 1).getFacultyNumber()) > 0) {
                    Student temp = result.get(j);
                    result.set(j, result.get(j + 1));
                    result.set(j + 1, temp);
                }
            }
        }
        return result;
    }

    @Override
    public void addSpecialty(Specialty specialty) {
        if (!specialties.contains(specialty)) {
            specialties.add(specialty);
        }
    }

    @Override
    public void removeSpecialty(Specialty specialty) {
        specialties.remove(specialty);
    }

    @Override
    public Specialty findSpecialtyByName(String name) {
        for (Specialty specialty : specialties) {
            if (specialty.getName().equalsIgnoreCase(name)) {
                return specialty;
            }
        }
        return null;
    }

    @Override
    public List<Specialty> getAllSpecialties() {
        return new ArrayList<>(specialties);
    }

    @Override
    public void addDiscipline(Discipline discipline) {
        if (!disciplines.contains(discipline)) {
            disciplines.add(discipline);
        }
    }

    @Override
    public void removeDiscipline(Discipline discipline) {
        disciplines.remove(discipline);
    }

    @Override
    public Discipline findDisciplineByName(String name) {
        for (Discipline discipline : disciplines) {
            if (discipline.getName().equalsIgnoreCase(name)) {
                return discipline;
            }
        }
        return null;
    }

    @Override
    public List<Discipline> getAllDisciplines() {
        return new ArrayList<>(disciplines);
    }

    @Override
    public void clear() {
        students.clear();
        specialties.clear();
        disciplines.clear();
    }

    @Override
    public boolean isEmpty() {
        return students.isEmpty() && specialties.isEmpty() && disciplines.isEmpty();
    }
}
