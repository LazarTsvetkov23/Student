package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;

public class SaveAsCommand extends BaseCommand {
    private static final String[] VALID_FILES = {"specialties.xml", "disciplines.xml", "students.xml"};

    public SaveAsCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length < 2) {
                return CommandResult.error("Usage: saveas <filename>");
            }
            requireFileOpen();

            String currentFilePath = session.getCurrentFilePath();
            String newFileName = args[1];

            // Проверка дали новото име е валидно
            boolean isValidFile = false;
            for (String validFile : VALID_FILES) {
                if (newFileName.equalsIgnoreCase(validFile)) {
                    isValidFile = true;
                    break;
                }
            }

            if (!isValidFile) {
                StringBuilder hint = new StringBuilder();
                hint.append("Invalid file name. Available files:\n");
                for (String validFile : VALID_FILES) {
                    hint.append("  • ").append(validFile).append("\n");
                }
                return CommandResult.error(hint.toString());
            }

            // Определяне на текущата директория
            String dir = "";
            if (currentFilePath.contains("/")) {
                dir = currentFilePath.substring(0, currentFilePath.lastIndexOf("/"));
            } else if (currentFilePath.contains("\\")) {
                dir = currentFilePath.substring(0, currentFilePath.lastIndexOf("\\"));
            }

            // Запазване на данните в новия файл
            String oldFileName = currentFilePath;
            if (oldFileName.contains("/")) {
                oldFileName = oldFileName.substring(oldFileName.lastIndexOf("/") + 1);
            } else if (oldFileName.contains("\\")) {
                oldFileName = oldFileName.substring(oldFileName.lastIndexOf("\\") + 1);
            }

            // Копиране на данните от текущия файл в новия
            if (oldFileName.equalsIgnoreCase("specialties.xml") || newFileName.equalsIgnoreCase("specialties.xml")) {
                XmlFileManager.saveSpecialties(repository);
            }
            if (oldFileName.equalsIgnoreCase("disciplines.xml") || newFileName.equalsIgnoreCase("disciplines.xml")) {
                XmlFileManager.saveDisciplines(repository);
            }
            if (oldFileName.equalsIgnoreCase("students.xml") || newFileName.equalsIgnoreCase("students.xml")) {
                XmlFileManager.saveStudents(repository);
            }

            // Актуализиране на текущия път
            String newPath = dir.isEmpty() ? newFileName : dir + "/" + newFileName;
            session.setCurrentFilePath(newPath);
            session.setHasUnsavedChanges(false);

            return CommandResult.success("Successfully saved as " + newFileName);

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        } catch (IOException e) {
            return CommandResult.error("Error saving file: " + e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "saveas <filename>";
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