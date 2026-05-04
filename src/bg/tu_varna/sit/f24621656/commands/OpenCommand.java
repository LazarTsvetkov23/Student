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
                hint.append("❌ Invalid file. Available files:\n");
                for (String validFile : VALID_FILES) {
                    hint.append("  • ").append(validFile).append("\n");
                }
                return CommandResult.error(hint.toString());
            }

            String fullPath = XmlFileManager.getFullPath(fileName);
            boolean fileExists = Files.exists(Paths.get(fullPath));

            // Ако файлът не съществува, НЕ го създаваме, а само го отваряме в паметта
            if (!fileExists) {
                // Изчистваме репозиторито за нов празен файл
                repository.clear();
                session.setCurrentFilePath(filepath);
                session.setFileOpen(true);
                session.setHasUnsavedChanges(false);
                return CommandResult.success("📂 Opened new (unsaved) file: " + fileName + " (use 'save' to create it on disk)");
            }

            // Ако файлът съществува, зареждаме данните
            if (fileName.equalsIgnoreCase("specialties.xml")) {
                XmlFileManager.loadSpecialties(repository);
            } else if (fileName.equalsIgnoreCase("disciplines.xml")) {
                XmlFileManager.loadDisciplines(repository);
            } else if (fileName.equalsIgnoreCase("students.xml")) {
                XmlFileManager.loadStudents(repository);
            }

            session.setCurrentFilePath(filepath);
            session.setFileOpen(true);
            session.setHasUnsavedChanges(false);

            return CommandResult.success("📂 Successfully opened " + fileName);

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        } catch (IOException e) {
            return CommandResult.error("❌ Error reading file: " + e.getMessage());
        }
    }

    @Override
    public String getUsage() { return "open <specialties.xml|disciplines.xml|students.xml>"; }

    @Override
    public String getDescription() { return "Opens an XML file (loads if exists, creates in memory if not)"; }

    @Override
    public String getName() { return "open"; }
}
