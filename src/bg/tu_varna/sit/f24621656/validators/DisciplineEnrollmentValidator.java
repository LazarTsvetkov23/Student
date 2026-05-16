package bg.tu_varna.sit.f24621656.validators;

import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Grade;
import bg.tu_varna.sit.f24621656.models.Student;

/**
 * Validates whether a student can be enrolled in a discipline.
 * Contains the core business rules for enrollment.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class DisciplineEnrollmentValidator {
    /**
     * Checks if a student is allowed to enroll in the given discipline.
     * Conditions:
     * - Student must have status ENROLLED.
     * - Discipline course must match student's current course.
     * - Discipline must belong to student's specialty.
     * - Student must not be already enrolled.
     * - Student must not already have a grade for that discipline.
     *
     * @param student    the student
     * @param discipline the discipline
     * @return true if enrollment is allowed, false otherwise
     */
    public static boolean canEnrollInDiscipline(Student student, Discipline discipline) {
        if (student.getStatus() != StudentStatus.ENROLLED) {
            return false;
        }

        if (discipline.getCourse() != student.getCourse()) {
            return false;
        }

        if (!student.getSpecialty().getDisciplines().contains(discipline)) {
            return false;
        }

        if (student.getEnrolledDisciplines().contains(discipline)) {
            return false;
        }

        for (Grade grade : student.getGrades()) {
            if (grade.getDiscipline().equals(discipline)) {
                return false;
            }
        }

        return true;
    }
}
