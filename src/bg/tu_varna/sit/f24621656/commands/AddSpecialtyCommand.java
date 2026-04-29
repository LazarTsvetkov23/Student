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
            if (args.length != 3) {
                return CommandResult.error("Usage: addspecialty <name> <minCredits>");
            }
            requireFileOpen();

            // Последният аргумент е minCredits
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

            // Всички аргументи от 1 до предпоследния са името на специалността
            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i < args.length - 1; i++) {
                if (i > 1) {
                    nameBuilder.append(" ");
                }
                nameBuilder.append(args[i]);
            }
            String rawName = nameBuilder.toString();

            // Премахване на кавички, ако има
            String name = removeQuotes(rawName);

            if (name == null || name.trim().isEmpty()) {
                return CommandResult.error("Specialty name cannot be empty");
            }

            if(isOnlyDigits(name)) {
                return CommandResult.error("Specialty name cannot contain only digits");
            }

            if (repository.findSpecialtyByName(name) != null) {
                return CommandResult.error("Specialty already exists: " + name);
            }

            if (!isValidSpecialtyName(name)) {
                return CommandResult.error("Specialty name contains invalid characters. Use letters, digits, spaces, hyphens or dots");
            }

            Specialty specialty = new Specialty(name, minCredits);
            repository.addSpecialty(specialty);
            session.setHasUnsavedChanges(true);

            return CommandResult.success("Added specialty: " + name + " (min credits: " + minCredits + ")");

        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    private String removeQuotes(String text) {
        if(text == null) {
            return null;
        }

        String trimmed = text.trim();

        // Ако започва и завършва с кавички, премахваме ги
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            String result = trimmed.substring(1, trimmed.length() - 1);
            // Ако след премахване на кавичките остане празен низ
            if (result.trim().isEmpty()) {
                return null;
            }
            return result;
        }

        return trimmed;
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

    private boolean isValidSpecialtyName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        for (char c : name.toCharArray()) {
            // Позволени символи:
            // - Букви (латински и български)
            // - Цифри
            // - Интервал (space)
            // - Тире (-)
            // - Точка (.)
            // - Амперсанд (&)
            if (Character.isLetterOrDigit(c) || c == ' ' || c == '-' || c == '.' || c == '&') {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return "addspecialty <name> <minCredits>";
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