package bg.tu_varna.sit.f24621656.commands.grade;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Grade;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class AddGradeCommand extends BaseCommand {
    public AddGradeCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 4) {
                return CommandResult.error("Usage: addgrade <fn> \"<discipline>\" <grade>");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            String fn = args[1];

            StringBuilder disciplineBuilder = new StringBuilder();
            for (int i = 2; i < args.length - 1; i++) {
                if (i > 2) {
                    disciplineBuilder.append(" ");
                }
                disciplineBuilder.append(args[i]);
            }
            String disciplineName = disciplineBuilder.toString();

            if (disciplineName.startsWith("\"") && disciplineName.endsWith("\"")) {
                disciplineName = disciplineName.substring(1, disciplineName.length() - 1);
            }

            double gradeValue;
            try {
                gradeValue = Double.parseDouble(args[args.length - 1]);
            } catch (NumberFormatException e) {
                return CommandResult.error("Grade must be a number");
            }

            if (gradeValue < 2.00 || gradeValue > 6.00) {
                return CommandResult.error("Grade must be between 2.00 and 6.00");
            }

            Student student = getRepository().findStudentByFacultyNumber(fn);
            if (student == null) {
                return CommandResult.error("Student with FN " + fn + " not found");
            }

            Discipline discipline = getRepository().findDisciplineByName(disciplineName);
            if (discipline == null) {
                return CommandResult.error("Discipline '" + disciplineName + "' not found");
            }

            if (student.getStatus() != bg.tu_varna.sit.f24621656.enums.StudentStatus.ENROLLED) {
                return CommandResult.error("Student is not enrolled. Status: " + student.getStatus());
            }

            if (!student.getEnrolledDisciplines().contains(discipline)) {
                return CommandResult.error("Student is not enrolled in " + disciplineName + ". Use 'enrollin' first.");
            }

            if (student.getGradeForDiscipline(discipline) != null) {
                return CommandResult.error("Student already has a grade in " + disciplineName);
            }

            Grade grade = new Grade(discipline, gradeValue);
            student.addGrade(grade);
            getSession().setHasUnsavedChanges(true);

            String result;
            if (gradeValue >= 3.00) {
                result = "PASSED";
            } else {
                result = "FAILED";
            }

            return CommandResult.success("Grade " + gradeValue + " (" + result + ") added for " + fn + " in " + disciplineName);

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "addgrade <fn> \"<discipline>\" <grade>";
    }

    @Override
    public String getDescription() {
        return "Adds a grade for a student in a discipline";
    }

    @Override
    public String getName() {
        return "addgrade";
    }
}