package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;

//WORKED

public class SaveAllCommand extends BaseCommand {
    public SaveAllCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length > 1) {
                return CommandResult.error("Usage: saveall (no arguments)");
            }
            requireFileOpen();

            XmlFileManager.saveSpecialties(repository);
            XmlFileManager.saveDisciplines(repository);
            XmlFileManager.saveStudents(repository);

            session.setHasUnsavedChanges(false);

            return CommandResult.success("Saved all files:\n  ✓ specialties.xml\n  ✓ disciplines.xml\n  ✓ students.xml");

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        } catch (IOException e) {
            return CommandResult.error("Error saving files: " + e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "saveall";
    }

    @Override
    public String getDescription() {
        return "Saves all files: specialties.xml, disciplines.xml, students.xml";
    }

    @Override
    public String getName() {
        return "saveall";
    }
}
