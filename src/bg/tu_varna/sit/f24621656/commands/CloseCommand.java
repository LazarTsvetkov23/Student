package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.commands.base.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.base.CommandResult;
import bg.tu_varna.sit.f24621656.session.Session;

public class CloseCommand extends BaseCommand {
    public CloseCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length > 1) {
                return CommandResult.error("Usage: close (no arguments)");
            }

            if (!session.isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            if (session.hasUnsavedChanges()) {
                return CommandResult.error("Cannot close: You have unsaved changes! Use 'save' first.");
            }

            String fileName = getFileName(session.getCurrentFilePath());
            session.closeFile();

            return CommandResult.success("Successfully closed " + fileName);

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
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
        return "close";
    }

    @Override
    public String getDescription() {
        return "Closes the currently open file";
    }

    @Override
    public String getName() {
        return "close";
    }
}