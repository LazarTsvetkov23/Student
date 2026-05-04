package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class InterruptCommand extends BaseCommand {
    public InterruptCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            validateArgs(args, 2);
            requireFileOpen();

            String facultyNumber = args[1];
            Student student = repository.findStudentByFacultyNumber(facultyNumber);

            if (student == null) {
                return CommandResult.error("Student with FN " + facultyNumber + " not found");
            }

            if (student.getStatus() != StudentStatus.ENROLLED) {
                return CommandResult.error("Student is not currently enrolled");
            }

            student.setStatus(StudentStatus.INTERRUPTED);
            session.setHasUnsavedChanges(true);

            return CommandResult.success(String.format("⏸ Student %s (FN: %s) has been interrupted", student.getName(), facultyNumber));

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() { return "interrupt <fn>"; }

    @Override
    public String getDescription() { return "Interrupts a student's studies"; }

    @Override
    public String getName() { return "interrupt"; }
}