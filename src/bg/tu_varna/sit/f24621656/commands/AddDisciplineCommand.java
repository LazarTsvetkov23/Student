package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.enums.DisciplineType;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.session.Session;

public class AddDisciplineCommand extends BaseCommand {
    public AddDisciplineCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 5) {
                return CommandResult.error("Usage: adddiscipline <name> <type> <credits> <courses>");
            }
            requireFileOpen();

            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i < args.length - 3; i++) {
                if (i > 1) nameBuilder.append(" ");
                nameBuilder.append(args[i]);
            }
            String name = nameBuilder.toString();

            if (name.trim().isEmpty()) {
                return CommandResult.error("Discipline name cannot be empty");
            }

            DisciplineType type;
            try {
                type = DisciplineType.valueOf(args[args.length - 3].toUpperCase());
            } catch (IllegalArgumentException e) {
                return CommandResult.error("Type must be MANDATORY or ELECTIVE");
            }

            int credits;
            try {
                credits = Integer.parseInt(args[args.length - 2]);
            } catch (NumberFormatException e) {
                return CommandResult.error("Credits must be a number");
            }

            if (type == DisciplineType.MANDATORY && credits != 0) {
                return CommandResult.error("Mandatory disciplines must have 0 credits");
            }

            String coursesStr = args[args.length - 1];
            if (coursesStr.startsWith("\"") && coursesStr.endsWith("\"")) {
                coursesStr = coursesStr.substring(1, coursesStr.length() - 1);
            }

            if (repository.findDisciplineByName(name) != null) {
                return CommandResult.error("Discipline already exists: " + name);
            }

            Discipline discipline = new Discipline(name, type);
            discipline.setCredits(credits);

            String[] courses = coursesStr.split(",");
            for (String c : courses) {
                try {
                    discipline.addAvailableCourse(Integer.parseInt(c.trim()));
                } catch (NumberFormatException e) {
                    return CommandResult.error("Invalid course number: " + c);
                }
            }

            if (discipline.getAvailableCourses().isEmpty()) {
                return CommandResult.error("At least one course must be specified");
            }

            repository.addDiscipline(discipline);
            session.setHasUnsavedChanges(true);

            return CommandResult.success("Added discipline: " + name + " (" + type + ", credits: " + credits + ", courses: " + coursesStr + ")");

        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "adddiscipline <name> <type> <credits> <courses>";
    }

    @Override
    public String getDescription() {
        return "Adds a new discipline";
    }

    @Override
    public String getName() {
        return "adddiscipline";
    }
}
