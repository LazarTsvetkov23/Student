package bg.tu_varna.sit.f24621656.commands.specialty;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
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

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            int creditsIndex = -1;
            for (int i = args.length - 1; i >= 1; i--) {
                try {
                    Integer.parseInt(args[i]);
                    creditsIndex = i;
                    break;
                } catch (NumberFormatException e) {
                    continue;
                }
            }

            if (creditsIndex == -1) {
                return CommandResult.error("Min credits must be a number (e.g., 20)");
            }

            int minCredits = Integer.parseInt(args[creditsIndex]);

            if (minCredits < 0) {
                return CommandResult.error("Min credits cannot be negative");
            }

            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i < creditsIndex; i++) {
                if (i > 1) {
                    nameBuilder.append(" ");
                }
                nameBuilder.append(args[i]);
            }
            String rawName = nameBuilder.toString();

            if (!rawName.startsWith("\"") || !rawName.endsWith("\"")) {
                return CommandResult.error("Specialty name must be enclosed in quotes: \"<name>\"");
            }

            String name = rawName.substring(1, rawName.length() - 1).trim();

            if (name.isEmpty()) {
                return CommandResult.error("Specialty name cannot be empty");
            }

            if (getRepository().findSpecialtyByName(name) != null) {
                return CommandResult.error("Specialty already exists: " + name);
            }

            Specialty specialty = new Specialty(name, minCredits);
            getRepository().addSpecialty(specialty);
            getSession().setHasUnsavedChanges(true);

            return CommandResult.success("Added specialty: " + name + " (min credits: " + minCredits + ")");

        } catch (Exception e) {
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