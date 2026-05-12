package bg.tu_varna.sit.f24621656.commands.specialty;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
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
