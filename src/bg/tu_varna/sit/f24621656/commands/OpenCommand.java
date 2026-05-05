package bg.tu_varna.sit.f24621656.commands;

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
                return CommandResult.error("❌ Usage: open <filename.xml>");
            }

            String filepath = args[1];

            if (!filepath.toLowerCase().endsWith(".xml")) {
                return CommandResult.error("❌ Only .xml files are allowed. Please use a file with .xml extension.");
            }

            String directory = "";
            String fileName = filepath;

            if (filepath.contains("/")) {
                directory = filepath.substring(0, filepath.lastIndexOf("/"));
                fileName = filepath.substring(filepath.lastIndexOf("/") + 1);
            } else if (filepath.contains("\\")) {
                directory = filepath.substring(0, filepath.lastIndexOf("\\"));
                fileName = filepath.substring(filepath.lastIndexOf("\\") + 1);
            }

            XmlFileManager.setCurrentDirectory(directory);

            String fullPath = XmlFileManager.getFullPath(fileName);
            boolean fileExists = Files.exists(Paths.get(fullPath));

            if (!fileExists) {
                repository.clear();
                session.setCurrentFilePath(filepath);
                session.setFileOpen(true);
                session.setHasUnsavedChanges(false);
                return CommandResult.success("📂 Opened new (unsaved) file: " + fileName + " (use 'save' to create it on disk)");
            }

            if (fileName.equalsIgnoreCase("specialties.xml")) {
                XmlFileManager.loadSpecialties(repository);
            } else if (fileName.equalsIgnoreCase("disciplines.xml")) {
                XmlFileManager.loadDisciplines(repository);
            } else if (fileName.equalsIgnoreCase("students.xml")) {
                XmlFileManager.loadStudents(repository);
            } else {
                repository.clear();
            }

            session.setCurrentFilePath(filepath);
            session.setFileOpen(true);
            session.setHasUnsavedChanges(false);

            return CommandResult.success("📂 Successfully opened " + fileName);

        } catch (IllegalArgumentException e) {
            return CommandResult.error("❌ " + e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error("❌ " + e.getMessage());
        } catch (IOException e) {
            return CommandResult.error("❌ Error reading file: " + e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "open <filename.xml>";
    }

    @Override
    public String getDescription() {
        return "Opens an XML file (loads if exists, creates in memory if not)";
    }

    @Override
    public String getName() {
        return "open";
    }
}
