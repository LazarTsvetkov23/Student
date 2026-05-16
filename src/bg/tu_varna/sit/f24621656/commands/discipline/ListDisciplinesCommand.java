package bg.tu_varna.sit.f24621656.commands.discipline;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.List;

/**
 * Lists all disciplines in the system.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class ListDisciplinesCommand extends BaseCommand {
    /**
     * Constructs a ListDisciplinesCommand with the given session.
     *
     * @param session the current session
     */
    public ListDisciplinesCommand(Session session) {
        super(session);
    }

    /**
     * Executes the list disciplines command.
     * Displays each discipline's name, type, credits, and course.
     *
     * @param args no arguments expected
     * @return CommandResult with the list or an empty message
     */
    @Override
    public CommandResult execute(String[] args) {
        try {
            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            List<Discipline> disciplines = getRepository().getAllDisciplines();
            if (disciplines.isEmpty()) {
                return CommandResult.success("No disciplines found.");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\nDisciplines List:\n");
            sb.append("----------------\n");
            for (int i = 0; i < disciplines.size(); i++) {
                Discipline discipline = disciplines.get(i);
                sb.append(i + 1).append(". ").append(discipline.getName())
                        .append(" (").append(discipline.getType())
                        .append(", credits: ").append(discipline.getCredits())
                        .append(", course: ").append(discipline.getCourse())
                        .append(")\n");
            }
            return CommandResult.success(sb.toString());

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "listdisciplines";
    }

    @Override
    public String getDescription() {
        return "Lists all disciplines";
    }

    @Override
    public String getName() {
        return "listdisciplines";
    }
}
