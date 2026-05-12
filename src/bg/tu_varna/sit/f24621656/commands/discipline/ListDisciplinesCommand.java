package bg.tu_varna.sit.f24621656.commands.discipline;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.List;

public class ListDisciplinesCommand extends BaseCommand {
    public ListDisciplinesCommand(Session session) {
        super(session);
    }

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
                Discipline d = disciplines.get(i);
                sb.append(i + 1).append(". ").append(d.getName())
                        .append(" (").append(d.getType())
                        .append(", credits: ").append(d.getCredits())
                        .append(", courses: ");
                for (int j = 0; j < d.getAvailableCourses().size(); j++) {
                    if (j > 0) sb.append(",");
                    sb.append(d.getAvailableCourses().get(j));
                }
                sb.append(")\n");
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
