package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.List;

public class PrintAllCommand extends BaseCommand {
    public PrintAllCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 3) {
                return CommandResult.error("Usage: printall <program> <year>");
            }
            requireFileOpen();

            StringBuilder programBuilder = new StringBuilder();
            for (int i = 1; i < args.length - 1; i++) {
                if (i > 1) programBuilder.append(" ");
                programBuilder.append(args[i]);
            }
            String programName = programBuilder.toString();

            int year;
            try {
                year = Integer.parseInt(args[args.length - 1]);
            } catch (NumberFormatException e) {
                return CommandResult.error("Year must be a number");
            }

            List<Student> students = repository.getStudentsBySpecialtyAndCourse(programName, year);

            if (students.isEmpty()) {
                return CommandResult.success("No students found in " + programName + ", year " + year);
            }

            StringBuilder sb = new StringBuilder();
            String shortProgram = programName.length() > 25 ? programName.substring(0, 22) + "..." : programName;
            sb.append("\n┌─────────────────────────────────────────────────────────────────────────────┐\n");
            sb.append(String.format("│                   STUDENTS IN %s - YEAR %d                      │\n", shortProgram, year));
            sb.append("├──────────┬─────────────────────────────────────┬────────┬────────────┬────────┤\n");
            sb.append("│    FN    │                Name                 │ Group  │   Status   │  Avg   │\n");
            sb.append("├──────────┼─────────────────────────────────────┼────────┼────────────┼────────┤\n");

            for (Student s : students) {
                sb.append(String.format("│ %-8s │ %-35s │ %-6d │ %-10s │ %6.2f │\n",
                        s.getFacultyNumber(), truncate(s.getName(), 35), s.getGroup(), s.getStatus(), s.getAverageGrade()));
            }

            sb.append("└──────────┴─────────────────────────────────────┴────────┴────────────┴────────┘\n");
            sb.append("Total: ").append(students.size()).append(" student(s)\n");

            return CommandResult.success(sb.toString());

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "printall <program> <year>";
    }

    @Override
    public String getDescription() {
        return "Prints all students in a program and year";
    }

    @Override
    public String getName() {
        return "printall";
    }
}