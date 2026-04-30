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

            String minCreditsStr = args[args.length - 1];

            int minCredits;
            try {
                minCredits = Integer.parseInt(minCreditsStr);
            } catch (NumberFormatException e) {
                return CommandResult.error("Min credits must be a number");
            }

            if (minCredits < 0) {
                return CommandResult.error("Min credits cannot be negative");
            }

            // Името трябва да е в кавички – събираме от args[1] до args[args.length-2]
            StringBuilder nameBuilder = new StringBuilder();

            for (int i = 1; i < args.length - 1; i++) {
                if (i > 1) {
                    nameBuilder.append(" ");
                }
                nameBuilder.append(args[i]);
            }
            String rawName = nameBuilder.toString();

            // Проверка за кавички
            if(!rawName.startsWith("\"") || !rawName.endsWith("\"")) {
                return CommandResult.error("Specialty name must be enclosed in quotes: \"<name>\"");
            }

            // Премахване на кавичките
            String name = rawName.substring(1, rawName.length() - 1);

            // Валидация на името
            if (name == null || name.trim().isEmpty()) {
                return CommandResult.error("Specialty name cannot be empty");
            }

            name = name.trim();

            if (name.length() < 2) {
                return CommandResult.error("Specialty name must be at least 2 characters long");
            }

            if (isOnlyDigits(name)) {
                return CommandResult.error("Specialty name cannot contain only digits");
            }

            // Проверка за съществуваща специалност
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