package bg.tu_varna.sit.f24621656.commands.student;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class EnrollCommand extends BaseCommand {
    public EnrollCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 5) {
                return CommandResult.error("Usage: enroll <fn> \"<program>\" <group> \"<name>\"");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            String fn = args[1];

            int groupIndex = -1;
            for (int i = args.length - 1; i >= 2; i--) {
                try {
                    Integer.parseInt(args[i]);
                    groupIndex = i;
                    break;
                } catch (NumberFormatException e) {
                    continue;
                }
            }

            if (groupIndex == -1 || groupIndex == args.length - 1) {
                return CommandResult.error("Cannot find group number or student name is missing");
            }

            StringBuilder programBuilder = new StringBuilder();
            for (int i = 2; i < groupIndex; i++) {
                if (i > 2) {
                    programBuilder.append(" ");
                }
                programBuilder.append(args[i]);
            }
            String programName = programBuilder.toString();

            if (programName.startsWith("\"") && programName.endsWith("\"")) {
                programName = programName.substring(1, programName.length() - 1);
            }

            int group;
            try {
                group = Integer.parseInt(args[groupIndex]);
            } catch (NumberFormatException e) {
                return CommandResult.error("Group must be a number");
            }

            if (group <= 0) {
                return CommandResult.error("Group must be a positive number");
            }

            StringBuilder nameBuilder = new StringBuilder();
            for (int i = groupIndex + 1; i < args.length; i++) {
                if (i > groupIndex + 1) {
                    nameBuilder.append(" ");
                }
                nameBuilder.append(args[i]);
            }
            String name = nameBuilder.toString();

            if (name.startsWith("\"") && name.endsWith("\"")) {
                name = name.substring(1, name.length() - 1);
            }

            if (name.trim().isEmpty()) {
                return CommandResult.error("Student name cannot be empty");
            }

            if (getRepository().findStudentByFacultyNumber(fn) != null) {
                return CommandResult.error("Student with FN " + fn + " already exists");
            }

            Specialty specialty = getRepository().findSpecialtyByName(programName);
            if (specialty == null) {
                return CommandResult.error("Specialty '" + programName + "' does not exist");
            }

            Student student = new Student(name, fn, 1, specialty, group);
            getRepository().addStudent(student);
            getSession().setHasUnsavedChanges(true);

            return CommandResult.success("Enrolled student: " + name + " (FN: " + fn + ") in " + programName);

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "enroll <fn> \"<program>\" <group> \"<name>\"";
    }

    @Override
    public String getDescription() {
        return "Enrolls a new student in first year";
    }

    @Override
    public String getName() {
        return "enroll";
    }
}