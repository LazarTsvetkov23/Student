package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.file.XmlFileManager;
import bg.tu_varna.sit.f24621656.session.Session;

import java.io.IOException;

public class OpenAllCommand extends BaseCommand {
    public OpenAllCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            validateArgs(args, 1);

            XmlFileManager.setCurrentDirectory("");

            StringBuilder resultMessage = new StringBuilder();
            boolean hasErrors = false;
            boolean anyFileLoaded = false;

            // specialties.xml
            try {
                if (XmlFileManager.fileExists("specialties.xml")) {
                    XmlFileManager.loadSpecialties(repository);
                    resultMessage.append("  ✓ Loaded specialties.xml\n");
                    anyFileLoaded = true;
                } else {
                    resultMessage.append("  • specialties.xml (not created yet)\n");
                }
            } catch (IOException e) {
                resultMessage.append("  ❌ Error loading specialties.xml: ").append(e.getMessage()).append("\n");
                hasErrors = true;
            }

            // disciplines.xml
            try {
                if (XmlFileManager.fileExists("disciplines.xml")) {
                    XmlFileManager.loadDisciplines(repository);
                    resultMessage.append("  ✓ Loaded disciplines.xml\n");
                    anyFileLoaded = true;
                } else {
                    resultMessage.append("  • disciplines.xml (not created yet)\n");
                }
            } catch (IOException e) {
                resultMessage.append("  ❌ Error loading disciplines.xml: ").append(e.getMessage()).append("\n");
                hasErrors = true;
            }

            // students.xml
            try {
                if (XmlFileManager.fileExists("students.xml")) {
                    XmlFileManager.loadStudents(repository);
                    resultMessage.append("  ✓ Loaded students.xml\n");
                    anyFileLoaded = true;
                } else {
                    resultMessage.append("  • students.xml (not created yet)\n");
                }
            } catch (IOException e) {
                resultMessage.append("  ❌ Error loading students.xml: ").append(e.getMessage()).append("\n");
                hasErrors = true;
            }

            // Ако няма заредени файлове, изчистваме репозиторито
            if (!anyFileLoaded) {
                repository.clear();
            }

            session.setCurrentFilePath("all_files");
            session.setFileOpen(true);
            session.setHasUnsavedChanges(false);

            String finalMessage = "📂 Opened files:\n" + resultMessage.toString();

            if (hasErrors) {
                return CommandResult.error(finalMessage);
            } else {
                return CommandResult.success(finalMessage);
            }

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "openall";
    }

    @Override
    public String getDescription() {
        return "Opens all files (loads existing ones, creates none)";
    }

    @Override
    public String getName() {
        return "openall";
    }
}
