package bg.tu_varna.sit.f24621656.commands.student;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

/**
 * Resumes an interrupted student (sets status back to ENROLLED).
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class ResumeCommand extends BaseCommand {
    /**
     * Constructs a ResumeCommand with the given session.
     *
     * @param session the current session
     */
    public ResumeCommand(Session session) {
        super(session);
    }

    /**
     * Executes the resume command.
     * Expects: resume <fn>
     *
     * @param args command arguments
     * @return CommandResult indicating success or error
     */
    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: resume <fn>");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            String fn = args[1];
            Student student = getRepository().findStudentByFacultyNumber(fn);

            if (student == null) {
                return CommandResult.error("Student with FN " + fn + " not found");
            }

            if (student.getStatus() != StudentStatus.INTERRUPTED) {
                return CommandResult.error("Student is not interrupted. Current status: " + student.getStatus());
            }

            student.setStatus(StudentStatus.ENROLLED);
            getSession().setHasUnsavedChanges(true);

            return CommandResult.success("Student " + student.getName() + " (FN: " + fn + ") has been resumed");

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "resume <fn>";
    }

    @Override
    public String getDescription() {
        return "Resumes an interrupted student";
    }

    @Override
    public String getName() {
        return "resume";
    }
}