package bg.tu_varna.sit.f24621656.commands.file;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;

public class SaveCommand extends BaseCommand {
    public SaveCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length > 1) {
                return CommandResult.error("Usage: save (no arguments)");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            String currentFilePath = getSession().getCurrentFilePath();

            XmlFileManager.saveAllData(getRepository(), currentFilePath);

            getSession().setHasUnsavedChanges(false);
            return CommandResult.success("Successfully saved " + getFileName(currentFilePath));

        } catch (IOException e) {
            return CommandResult.error("Error saving file: " + e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "save";
    }

    @Override
    public String getDescription() {
        return "Saves the currently open file";
    }

    @Override
    public String getName() {
        return "save";
    }
}