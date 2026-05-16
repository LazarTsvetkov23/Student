package bg.tu_varna.sit.f24621656.commands.specialty;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

/**
 * Removes a specialty from the system.
 * Fails if there are any students enrolled in that specialty.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class RemoveSpecialtyCommand extends BaseCommand {
    /**
     * Constructs a RemoveSpecialtyCommand with the given session.
     *
     * @param session the current session
     */
    public RemoveSpecialtyCommand(Session session) {
        super(session);
    }

    /**
     * Executes the remove specialty command.
     * Expects: removespecialty "<name>"
     *
     * @param args command arguments
     * @return CommandResult indicating success or error
     */
    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: removespecialty \"<name>\"");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i > 1) {
                    nameBuilder.append(" ");
                }
                nameBuilder.append(args[i]);
            }
            String rawName = nameBuilder.toString();

            if (!rawName.startsWith("\"") || !rawName.endsWith("\"")) {
                return CommandResult.error("Specialty name must be enclosed in quotes: \"<name>\"");
            }

            String name = rawName.substring(1, rawName.length() - 1);

            Specialty specialty = getRepository().findSpecialtyByName(name);
            if (specialty == null) {
                return CommandResult.error("Specialty not found: " + name);
            }

            for (Student student : getRepository().getAllStudents()) {
                if (student.getSpecialty().equals(specialty)) {
                    return CommandResult.error("Cannot remove specialty: There are students enrolled in " + name);
                }
            }

            getRepository().removeSpecialty(specialty);
            getSession().setHasUnsavedChanges(true);
            return CommandResult.success("Removed specialty: " + name);

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "removespecialty \"<name>\"";
    }

    @Override
    public String getDescription() {
        return "Removes a specialty";
    }

    @Override
    public String getName() {
        return "removespecialty";
    }
}