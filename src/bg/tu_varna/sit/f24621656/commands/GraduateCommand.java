package bg.tu_varna.sit.f24621656.commands;

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
            validateArgs(args, 2);
            requireFileOpen();

            String fn = args[1];
            Student student = repository.findStudentByFacultyNumber(fn);

            if (student == null) {
                return CommandResult.error("Student with FN " + fn + " not found");
            }

            if (!student.canGraduate()) {
                return CommandResult.error("Student cannot graduate (not all mandatory or enrolled subjects are passed)");
            }

            student.setStatus(StudentStatus.GRADUATED);
            session.setHasUnsavedChanges(true);

            return CommandResult.success(String.format("Student %s (FN: %s) has graduated!", student.getName(), fn));

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
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