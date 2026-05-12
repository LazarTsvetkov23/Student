package bg.tu_varna.sit.f24621656.commands.student;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class GraduateCommand extends BaseCommand {
    public GraduateCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: graduate <fn>");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            String fn = args[1];
            Student student = getRepository().findStudentByFacultyNumber(fn);

            if (student == null) {
                return CommandResult.error("Student with FN " + fn + " not found");
            }

            if (student.getStatus() != StudentStatus.ENROLLED) {
                return CommandResult.error("Student is not enrolled. Status: " + student.getStatus());
            }

            if (!student.canGraduate()) {
                String reason = "";
                if (student.getCourse() < 4) {
                    reason = "Student is not in 4th year (current: " + student.getCourse() + ")";
                } else if (student.getRemainingElectiveCredits() > 0) {
                    reason = "Missing " + student.getRemainingElectiveCredits() + " elective credits";
                } else {
                    reason = "Not all enrolled disciplines are passed";
                }
                return CommandResult.error("Cannot graduate: " + reason);
            }

            student.setStatus(StudentStatus.GRADUATED);
            getSession().setHasUnsavedChanges(true);

            return CommandResult.success("Student " + student.getName() + " (FN: " + fn + ") has graduated!");

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "graduate <fn>";
    }

    @Override
    public String getDescription() {
        return "Marks a student as graduated";
    }

    @Override
    public String getName() {
        return "graduate";
    }
}