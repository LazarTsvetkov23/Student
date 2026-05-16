package bg.tu_varna.sit.f24621656.commands.student;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

/**
 * Interrupts a student's studies (sets status to INTERRUPTED).
 * Interrupted students cannot enroll in disciplines, receive grades, or change program/group/year.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class InterruptCommand extends BaseCommand {
    /**
     * Constructs an InterruptCommand with the given session.
     *
     * @param session the current session
     */
    public InterruptCommand(Session session) {
        super(session);
    }

    /**
     * Executes the interrupt command.
     * Expects: interrupt <fn>
     *
     * @param args command arguments
     * @return CommandResult indicating success or error
     */
    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: interrupt <fn>");
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
                return CommandResult.error("Student is not enrolled. Current status: " + student.getStatus());
            }

            student.setStatus(StudentStatus.INTERRUPTED);
            getSession().setHasUnsavedChanges(true);

            return CommandResult.success("Student " + student.getName() + " (FN: " + fn + ") has been interrupted");

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "interrupt <fn>";
    }

    @Override
    public String getDescription() {
        return "Interrupts a student's studies";
    }

    @Override
    public String getName() {
        return "interrupt";
    }
}