package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.session.Session;

//WORKED

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

            int lastIndex = args.length - 1;

            int i = 1;
            while (i < lastIndex && !args[i].startsWith("\"")) {
                i++;
            }

            if (i >= lastIndex) {
                return CommandResult.error("Specialty name must be enclosed in quotes: \"<name>\"");
            }

            StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append(args[i]);
            if (!args[i].endsWith("\"")) {
                i++;
                while (i < lastIndex && !args[i].endsWith("\"")) {
                    nameBuilder.append(" ").append(args[i]);
                    i++;
                }
                if (i < lastIndex) {
                    nameBuilder.append(" ").append(args[i]);
                } else {
                    return CommandResult.error("Invalid quote format for specialty name");
                }
            }

            int afterQuoteIndex = i + 1;

            if (afterQuoteIndex > lastIndex) {
                return CommandResult.error("Missing minCredits. Usage: addspecialty \"<name>\" <minCredits>");
            }

            if (afterQuoteIndex + 1 < lastIndex) {
                return CommandResult.error("Too many arguments. Usage: addspecialty \"<name>\" <minCredits>");
            }

            int minCredits;
            try {
                minCredits = Integer.parseInt(args[afterQuoteIndex]);
            } catch (NumberFormatException e) {
                return CommandResult.error("Min credits must be a number (e.g., 20)");
            }

            if (minCredits < 0) {
                return CommandResult.error("Min credits cannot be negative");
            }

            if (afterQuoteIndex + 1 < args.length) {
                return CommandResult.error("Too many arguments. Usage: addspecialty \"<name>\" <minCredits>");
            }

            String rawName = nameBuilder.toString();

            if (!rawName.startsWith("\"") || !rawName.endsWith("\"")) {
                return CommandResult.error("Specialty name must be enclosed in quotes: \"<name>\"");
            }

            String name = rawName.substring(1, rawName.length() - 1).trim();

            if (name.isEmpty()) {
                return CommandResult.error("Specialty name cannot be empty");
            }

            if (isOnlyDigits(name)) {
                return CommandResult.error("Specialty name cannot contain only digits");
            }

            if (repository.findSpecialtyByName(name) != null) {
                return CommandResult.error("Specialty already exists: " + name);
            }

            Specialty specialty = new Specialty(name, minCredits);
            repository.addSpecialty(specialty);
            session.setHasUnsavedChanges(true);

            return CommandResult.success("Added specialty: " + name + " (min credits: " + minCredits + ")");

        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    private boolean isOnlyDigits(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        for (char c : text.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return "addspecialty \"<name>\" <minCredits>";
    }

    @Override
    public String getDescription() {
        return "Adds a new specialty (name must be in quotes)";
    }

    @Override
    public String getName() {
        return "addspecialty";
    }
}