package bg.tu_varna.sit.f24621656.commands;

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
                return CommandResult.error("Usage: enrollin <fn> <course>");
            }
            requireFileOpen();

            String facultyNumber = args[1];

            StringBuilder disciplineBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                if (i > 2) {
                    disciplineBuilder.append(" ");
                }
                disciplineBuilder.append(args[i]);
            }
            String disciplineName = disciplineBuilder.toString();

            Student student = repository.findStudentByFacultyNumber(facultyNumber);
            if (student == null) {
                return CommandResult.error("❌ Student with faculty number " + facultyNumber + " not found");
            }

            Discipline discipline = repository.findDisciplineByName(disciplineName);
            if (discipline == null) {
                return CommandResult.error("❌ Discipline '" + disciplineName + "' not found");
            }

            if (!student.enrollInDiscipline(discipline)) {
                return CommandResult.error("❌ Cannot enroll in discipline. Check: discipline exists for this course/specialty, student is enrolled");
            }

            session.setHasUnsavedChanges(true);
            return CommandResult.success("✅ Student " + facultyNumber + " enrolled in " + disciplineName);

        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "enrollin <fn> <course>";
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