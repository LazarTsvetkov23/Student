package bg.tu_varna.sit.f24621656.commands;

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
                return CommandResult.error("Usage: addgrade <fn> <course> <grade>");
            }
            requireFileOpen();

            String fn = args[1];

            StringBuilder disciplineBuilder = new StringBuilder();
            for (int i = 2; i < args.length - 1; i++) {
                if (i > 2) {
                    disciplineBuilder.append(" ");
                }
                disciplineBuilder.append(args[i]);
            }
            String disciplineName = disciplineBuilder.toString();

            double gradeValue;
            try {
                gradeValue = Double.parseDouble(args[args.length - 1]);
            } catch (NumberFormatException e) {
                return CommandResult.error("Grade must be a number");
            }

            if (gradeValue < 2.00 || gradeValue > 6.00) {
                return CommandResult.error("Grade must be between 2.00 and 6.00");
            }

            Student student = repository.findStudentByFacultyNumber(fn);
            if (student == null) {
                return CommandResult.error("Student with FN " + fn + " not found");
            }

            Discipline discipline = repository.findDisciplineByName(disciplineName);
            if (discipline == null) {
                return CommandResult.error("Discipline '" + disciplineName + "' not found");
            }

            Grade grade = new Grade(discipline, gradeValue);
            if (!student.addGrade(grade)) {
                return CommandResult.error("Cannot add grade. Student is not enrolled in this discipline or status is not ENROLLED");
            }

            session.setHasUnsavedChanges(true);
            String result = gradeValue >= 3.00 ? "PASSED" : "FAILED";

            return CommandResult.success(String.format("Grade %.2f (%s) added for %s in %s", gradeValue, result, fn, disciplineName));

        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "addgrade <fn> <course> <grade>";
    }

    @Override
    public String getDescription() {
        return "Adds a grade for a student";
    }

    @Override
    public String getName() {
        return "addgrade";
    }
}