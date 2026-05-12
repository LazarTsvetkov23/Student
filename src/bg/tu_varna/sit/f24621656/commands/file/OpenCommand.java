package bg.tu_varna.sit.f24621656.commands.file;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenCommand extends BaseCommand {
    public OpenCommand(Session session) {
        super(session);
    }

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

    @Override
    public String getUsage() {
        return "open <filename.xml>";
    }

    @Override
    public String getDescription() {
        return "Opens an XML file";
    }

    @Override
    public String getName() {
        return "open";
    }
}