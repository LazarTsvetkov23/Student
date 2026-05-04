package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Student;
import bg.tu_varna.sit.f24621656.session.Session;

public class RemoveDisciplineCommand extends BaseCommand {
    public RemoveDisciplineCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: removediscipline <name>");
            }
            requireFileOpen();

            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i > 1) nameBuilder.append(" ");
                nameBuilder.append(args[i]);
            }
            String name = nameBuilder.toString();

            Discipline discipline = repository.findDisciplineByName(name);
            if (discipline == null) {
                return CommandResult.error("Discipline not found: " + name);
            }

            for (Student student : repository.getAllStudents()) {
                if (student.getEnrolledDisciplines().contains(discipline)) {
                    return CommandResult.error("Cannot remove discipline: There are students enrolled in " + name);
                }
            }

            repository.removeDiscipline(discipline);
            session.setHasUnsavedChanges(true);

            return CommandResult.success("Removed discipline: " + name);

        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "removediscipline <name>";
    }

    @Override
    public String getDescription() {
        return "Removes a discipline (if no students enrolled)";
    }

    @Override
    public String getName() {
        return "removediscipline";
    }
}
