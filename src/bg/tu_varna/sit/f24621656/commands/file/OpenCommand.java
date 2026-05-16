package bg.tu_varna.sit.f24621656.commands.file;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Opens an XML file and loads its data into the repository.
 * If the file does not exist, a new empty session is created.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class OpenCommand extends BaseCommand {
    /**
     * Constructs an OpenCommand with the given session.
     *
     * @param session the current session
     */
    public OpenCommand(Session session) {
        super(session);
    }

    /**
     * Executes the open command.
     * Validates the file extension, checks if another file is already open,
     * then either loads existing data or initializes an empty session.
     *
     * @param args command arguments: args[1] is the file name/path
     * @return CommandResult indicating success or error
     */
    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: open <filename.xml>");
            }

            String filepath = args[1];

            if (!filepath.toLowerCase().endsWith(".xml")) {
                return CommandResult.error("Only .xml files are allowed. Please use a file with .xml extension.");
            }

            if (getSession().isFileOpen()) {
                String currentFile = getFileName(getSession().getCurrentFilePath());
                return CommandResult.error("Cannot open new file. Close the current file '" + currentFile + "' first.");
            }

            String dir = "";
            String fileName = filepath;

            if (filepath.contains("/")) {
                dir = filepath.substring(0, filepath.lastIndexOf("/"));
                fileName = filepath.substring(filepath.lastIndexOf("/") + 1);
            } else if (filepath.contains("\\")) {
                dir = filepath.substring(0, filepath.lastIndexOf("\\"));
                fileName = filepath.substring(filepath.lastIndexOf("\\") + 1);
            }

            XmlFileManager.setCurrentDirectory(dir);
            String fullPath = XmlFileManager.getFullPath(fileName);
            boolean fileExists = Files.exists(Paths.get(fullPath));

            if (!fileExists) {
                getRepository().clear();
                getSession().setCurrentFilePath(filepath);
                getSession().setFileOpen(true);
                getSession().setHasUnsavedChanges(false);
                return CommandResult.success("Opened new (unsaved) file: " + fileName + " (use 'save' to create it on disk)");
            }

            XmlFileManager.loadAllData(getRepository(), fullPath);
            getSession().setCurrentFilePath(filepath);
            getSession().setFileOpen(true);
            getSession().setHasUnsavedChanges(false);

            return CommandResult.success("Successfully opened " + fileName);

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IOException e) {
            return CommandResult.error("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Returns the usage string.
     *
     * @return usage
     */
    @Override
    public String getUsage() {
        return "open <filename.xml>";
    }

    /**
     * Returns the description.
     *
     * @return description
     */
    @Override
    public String getDescription() {
        return "Opens an XML file";
    }

    /**
     * Returns the command name.
     *
     * @return "open"
     */
    @Override
    public String getName() {
        return "open";
    }
}