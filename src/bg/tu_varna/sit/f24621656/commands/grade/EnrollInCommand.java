package bg.tu_varna.sit.f24621656.commands.grade;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Grade;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class EnrollInCommand extends BaseCommand {
    public EnrollInCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 3) {
                return CommandResult.error("Usage: enrollin <fn> \"<discipline>\"");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            String fn = args[1];

            StringBuilder disciplineBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                if (i > 2) disciplineBuilder.append(" ");
                disciplineBuilder.append(args[i]);
            }
            String disciplineName = disciplineBuilder.toString();

            // ========== НОВА ПРОВЕРКА: задължителни кавички ==========
            if (!disciplineName.startsWith("\"") || !disciplineName.endsWith("\"")) {
                return CommandResult.error("Discipline name must be enclosed in quotes: \"<discipline>\"");
            }
            // Премахване на кавичките
            disciplineName = disciplineName.substring(1, disciplineName.length() - 1);

            Student student = getRepository().findStudentByFacultyNumber(fn);
            if (student == null) {
                return CommandResult.error("Student with FN " + fn + " not found");
            }

            Discipline discipline = getRepository().findDisciplineByName(disciplineName);
            if (discipline == null) {
                return CommandResult.error("Discipline '" + disciplineName + "' not found");
            }

            // Проверка за точен курс
            if (discipline.getCourse() != student.getCourse()) {
                return CommandResult.error("Discipline '" + disciplineName +
                        "' is for course " + discipline.getCourse() +
                        ", but student is in course " + student.getCourse());
            }

            if (student.getStatus() != StudentStatus.ENROLLED) {
                return CommandResult.error("Student is not enrolled. Status: " + student.getStatus());
            }

            if (!student.getSpecialty().getDisciplines().contains(discipline)) {
                return CommandResult.error("Discipline '" + disciplineName + "' is not in specialty '" + student.getSpecialty().getName() + "'");
            }

            if (student.getEnrolledDisciplines().contains(discipline)) {
                return CommandResult.error("Student already enrolled in " + disciplineName);
            }

            for (Grade grade : student.getGrades()) {
                if (grade.getDiscipline().equals(discipline)) {
                    return CommandResult.error("Student already has a grade in " + disciplineName);
                }
            }

            student.enrollInDiscipline(discipline);
            getSession().setHasUnsavedChanges(true);
            return CommandResult.success("Student " + fn + " enrolled in " + disciplineName);

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "enrollin <fn> \"<discipline>\"";
    }

    @Override
    public String getDescription() {
        return "Enrolls a student in a discipline";
    }

    @Override
    public String getName() {
        return "enrollin";
    }
}