package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class RemoveSpecialtyCommand extends BaseCommand {
    public RemoveSpecialtyCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: removespecialty \"<name>\"");
            }
            requireFileOpen();

            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i > 1) {
                    nameBuilder.append(" ");
                }
                nameBuilder.append(args[i]);
            }
            String rawName = nameBuilder.toString();

            if (!rawName.startsWith("\"") || !rawName.endsWith("\"")) {
                return CommandResult.error("❌ Specialty name must be enclosed in quotes: \"<name>\"");
            }

            String name = rawName.substring(1, rawName.length() - 1);

            Specialty specialty = repository.findSpecialtyByName(name);
            if (specialty == null) {
                return CommandResult.error("❌ Specialty not found: " + name);
            }

            for (Student student : repository.getAllStudents()) {
                if (student.getSpecialty().equals(specialty)) {
                    return CommandResult.error("❌ Cannot remove specialty: There are students enrolled in " + name);
                }
            }

            repository.removeSpecialty(specialty);
            session.setHasUnsavedChanges(true);

            return CommandResult.success("✅ Removed specialty: " + name);

        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "removespecialty \"<name>\"";
    }

    @Override
    public String getDescription() {
        return "Removes a specialty (if no students enrolled)";
    }

    @Override
    public String getName() {
        return "removespecialty";
    }
}
