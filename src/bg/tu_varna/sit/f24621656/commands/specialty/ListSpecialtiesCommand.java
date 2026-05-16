package bg.tu_varna.sit.f24621656.commands.specialty;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.List;

/**
 * Lists all specialties currently in the system.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class ListSpecialtiesCommand extends BaseCommand {
    /**
     * Constructs a ListSpecialtiesCommand with the given session.
     *
     * @param session the current session
     */
    public ListSpecialtiesCommand(Session session) {
        super(session);
    }

    /**
     * Executes the list specialties command.
     * Displays each specialty with its minimum elective credits.
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

            List<Specialty> specialties = getRepository().getAllSpecialties();
            if (specialties.isEmpty()) {
                return CommandResult.success("No specialties found.");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\nSpecialties List:\n");
            sb.append("----------------\n");
            for (int i = 0; i < specialties.size(); i++) {
                Specialty specialty = specialties.get(i);
                sb.append(i + 1).append(". ").append(specialty.getName())
                        .append(" (min credits: ").append(specialty.getMinElectiveCredits()).append(")\n");
            }
            return CommandResult.success(sb.toString());

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "listspecialties";
    }

    @Override
    public String getDescription() {
        return "Lists all specialties";
    }

    @Override
    public String getName() {
        return "listspecialties";
    }
}
