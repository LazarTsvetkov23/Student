package bg.tu_varna.sit.f24621656.commands.discipline;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.enums.DisciplineType;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.HashSet;
import java.util.Set;

public class AddDisciplineCommand extends BaseCommand {
    public AddDisciplineCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 5) {
                return CommandResult.error("Usage: adddiscipline \"<name>\" <type> <credits> (<course> or \"<courses>\")");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            int typeIndex = args.length - 3;
            int creditsIndex = args.length - 2;
            int coursesIndex = args.length - 1;

            String name = extractDisciplineName(args, typeIndex);
            if (name == null || name.trim().isEmpty()) {
                return CommandResult.error("Discipline name cannot be empty");
            }

            DisciplineType type = parseDisciplineType(args[typeIndex]);
            if (type == null) {
                return CommandResult.error("Type must be MANDATORY or ELECTIVE");
            }

            Integer credits = parseCredits(args[creditsIndex]);
            if (credits == null) {
                return CommandResult.error("Credits must be a number");
            }

            String creditValidationError = validateCreditsByType(type, credits);
            if (creditValidationError != null) {
                return CommandResult.error(creditValidationError);
            }

            int course;
            try {
                course = Integer.parseInt(args[coursesIndex]);
            } catch (NumberFormatException e) {
                return CommandResult.error("Course must be a number between 1 and 4");
            }
            if (course < 1 || course > 4) {
                return CommandResult.error("Course must be between 1 and 4");
            }

            if (getRepository().findDisciplineByName(name) != null) {
                return CommandResult.error("Discipline already exists: " + name);
            }

            Discipline discipline = new Discipline(name, type, course);
            discipline.setCredits(credits);

            getRepository().addDiscipline(discipline);

            int addedToSpecialties = 0;
            for (Specialty specialty : getRepository().getAllSpecialties()) {
                specialty.addDiscipline(discipline);
                addedToSpecialties++;
            }

            getSession().setHasUnsavedChanges(true);

            String successMessage = String.format(
                    "Added discipline: %s (%s, credits: %d, course: %s) [added to %d specialty/ies]",
                    name, type, credits, course, addedToSpecialties
            );
            return CommandResult.success(successMessage);

        } catch (Exception e) {
            return CommandResult.error("Unexpected error: " + e.getMessage());
        }
    }

    private String extractDisciplineName(String[] args, int typeIndex) {
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 1; i < typeIndex; i++) {
            if (i > 1) {
                nameBuilder.append(" ");
            }
            nameBuilder.append(args[i]);
        }
        String name = nameBuilder.toString();

        if (name.startsWith("\"") && name.endsWith("\"")) {
            name = name.substring(1, name.length() - 1);
        }
        return name;
    }

    private DisciplineType parseDisciplineType(String typeStr) {
        try {
            return DisciplineType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Integer parseCredits(String creditsStr) {
        try {
            return Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String validateCreditsByType(DisciplineType type, int credits) {
        if (type == DisciplineType.MANDATORY && credits != 0) {
            return "Mandatory disciplines must have 0 credits";
        }
        if (type == DisciplineType.ELECTIVE && credits <= 0) {
            return "Elective disciplines must have positive credits";
        }
        return null;
    }

    @Override
    public String getUsage() {
        return "adddiscipline \"<name>\" <type> <credits> <course>";
    }

    @Override
    public String getDescription() {
        return "Adds a new discipline and automatically adds it to all existing specialties";
    }

    @Override
    public String getName() {
        return "adddiscipline";
    }
}