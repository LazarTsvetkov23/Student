package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;

//WORKED

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
            requireFileOpen();

            String currentFilePath = session.getCurrentFilePath();

            if (currentFilePath.equals("all_files")) {
                return CommandResult.error("Cannot save: multiple files are open. Use 'saveall' to save all files, or close and open a single file first.");
            }

            XmlFileManager.saveCurrentFile(currentFilePath, repository);
            session.setHasUnsavedChanges(false);

            String fileName = getFileName(currentFilePath);
            return CommandResult.success("Successfully saved " + fileName);

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
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
        return "Saves the currently open file (only works with a single open file)";
    }

    @Override
    public String getName() {
        return "save";
    }
}