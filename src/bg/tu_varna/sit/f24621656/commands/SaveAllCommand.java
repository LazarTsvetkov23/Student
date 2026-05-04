package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;

public class SaveAllCommand extends BaseCommand {
    public SaveAllCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            validateArgs(args, 1);
            requireFileOpen();

            StringBuilder resultMessage = new StringBuilder();

            XmlFileManager.saveSpecialties(repository);
            resultMessage.append("  ✓ specialties.xml\n");

            XmlFileManager.saveDisciplines(repository);
            resultMessage.append("  ✓ disciplines.xml\n");

            XmlFileManager.saveStudents(repository);
            resultMessage.append("  ✓ students.xml\n");

            session.setHasUnsavedChanges(false);

            return CommandResult.success("💾 Saved all files:\n" + resultMessage.toString());

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        } catch (IOException e) {
            return CommandResult.error("❌ Error saving files: " + e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "saveall";
    }

    @Override
    public String getDescription() {
        return "Saves all files (creates them if not exists)";
    }

    @Override
    public String getName() {
        return "saveall";
    }
}
