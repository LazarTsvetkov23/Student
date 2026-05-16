package bg.tu_varna.sit.f24621656.contracts;

import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.models.Student;

import java.util.List;

/**
 * Returns the name of the command (the keyword used to invoke it).
 *
 * @return command name (e.g., "open", "enroll")
 */
public interface DataRepository {
    // Student operations

    /**
     * Adds a student to the repository if not already present.
     *
     * @param student the student to add
     */
    void addStudent(Student student);

    /**
     * Finds a student by faculty number.
     *
     * @param fn faculty number (unique identifier)
     * @return the student if found, otherwise null
     */
    Student findStudentByFacultyNumber(String fn);

    /**
     * Returns a list of all students.
     *
     * @return new list containing all students
     */
    List<Student> getAllStudents();

    /**
     * Returns a list of students enrolled in the given discipline, sorted by faculty number.
     *
     * @param discipline the discipline to filter by
     * @return sorted list of students enrolled in the discipline
     */
    List<Student> getStudentsForProtocol(Discipline discipline);

    // Specialty operations

    /**
     * Adds a specialty to the repository if not already present.
     *
     * @param specialty the specialty to add
     */
    void addSpecialty(Specialty specialty);

    /**
     * Removes a specialty from the repository.
     *
     * @param specialty the specialty to remove
     */
    void removeSpecialty(Specialty specialty);

    /**
     * Finds a specialty by its name (case-insensitive).
     *
     * @param name the name of the specialty
     * @return the specialty if found, otherwise null
     */
    Specialty findSpecialtyByName(String name);

    /**
     * Returns a list of all specialties.
     *
     * @return new list containing all specialties
     */
    List<Specialty> getAllSpecialties();

    // Discipline operations

    /**
     * Adds a discipline to the repository if not already present.
     *
     * @param discipline the discipline to add
     */
    void addDiscipline(Discipline discipline);

    /**
     * Removes a discipline from the repository.
     *
     * @param discipline the discipline to remove
     */
    void removeDiscipline(Discipline discipline);

    /**
     * Finds a discipline by its name (case-insensitive).
     *
     * @param name the name of the discipline
     * @return the discipline if found, otherwise null
     */
    Discipline findDisciplineByName(String name);

    /**
     * Returns a list of all disciplines.
     *
     * @return new list containing all disciplines
     */
    List<Discipline> getAllDisciplines();

    // Utility operations

    /**
     * Clears all data from the repository (students, specialties, disciplines).
     */
    void clear();

    /**
     * Checks whether the repository is empty (no data loaded).
     *
     * @return true if all lists are empty, false otherwise
     */
    boolean isEmpty();
}