package bg.tu_varna.sit.f24621656.commands;

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
                return CommandResult.error("Usage: protocol <course>");
            }
            requireFileOpen();

            StringBuilder disciplineBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i > 1) disciplineBuilder.append(" ");
                disciplineBuilder.append(args[i]);
            }
            String disciplineName = disciplineBuilder.toString();

            Discipline discipline = repository.findDisciplineByName(disciplineName);
            if (discipline == null) {
                return CommandResult.error("Discipline '" + disciplineName + "' not found");
            }

            List<Student> students = repository.getStudentsForProtocol(discipline);

            if (students.isEmpty()) {
                return CommandResult.success("No students enrolled in " + disciplineName);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\n╔═══════════════════════════════════════════════════════════════════════════╗\n");
            sb.append(String.format("║                      PROTOCOL FOR: %-46s ║\n",
                    truncate(disciplineName, 46)));
            sb.append("╠═══════════════════════════════════════════════════════════════════════════╣\n");

            String currentSpecialty = "";
            int currentCourse = -1;

            for (Student s : students) {
                String specialty = s.getSpecialty().getName();
                int course = s.getCourse();

                if (!specialty.equals(currentSpecialty) || course != currentCourse) {
                    currentSpecialty = specialty;
                    currentCourse = course;
                    sb.append("╠═══════════════════════════════════════════════════════════════════════════╣\n");
                    sb.append(String.format("║ %-71s ║\n", truncate(specialty + " - YEAR " + course, 71)));
                    sb.append("╠──────────┬─────────────────────────────────────┬────────┬────────────┬────────╣\n");
                    sb.append("║    FN    │                Name                 │ Group  │   Grade    │ Status ║\n");
                    sb.append("╠──────────┼─────────────────────────────────────┼────────┼────────────┼────────╣\n");
                }

                Grade grade = null;
                for (Grade g : s.getGrades()) {
                    if (g.getDiscipline().equals(discipline)) {
                        grade = g;
                        break;
                    }
                }

                String gradeStr = (grade == null) ? "not taken" : String.format("%.2f", grade.getValue());
                String statusStr = (grade == null) ? "PENDING" : (grade.isPassed() ? "PASSED" : "FAILED");

                sb.append(String.format("║ %-8s │ %-35s │ %-6d │ %-10s │ %-6s ║\n",
                        s.getFacultyNumber(), truncate(s.getName(), 35), s.getGroup(), gradeStr, statusStr));
            }

            sb.append("╚═══════════════════════════════════════════════════════════════════════════╝\n");

            return CommandResult.success(sb.toString());

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "protocol <course>";
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