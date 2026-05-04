package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.List;

public class ListSpecialtiesCommand extends BaseCommand {
    public ListSpecialtiesCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            requireFileOpen();

            List<Specialty> specialties = repository.getAllSpecialties();

            if (specialties.isEmpty()) {
                return CommandResult.success("📭 No specialties found. Use 'addspecialty' to add one.");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\n┌─────────────────────────────────────────────────────────────┐\n");
            sb.append("│                    📚 SPECIALTIES LIST                      │\n");
            sb.append("├─────────────────────────────────────────────────────────────┤\n");

            for (int i = 0; i < specialties.size(); i++) {
                Specialty specialty = specialties.get(i);
                sb.append(String.format("│ %-2d │ %-35s │ %8d credits │\n",
                        (i + 1), truncate(specialty.getName(), 35), specialty.getMinElectiveCredits()));
            }

            sb.append("└─────────────────────────────────────────────────────────────┘\n");
            sb.append("Total: ").append(specialties.size()).append(" specialty(ies)\n");

            return CommandResult.success(sb.toString());

        } catch (IllegalStateException e) {
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
