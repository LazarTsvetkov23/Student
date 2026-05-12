package bg.tu_varna.sit.f24621656.commands.grade;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Grade;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.List;

public class ProtocolCommand extends BaseCommand {
    public ProtocolCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: protocol \"<discipline>\"");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            StringBuilder disciplineBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i > 1) {
                    disciplineBuilder.append(" ");
                }
                disciplineBuilder.append(args[i]);
            }
            String disciplineName = disciplineBuilder.toString();

            if (disciplineName.startsWith("\"") && disciplineName.endsWith("\"")) {
                disciplineName = disciplineName.substring(1, disciplineName.length() - 1);
            }

            Discipline discipline = getRepository().findDisciplineByName(disciplineName);
            if (discipline == null) {
                return CommandResult.error("Discipline '" + disciplineName + "' not found");
            }

            List<Student> students = getRepository().getStudentsForProtocol(discipline);
            if (students.isEmpty()) {
                return CommandResult.success("No students enrolled in " + disciplineName);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\nProtocol for: ").append(disciplineName).append("\n");
            sb.append("========================================\n");

            String currentSpecialty = "";
            int currentCourse = -1;

            for (Student student : students) {
                String specialty = student.getSpecialty().getName();
                int course = student.getCourse();

                if (!specialty.equals(currentSpecialty) || course != currentCourse) {
                    currentSpecialty = specialty;
                    currentCourse = course;
                    sb.append("\n--- ").append(specialty).append(" - Year ").append(course).append(" ---\n");
                    sb.append("FN\tName\t\t\tGroup\tGrade\tStatus\n");
                    sb.append("------------------------------------------------\n");
                }

                Grade grade = null;
                for (Grade g : student.getGrades()) {
                    if (g.getDiscipline().equals(discipline)) {
                        grade = g;
                        break;
                    }
                }

                String gradeStr;
                String statusStr;

                if (grade == null) {
                    gradeStr = "not taken";
                    statusStr = "PENDING";
                } else {
                    gradeStr = String.format("%.2f", grade.getValue());
                    if (grade.isPassed()) {
                        statusStr = "PASSED";
                    } else {
                        statusStr = "FAILED";
                    }
                }

                sb.append(student.getFacultyNumber()).append("\t")
                        .append(student.getName()).append("\t\t")
                        .append("Group ").append(student.getGroup()).append("\t")
                        .append(gradeStr).append("\t")
                        .append(statusStr).append("\n");
            }

            return CommandResult.success(sb.toString());

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "protocol \"<discipline>\"";
    }

    @Override
    public String getDescription() {
        return "Shows protocol for a discipline";
    }

    @Override
    public String getName() {
        return "protocol";
    }
}