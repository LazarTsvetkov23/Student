package bg.tu_varna.sit.f24621656.commands.student;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class ChangeCommand extends BaseCommand {
    public ChangeCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 4) {
                return CommandResult.error("Usage: change <fn> <option> <value>");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            String fn = args[1];
            String option = args[2].toLowerCase();

            Student student = getRepository().findStudentByFacultyNumber(fn);
            if (student == null) {
                return CommandResult.error("Student with FN " + fn + " not found");
            }

            if (student.getStatus() != bg.tu_varna.sit.f24621656.enums.StudentStatus.ENROLLED) {
                return CommandResult.error("Student is not enrolled. Current status: " + student.getStatus());
            }

            switch (option) {
                case "program": {
                    StringBuilder programBuilder = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        if (i > 3) programBuilder.append(" ");
                        programBuilder.append(args[i]);
                    }
                    String newProgram = programBuilder.toString();

                    if (newProgram.startsWith("\"") && newProgram.endsWith("\"")) {
                        newProgram = newProgram.substring(1, newProgram.length() - 1);
                    }

                    Specialty newSpecialty = getRepository().findSpecialtyByName(newProgram);
                    if (newSpecialty == null) {
                        return CommandResult.error("Specialty '" + newProgram + "' does not exist");
                    }

                    student.setSpecialty(newSpecialty);
                    getSession().setHasUnsavedChanges(true);
                    return CommandResult.success("Student " + fn + " changed specialty to " + newProgram);
                }
                case "group": {
                    int newGroup;
                    try {
                        newGroup = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        return CommandResult.error("Group must be a number");
                    }
                    student.setGroup(newGroup);
                    getSession().setHasUnsavedChanges(true);
                    return CommandResult.success("Student " + fn + " changed group to " + newGroup);
                }
                case "year": {
                    int newYear;
                    try {
                        newYear = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        return CommandResult.error("Year must be a number");
                    }
                    if (newYear != student.getCourse() + 1) {
                        return CommandResult.error("Can only change to next course (year " + (student.getCourse() + 1) + ")");
                    }
                    if (!student.canAdvance()) {
                        return CommandResult.error("Student cannot advance to next course (too many failed mandatory subjects)");
                    }
                    student.setCourse(newYear);
                    getSession().setHasUnsavedChanges(true);
                    return CommandResult.success("Student " + fn + " changed year to " + newYear);
                }
                default:
                    return CommandResult.error("Invalid option. Use: program, group, or year");
            }

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "change <fn> <option> <value>";
    }

    @Override
    public String getDescription() {
        return "Changes student's program, group, or year";
    }

    @Override
    public String getName() {
        return "change";
    }
}