package bg.tu_varna.sit.f24621656.commands.file;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Saves the current data to a new file (with a different name or path).
 * After saving, the new file becomes the current file.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class SaveAsCommand extends BaseCommand {
    /**
     * Constructs a SaveAsCommand with the given session.
     *
     * @param session the current session
     */
    public SaveAsCommand(Session session) {
        super(session);
    }

    /**
     * Executes the save as command.
     * Validates the new file name, creates directories if needed,
     * saves the data, and updates the session's current file path.
     *
     * @param args arguments: args[1] is the new file name/path
     * @return CommandResult indicating success or error
     */
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