package bg.tu_varna.sit.f24621656.commands.student;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class AdvanceCommand extends BaseCommand {
    public AdvanceCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: advance <fn>");
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

            if (!student.canAdvance()) {
                return CommandResult.error("Student cannot advance to next course (too many failed mandatory subjects)");
            }

            int oldCourse = student.getCourse();
            student.setCourse(oldCourse + 1);
            getSession().setHasUnsavedChanges(true);

            return CommandResult.success("Student " + student.getName() + " advanced from course " + oldCourse + " to " + student.getCourse());

        } catch (Exception e) {
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