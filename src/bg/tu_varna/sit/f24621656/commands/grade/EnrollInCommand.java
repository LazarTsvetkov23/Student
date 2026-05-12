package bg.tu_varna.sit.f24621656.commands.grade;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Discipline;
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
                if (i > 2) {
                    disciplineBuilder.append(" ");
                }
                disciplineBuilder.append(args[i]);
            }
            String disciplineName = disciplineBuilder.toString();

            if (disciplineName.startsWith("\"") && disciplineName.endsWith("\"")) {
                disciplineName = disciplineName.substring(1, disciplineName.length() - 1);
            }

            Student student = getRepository().findStudentByFacultyNumber(fn);
            if (student == null) {
                return CommandResult.error("Student with FN " + fn + " not found");
            }

            Discipline discipline = getRepository().findDisciplineByName(disciplineName);
            if (discipline == null) {
                return CommandResult.error("Discipline '" + disciplineName + "' not found");
            }

            boolean isAvailableForStudent = false;
            for (int availableCourse : discipline.getAvailableCourses()) {
                if (availableCourse <= student.getCourse()) {
                    isAvailableForStudent = true;
                    break;
                }
            }

            if (!isAvailableForStudent) {
                return CommandResult.error("Discipline '" + disciplineName +
                        "' is not available for course " + student.getCourse() +
                        " or lower. Available courses: " + discipline.getAvailableCourses());
            }

            if (student.getStatus() != bg.tu_varna.sit.f24621656.enums.StudentStatus.ENROLLED) {
                return CommandResult.error("Student is not enrolled. Status: " + student.getStatus());
            }

            if (!student.getSpecialty().getDisciplines().contains(discipline)) {
                return CommandResult.error("Discipline '" + disciplineName + "' is not in specialty '" + student.getSpecialty().getName() + "'");
            }

            if (student.getEnrolledDisciplines().contains(discipline)) {
                return CommandResult.error("Student already enrolled in " + disciplineName);
            }

            for (bg.tu_varna.sit.f24621656.models.Grade g : student.getGrades()) {
                if (g.getDiscipline().equals(discipline)) {
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