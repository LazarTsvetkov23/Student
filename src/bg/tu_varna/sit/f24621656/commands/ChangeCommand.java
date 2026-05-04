package bg.tu_varna.sit.f24621656.commands;

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
            requireFileOpen();

            String facultyNumber = args[1];
            String option = args[2].toLowerCase();

            Student student = repository.findStudentByFacultyNumber(facultyNumber);
            if (student == null) {
                return CommandResult.error("❌ Student with faculty number " + facultyNumber + " not found");
            }

            switch (option) {
                case "program": {
                    StringBuilder programBuilder = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        if (i > 3) {
                            programBuilder.append(" ");
                        }
                        programBuilder.append(args[i]);
                    }
                    String newProgram = programBuilder.toString();

                    Specialty newSpecialty = repository.findSpecialtyByName(newProgram);
                    if (newSpecialty == null) {
                        return CommandResult.error("❌ Specialty '" + newProgram + "' does not exist");
                    }

                    student.setSpecialty(newSpecialty);
                    session.setHasUnsavedChanges(true);
                    return CommandResult.success("✅ Student " + facultyNumber + " changed specialty to " + newProgram);
                }
                case "group": {
                    int newGroup;
                    try {
                        newGroup = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        return CommandResult.error("❌ Group must be a number");
                    }
                    student.setGroup(newGroup);
                    session.setHasUnsavedChanges(true);
                    return CommandResult.success("✅ Student " + facultyNumber + " changed group to " + newGroup);
                }
                case "year": {
                    int newYear;
                    try {
                        newYear = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        return CommandResult.error("❌ Year must be a number");
                    }
                    if (newYear != student.getCourse() + 1) {
                        return CommandResult.error("❌ Can only change to next course (year " + (student.getCourse() + 1) + ")");
                    }
                    if (!student.canAdvance()) {
                        return CommandResult.error("❌ Student cannot advance to next course (too many failed mandatory subjects)");
                    }
                    student.setCourse(newYear);
                    session.setHasUnsavedChanges(true);
                    return CommandResult.success("✅ Student " + facultyNumber + " changed year to " + newYear);
                }
                default:
                    return CommandResult.error("❌ Invalid option. Use: program, group, or year");
            }
        } catch (IllegalStateException e) {
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