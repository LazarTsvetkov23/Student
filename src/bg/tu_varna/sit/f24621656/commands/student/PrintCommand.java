package bg.tu_varna.sit.f24621656.commands.student;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class PrintCommand extends BaseCommand {
    public PrintCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: print <fn>");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            String fn = args[1];
            Student student = getRepository().findStudentByFacultyNumber(fn);

            if (student == null) {
                return CommandResult.error("Student with FN " + fn + " not found");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\nStudent Information:\n");
            sb.append("-------------------\n");
            sb.append("Name: ").append(student.getName()).append("\n");
            sb.append("Faculty Number: ").append(student.getFacultyNumber()).append("\n");
            sb.append("Specialty: ").append(student.getSpecialty().getName()).append("\n");
            sb.append("Course: ").append(student.getCourse()).append("\n");
            sb.append("Group: ").append(student.getGroup()).append("\n");
            sb.append("Status: ").append(student.getStatus()).append("\n");
            sb.append("Average Grade: ").append(String.format("%.2f", student.getAverageGrade())).append("\n");
            sb.append("Remaining Credits: ").append(student.getRemainingElectiveCredits()).append("\n");

            return CommandResult.success(sb.toString());

        } catch (Exception e) {
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