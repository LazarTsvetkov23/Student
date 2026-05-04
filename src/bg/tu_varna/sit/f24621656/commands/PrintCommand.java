package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class PrintCommand extends BaseCommand {
    public PrintCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            validateArgs(args, 2);
            requireFileOpen();

            String fn = args[1];
            Student student = repository.findStudentByFacultyNumber(fn);

            if (student == null) {
                return CommandResult.error("Student with FN " + fn + " not found");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\n┌─────────────────────────────────────────────────────────────┐\n");
            sb.append("│                    👨‍🎓 STUDENT INFORMATION                  │\n");
            sb.append("├─────────────────────────────────────────────────────────────┤\n");
            sb.append(String.format("│ %-20s │ %-35s │\n", "Name", truncate(student.getName(), 35)));
            sb.append(String.format("│ %-20s │ %-35s │\n", "Faculty Number", student.getFacultyNumber()));
            sb.append(String.format("│ %-20s │ %-35s │\n", "Specialty", truncate(student.getSpecialty().getName(), 35)));
            sb.append(String.format("│ %-20s │ %-35s │\n", "Course", student.getCourse()));
            sb.append(String.format("│ %-20s │ %-35s │\n", "Group", student.getGroup()));
            sb.append(String.format("│ %-20s │ %-35s │\n", "Status", student.getStatus()));
            sb.append(String.format("│ %-20s │ %-35s │\n", "Average Grade", String.format("%.2f", student.getAverageGrade())));
            sb.append(String.format("│ %-20s │ %-35s │\n", "Remaining Credits", student.getRemainingElectiveCredits()));
            sb.append("└─────────────────────────────────────────────────────────────┘\n");

            return CommandResult.success(sb.toString());

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "print <fn>";
    }

    @Override
    public String getDescription() {
        return "Prints student information";
    }

    @Override
    public String getName() {
        return "print";
    }
}