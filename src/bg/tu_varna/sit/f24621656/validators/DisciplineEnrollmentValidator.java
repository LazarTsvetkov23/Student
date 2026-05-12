package bg.tu_varna.sit.f24621656.validators;

import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Grade;
import bg.tu_varna.sit.f24621656.models.Student;

public class DisciplineEnrollmentValidator {
    public static boolean canEnrollInDiscipline(Student student, Discipline discipline) {
        if (student.getStatus() != StudentStatus.ENROLLED) {
            return false;
        }

        boolean isAvailable = false;
        for (int availableCourse : discipline.getAvailableCourses()) {
            if (availableCourse <= student.getCourse()) {
                isAvailable = true;
                break;
            }
        }
        if (!isAvailable) {
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
