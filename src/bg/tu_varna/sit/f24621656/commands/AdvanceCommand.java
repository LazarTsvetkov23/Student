package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class AdvanceCommand extends BaseCommand {
    public AdvanceCommand(Session session) {
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

            if (!student.canAdvance()) {
                return CommandResult.error("Student cannot advance to next course (too many failed mandatory subjects)");
            }

            int oldCourse = student.getCourse();
            student.setCourse(oldCourse + 1);
            session.setHasUnsavedChanges(true);

            return CommandResult.success(String.format("Student %s advanced from course %d to %d", student.getName(), oldCourse, student.getCourse()));

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "advance <fn>";
    }

    @Override
    public String getDescription() {
        return "Advances a student to the next course";
    }

    @Override
    public String getName() {
        return "advance";
    }
}