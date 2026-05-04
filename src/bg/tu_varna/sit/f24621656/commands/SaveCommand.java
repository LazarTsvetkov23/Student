package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;

public class SaveCommand extends BaseCommand {
    private static final String[] VALID_FILES = {"specialties.xml", "disciplines.xml", "students.xml"};

    public SaveCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            validateArgs(args, 1);
            requireFileOpen();

            String currentFilePath = session.getCurrentFilePath();

            String fileName = currentFilePath;
            String dir = "";

            if (currentFilePath.contains("/")) {
                dir = currentFilePath.substring(0, currentFilePath.lastIndexOf("/"));
                fileName = currentFilePath.substring(currentFilePath.lastIndexOf("/") + 1);
            } else if (currentFilePath.contains("\\")) {
                dir = currentFilePath.substring(0, currentFilePath.lastIndexOf("\\"));
                fileName = currentFilePath.substring(currentFilePath.lastIndexOf("\\") + 1);
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

            // Създаване на директорията, ако не съществува
            if (!dir.isEmpty()) {
                Path dirPath = Paths.get(dir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }
            }

            // Запазване на файла (създава или презаписва)
            if (fileName.equalsIgnoreCase("specialties.xml")) {
                XmlFileManager.saveSpecialties(repository);
            } else if (fileName.equalsIgnoreCase("disciplines.xml")) {
                XmlFileManager.saveDisciplines(repository);
            } else if (fileName.equalsIgnoreCase("students.xml")) {
                XmlFileManager.saveStudents(repository);
            }

            session.setHasUnsavedChanges(false);

            // Проверка дали файлът е бил нов (не е съществувал преди)
            String fullPath = dir.isEmpty() ? fileName : dir + "/" + fileName;
            boolean fileExistedBefore = Files.exists(Paths.get(fullPath));

            if (!fileExistedBefore) {
                return CommandResult.success("💾 Created and saved new file: " + fileName);
            } else {
                return CommandResult.success("💾 Successfully saved " + fileName);
            }

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        } catch (IOException e) {
            return CommandResult.error("❌ Error saving file: " + e.getMessage());
        }
    }

    @Override
    public String getUsage() { return "save"; }

    @Override
    public String getDescription() { return "Saves the currently open file (creates it if not exists)"; }

    @Override
    public String getName() { return "save"; }
}