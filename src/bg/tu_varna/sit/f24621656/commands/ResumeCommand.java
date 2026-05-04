package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class ResumeCommand extends BaseCommand {
    public ResumeCommand(Session session) {
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
                return CommandResult.error("❌ Student with faculty number " + facultyNumber + " not found");
            }

            if (student.getStatus() != StudentStatus.INTERRUPTED) {
                return CommandResult.error("❌ Student is not interrupted");
            }

            student.setStatus(StudentStatus.ENROLLED);
            session.setHasUnsavedChanges(true);

            return CommandResult.success(String.format("▶ Student %s (faculty number: %s) has been resumed", student.getName(), facultyNumber));

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
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
