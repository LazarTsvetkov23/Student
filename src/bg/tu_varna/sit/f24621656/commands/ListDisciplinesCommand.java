package bg.tu_varna.sit.f24621656.commands;

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
            requireFileOpen();

            List<Discipline> disciplines = repository.getAllDisciplines();

            if (disciplines.isEmpty()) {
                return CommandResult.success("📭 No disciplines found. Use 'adddiscipline' to add one.");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\n┌─────────────────────────────────────────────────────────────────────┐\n");
            sb.append("│                         📚 DISCIPLINES LIST                         │\n");
            sb.append("├─────────────────────────────────────────────────────────────────────┤\n");

            for (int i = 0; i < disciplines.size(); i++) {
                Discipline discipline = disciplines.get(i);
                StringBuilder courses = new StringBuilder();
                for (int j = 0; j < discipline.getAvailableCourses().size(); j++) {
                    if (j > 0) {
                        courses.append(",");
                    }
                    courses.append(discipline.getAvailableCourses().get(j));
                }
                sb.append(String.format("│ %-3d │ %-25s │ %-10s │ %4d │ %-10s │\n",
                        (i + 1), truncate(discipline.getName(), 25), discipline.getType(), discipline.getCredits(), courses));
            }

            sb.append("└─────────────────────────────────────────────────────────────────────┘\n");
            sb.append("Total: ").append(disciplines.size()).append(" discipline(s)\n");

            return CommandResult.success(sb.toString());

        } catch (IllegalStateException e) {
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
