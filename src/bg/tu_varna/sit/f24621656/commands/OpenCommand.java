package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenCommand extends BaseCommand {
    private static final String[] VALID_FILES = {"specialties.xml", "disciplines.xml", "students.xml"};

    public OpenCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            validateArgs(args, 2);

            String filepath = args[1];

            String dir = "";
            if (filepath.contains("/")) {
                dir = filepath.substring(0, filepath.lastIndexOf("/"));
            } else if (filepath.contains("\\")) {
                dir = filepath.substring(0, filepath.lastIndexOf("\\"));
            }
            XmlFileManager.setCurrentDirectory(dir);

            String fileName = filepath;
            if (filepath.contains("/")) {
                fileName = filepath.substring(filepath.lastIndexOf("/") + 1);
            } else if (filepath.contains("\\")) {
                fileName = filepath.substring(filepath.lastIndexOf("\\") + 1);
            }

            boolean isValidFile = false;
            for (String validFile : VALID_FILES) {
                if (fileName.equalsIgnoreCase(validFile)) {
                    isValidFile = true;
                    break;
                }
            }

            if (!isValidFile) {
                StringBuilder hint = new StringBuilder();
                hint.append("Invalid file. Available files:\n");
                for (String validFile : VALID_FILES) {
                    hint.append("  • ").append(validFile).append("\n");
                }
                return CommandResult.error(hint.toString());
            }

            String fullPath = XmlFileManager.getFullPath(fileName);
            boolean fileExists = Files.exists(Paths.get(fullPath));

            if (fileName.equalsIgnoreCase("specialties.xml")) {
                if (!fileExists) {
                    XmlFileManager.saveSpecialties(repository);
                } else {
                    XmlFileManager.loadSpecialties(repository);
                }
            } else if (fileName.equalsIgnoreCase("disciplines.xml")) {
                if (!fileExists) {
                    XmlFileManager.saveDisciplines(repository);
                } else {
                    XmlFileManager.loadDisciplines(repository);
                }
            } else if (fileName.equalsIgnoreCase("students.xml")) {
                if (!fileExists) {
                    XmlFileManager.saveStudents(repository);
                } else {
                    XmlFileManager.loadStudents(repository);
                }
            }

            session.setCurrentFilePath(filepath);
            session.setFileOpen(true);
            session.setHasUnsavedChanges(false);

            if (!fileExists) {
                return CommandResult.success("Created and opened new file: " + fileName);
            } else {
                return CommandResult.success("Successfully opened " + fileName);
            }

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        } catch (IOException e) {
            return CommandResult.error("Error: " + e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "open <specialties.xml|disciplines.xml|students.xml>";
    }

    @Override
    public String getDescription() {
        return "Opens an XML file (creates it if not exists)";
    }

    @Override
    public String getName() {
        return "open";
    }
}
