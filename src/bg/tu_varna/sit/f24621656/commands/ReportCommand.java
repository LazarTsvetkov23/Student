package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Grade;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class ReportCommand extends BaseCommand {
    public ReportCommand(Session session) {
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
            sb.append("\n╔═══════════════════════════════════════════════════════════════════════════╗\n");
            sb.append(String.format("║                     ACADEMIC REPORT FOR %-30s                 ║\n",
                    truncate(student.getName(), 30)));
            sb.append("╠═══════════════════════════════════════════════════════════════════════════╣\n");
            sb.append(String.format("║ Faculty Number: %-65s ║\n", student.getFacultyNumber()));
            sb.append(String.format("║ Specialty: %-66s ║\n", truncate(student.getSpecialty().getName(), 66)));
            sb.append("╠═══════════════════════════════════════════════════════════════════════════╣\n");

            sb.append("║   PASSED EXAMS:                                                         ║\n");
            var passedExams = student.getPassedExams();
            if (passedExams.isEmpty()) {
                sb.append("║    (no passed exams yet)                                                ║\n");
            } else {
                for (Grade g : passedExams) {
                    sb.append(String.format("║    • %-35s %.2f                                          ║\n",
                            truncate(g.getDiscipline().getName(), 35), g.getValue()));
                }
            }

            sb.append("╠═══════════════════════════════════════════════════════════════════════════╣\n");

            sb.append("║   FAILED EXAMS (no grade or grade < 3.00):                               ║\n");
            var failedExams = student.getFailedExams();
            if (failedExams.isEmpty()) {
                sb.append("║    (no failed exams)                                                    ║\n");
            } else {
                for (Discipline d : failedExams) {
                    sb.append(String.format("║    • %-60s ║\n", truncate(d.getName(), 60)));
                }
            }

            sb.append("╠═══════════════════════════════════════════════════════════════════════════╣\n");
            sb.append(String.format("║ Average Grade: %.2f                                                       ║\n", student.getAverageGrade()));
            sb.append(String.format("║ Earned Elective Credits: %-52d ║\n", student.getEarnedElectiveCredits()));
            sb.append(String.format("║ Remaining Elective Credits: %-50d ║\n", student.getRemainingElectiveCredits()));
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
        return "report <fn>";
    }

    @Override
    public String getDescription() {
        return "Shows academic report for a student";
    }

    @Override
    public String getName() {
        return "report";
    }
}