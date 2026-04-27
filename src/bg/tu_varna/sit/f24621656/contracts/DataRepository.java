package bg.tu_varna.sit.f24621656.contracts;

import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.models.Student;

import java.util.List;

/**
 * Интерфейс за хранилище на данни.
 *
 * <p>Този интерфейс дефинира всички операции за достъп до данните
 * в системата. Спазва принципа Dependency Inversion - зависимостите
 * са към абстракция, а не към конкретна имплементация.
 *
 * @author Student
 * @version 1.0
 */
public interface DataRepository {

    // ==================== Student Operations ====================

    /**
     * Добавя студент в репозиторито.
     *
     * @param student студентът за добавяне
     */
    void addStudent(Student student);

    /**
     * Търси студент по факултетен номер.
     *
     * @param fn факултетен номер
     * @return студентът или null, ако не е намерен
     */
    Student findStudentByFacultyNumber(String fn);

    /**
     * Връща всички студенти.
     *
     * @return списък с всички студенти
     */
    List<Student> getAllStudents();

    /**
     * Връща студентите в дадена специалност и курс.
     *
     * @param specialtyName име на специалност
     * @param course курс
     * @return списък със студентите
     */
    List<Student> getStudentsBySpecialtyAndCourse(String specialtyName, int course);

    /**
     * Връща студентите, записани в дадена дисциплина, сортирани по факултетен номер.
     *
     * @param discipline дисциплината
     * @return списък със студентите
     */
    List<Student> getStudentsForProtocol(Discipline discipline);

    // ==================== Specialty Operations ====================

    /**
     * Добавя специалност в репозиторито.
     *
     * @param specialty специалността за добавяне
     */
    void addSpecialty(Specialty specialty);

    /**
     * Търси специалност по име.
     *
     * @param name име на специалността
     * @return специалността или null, ако не е намерена
     */
    Specialty findSpecialtyByName(String name);

    /**
     * Връща всички специалности.
     *
     * @return списък с всички специалности
     */
    List<Specialty> getAllSpecialties();

    // ==================== Discipline Operations ====================

    /**
     * Добавя дисциплина в репозиторито.
     *
     * @param discipline дисциплината за добавяне
     */
    void addDiscipline(Discipline discipline);

    /**
     * Търси дисциплина по име.
     *
     * @param name име на дисциплината
     * @return дисциплината или null, ако не е намерена
     */
    Discipline findDisciplineByName(String name);

    /**
     * Връща всички дисциплини.
     *
     * @return списък с всички дисциплини
     */
    List<Discipline> getAllDisciplines();

    // ==================== Common Operations ====================

    /**
     * Изчиства всички данни от репозиторито.
     */
    void clear();

    /**
     * Проверява дали репозиторито е празно.
     *
     * @return true ако няма данни
     */
    boolean isEmpty();
}