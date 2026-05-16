package bg.tu_varna.sit.f24621656.commands.grade;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Grade;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.List;

/**
 * Displays an academic report for a student.
 * Includes passed exams (with grades), failed exams (disciplines without grade or grade <3.00),
 * average grade (with failed ones counted as 2.00), earned and remaining elective credits.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class ReportCommand extends BaseCommand {
    /**
     * Constructs a ReportCommand with the given session.
     *
     * @param session the current session
     */
    public ReportCommand(Session session) {
        super(session);
    }

    /**
     * Executes the report command.
     * Expected format: report <fn>
     *
     * @param args command arguments
     * @return CommandResult with the academic report
     */
    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: report <fn>");
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
            sb.append("\nAcademic Report for: ").append(student.getName()).append("\n");
            sb.append("Faculty Number: ").append(student.getFacultyNumber()).append("\n");
            sb.append("Specialty: ").append(student.getSpecialty().getName()).append("\n\n");

            sb.append("PASSED EXAMS:\n");
            sb.append("-------------\n");
            List<Grade> passedExams = student.getPassedExams();
            if (passedExams.isEmpty()) {
                sb.append("No passed exams yet.\n");
            } else {
                for (Grade grade : passedExams) {
                    sb.append(grade.getDiscipline().getName()).append(": ").append(String.format("%.2f", grade.getValue())).append("\n");
                }
            }

            sb.append("\nFAILED EXAMS (no grade or grade < 3.00):\n");
            sb.append("----------------------------------------\n");
            List<Discipline> failedExams = student.getFailedExams();
            if (failedExams.isEmpty()) {
                sb.append("No failed exams.\n");
            } else {
                for (Discipline discipline : failedExams) {
                    sb.append(discipline.getName()).append("\n");
                }
            }

            sb.append("\nAverage Grade: ").append(String.format("%.2f", student.getAverageGrade())).append("\n");
            sb.append("Earned Elective Credits: ").append(student.getEarnedElectiveCredits()).append("\n");
            sb.append("Remaining Elective Credits: ").append(student.getRemainingElectiveCredits()).append("\n");

            return CommandResult.success(sb.toString());

        } catch (Exception e) {
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