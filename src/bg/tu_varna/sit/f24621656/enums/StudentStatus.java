package bg.tu_varna.sit.f24621656.enums;

/**
 * Enumeration representing the academic status of a student.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public enum StudentStatus {
    /** Student is actively enrolled and can take exams, enroll in disciplines, etc. */
    ENROLLED,
    /** Student has interrupted their studies – cannot perform most actions. */
    INTERRUPTED,
    /** Student has graduated and is no longer active. */
    GRADUATED
}