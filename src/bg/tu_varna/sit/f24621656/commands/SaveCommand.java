package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.commands.base.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.base.CommandResult;
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

            if (!session.isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            String currentFilePath = session.getCurrentFilePath();

            XmlFileManager.saveAllData(repository, currentFilePath);

            session.setHasUnsavedChanges(false);
            return CommandResult.success("Successfully saved " + getFileName(currentFilePath));

        } catch (IOException e) {
            return CommandResult.error("Error saving file: " + e.getMessage());
        }
    }

    private String getFileName(String filepath) {
        String fileName = filepath;

        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        } else if (fileName.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        }
        return fileName;
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