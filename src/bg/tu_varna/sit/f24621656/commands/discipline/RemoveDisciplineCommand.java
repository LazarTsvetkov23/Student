package bg.tu_varna.sit.f24621656.commands.discipline;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

/**
 * Removes a discipline from the system.
 * Fails if there are any students enrolled in that discipline.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class RemoveDisciplineCommand extends BaseCommand {
    /**
     * Constructs a RemoveDisciplineCommand with the given session.
     *
     * @param session the current session
     */
    public RemoveDisciplineCommand(Session session) {
        super(session);
    }

    /**
     * Executes the remove discipline command.
     * Expects: removediscipline "<name>"
     *
     * @param args command arguments
     * @return CommandResult indicating success or error
     */
    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: removediscipline \"<name>\"");
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
            String name = nameBuilder.toString();

            if (name.startsWith("\"") && name.endsWith("\"")) {
                name = name.substring(1, name.length() - 1);
            }

            Discipline discipline = getRepository().findDisciplineByName(name);
            if (discipline == null) {
                return CommandResult.error("Discipline not found: " + name);
            }

            for (Student student : getRepository().getAllStudents()) {
                if (student.getEnrolledDisciplines().contains(discipline)) {
                    return CommandResult.error("Cannot remove discipline: There are students enrolled in " + name);
                }
            }

            getRepository().removeDiscipline(discipline);
            for (bg.tu_varna.sit.f24621656.models.Specialty specialty : getRepository().getAllSpecialties()) {
                specialty.getDisciplines().remove(discipline);
            }
            getSession().setHasUnsavedChanges(true);
            return CommandResult.success("Removed discipline: " + name);

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "removediscipline \"<name>\"";
    }

    @Override
    public String getDescription() {
        return "Removes a discipline";
    }

    @Override
    public String getName() {
        return "removediscipline";
    }
}
