package bg.tu_varna.sit.f24621656.commands.file;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveAsCommand extends BaseCommand {
    public SaveAsCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: saveas <filename.xml>");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            String newFileName = args[1];

            if (!newFileName.toLowerCase().endsWith(".xml")) {
                return CommandResult.error("Only .xml files are allowed. Please use a file with .xml extension.");
            }

            String currentFilePath = getSession().getCurrentFilePath();

            String dir = "";
            if (currentFilePath.contains("/")) {
                dir = currentFilePath.substring(0, currentFilePath.lastIndexOf("/"));
            } else if (currentFilePath.contains("\\")) {
                dir = currentFilePath.substring(0, currentFilePath.lastIndexOf("\\"));
            }

            String newPath;
            if (dir.isEmpty()) {
                newPath = newFileName;
            } else {
                newPath = dir + "/" + newFileName;
            }

            Path dirPath = Paths.get(dir);
            if (!dir.isEmpty() && !Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            XmlFileManager.saveAllData(getRepository(), newPath);

            getSession().setCurrentFilePath(newPath);
            getSession().setHasUnsavedChanges(false);

            return CommandResult.success("Successfully saved as " + newFileName);

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IOException e) {
            return CommandResult.error("Error saving file: " + e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "saveas <filename.xml>";
    }

    @Override
    public String getDescription() {
        return "Saves the currently open file with a new name";
    }

    @Override
    public String getName() {
        return "saveas";
    }
}