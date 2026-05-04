package bg.tu_varna.sit.f24621656.commands;

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
                return CommandResult.error("Usage: enroll <fn> <program> <group> <name>");
            }
            requireFileOpen();

            String facultyNumber = args[1];

            int groupIndex = -1;
            for (int i = 2; i < args.length; i++) {
                try {
                    Integer.parseInt(args[i]);
                    groupIndex = i;
                    break;
                } catch (NumberFormatException e) {
                    // continue
                }
            }

            if (groupIndex == -1) {
                return CommandResult.error("❌ Cannot find group number");
            }

            StringBuilder programBuilder = new StringBuilder();
            for (int i = 2; i < groupIndex; i++) {
                if (i > 2) {
                    programBuilder.append(" ");
                }
                programBuilder.append(args[i]);
            }
            String programName = programBuilder.toString();

            int group;
            try {
                group = Integer.parseInt(args[groupIndex]);
            } catch (NumberFormatException e) {
                return CommandResult.error("❌ Group must be a number");
            }

            StringBuilder nameBuilder = new StringBuilder();
            for (int i = groupIndex + 1; i < args.length; i++) {
                if (i > groupIndex + 1) {
                    nameBuilder.append(" ");
                }
                nameBuilder.append(args[i]);
            }
            String name = nameBuilder.toString();

            if (name.trim().isEmpty()) {
                return CommandResult.error("❌ Student name cannot be empty");
            }

            if (repository.findStudentByFacultyNumber(facultyNumber) != null) {
                return CommandResult.error("❌ Student with faculty number " + facultyNumber + " already exists");
            }

            Specialty specialty = repository.findSpecialtyByName(programName);
            if (specialty == null) {
                return CommandResult.error("❌ Specialty '" + programName + "' does not exist");
            }

            Student student = new Student(name, facultyNumber, 1, specialty, group);
            repository.addStudent(student);
            session.setHasUnsavedChanges(true);

            return CommandResult.success("✅ Enrolled student: " + name + " (faculty number: " + facultyNumber + ") in " + programName);

        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "enroll <fn> <program> <group> <name>";
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