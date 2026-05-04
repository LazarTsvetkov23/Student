package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.session.Session;

public class AddSpecialtyCommand extends BaseCommand {
    public AddSpecialtyCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 3) {
                return CommandResult.error("Usage: addspecialty \"<name>\" <minCredits>");
            }
            requireFileOpen();

            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i < args.length - 1; i++) {
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

            if (name.trim().isEmpty()) {
                return CommandResult.error("❌ Specialty name cannot be empty");
            }

            int minCredits;
            try {
                minCredits = Integer.parseInt(args[args.length - 1]);
            } catch (NumberFormatException e) {
                return CommandResult.error("❌ Min credits must be a number");
            }

            if (minCredits < 0) {
                return CommandResult.error("❌ Min credits cannot be negative");
            }

            if (repository.findSpecialtyByName(name) != null) {
                return CommandResult.error("❌ Specialty already exists: " + name);
            }

            Specialty specialty = new Specialty(name, minCredits);
            repository.addSpecialty(specialty);
            session.setHasUnsavedChanges(true);

            return CommandResult.success("✅ Added specialty: " + name + " (min credits: " + minCredits + ")");

        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "addspecialty \"<name>\" <minCredits>";
    }

    @Override
    public String getDescription() {
        return "Adds a new specialty";
    }

    @Override
    public String getName() {
        return "addspecialty";
    }
}