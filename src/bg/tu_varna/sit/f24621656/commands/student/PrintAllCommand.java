package bg.tu_varna.sit.f24621656.commands.student;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Prints all enrolled students in a given specialty and course (year).
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class PrintAllCommand extends BaseCommand {
    /**
     * Constructs a PrintAllCommand with the given session.
     *
     * @param session the current session
     */
    public PrintAllCommand(Session session) {
        super(session);
    }

    /**
     * Executes the printall command.
     * Expected format: printall "<program>" <year>
     *
     * @param args command arguments
     * @return CommandResult with the list of students or an empty message
     */
    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 3) {
                return CommandResult.error("Usage: printall \"<program>\" <year>");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            int yearIndex = args.length - 1;
            int year;
            try {
                year = Integer.parseInt(args[yearIndex]);
            } catch (NumberFormatException e) {
                return CommandResult.error("Year must be a number");
            }

            StringBuilder programBuilder = new StringBuilder();
            for (int i = 1; i < yearIndex; i++) {
                if (i > 1) {
                    programBuilder.append(" ");
                }
                programBuilder.append(args[i]);
            }
            String programName = programBuilder.toString();

            if (programName.startsWith("\"") && programName.endsWith("\"")) {
                programName = programName.substring(1, programName.length() - 1);
            }

            List<Student> allStudents = getRepository().getAllStudents();
            List<Student> filteredStudents = new ArrayList<>();

            for (Student student : allStudents) {
                if (student.getStatus() == StudentStatus.ENROLLED &&
                        student.getCourse() == year &&
                        student.getSpecialty().getName().equalsIgnoreCase(programName)) {
                    filteredStudents.add(student);
                }
            }

            if (filteredStudents.isEmpty()) {
                return CommandResult.success("No enrolled students found in " + programName + ", year " + year);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\nEnrolled Students in ").append(programName).append(" - Year ").append(year).append(":\n");
            sb.append("------------------------------------------------\n");
            for (Student student : filteredStudents) {
                sb.append(student.getFacultyNumber()).append(" | ")
                        .append(student.getName()).append(" | ")
                        .append("Group ").append(student.getGroup()).append("\n");
            }
            return CommandResult.success(sb.toString());

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "printall \"<program>\" <year>";
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