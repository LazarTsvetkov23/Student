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

            // specialties.xml
            try {
                boolean exists = XmlFileManager.fileExists("specialties.xml");
                if (!exists) {
                    XmlFileManager.saveSpecialties(repository);
                    resultMessage.append("Created specialties.xml\n");
                } else {
                    XmlFileManager.loadSpecialties(repository);
                    resultMessage.append("Loaded specialties.xml\n");
                }
            } catch (IOException e) {
                resultMessage.append("Error loading specialties.xml: ").append(e.getMessage()).append("\n");
                hasErrors = true;
            }

            // disciplines.xml
            try {
                boolean exists = XmlFileManager.fileExists("disciplines.xml");
                if (!exists) {
                    XmlFileManager.saveDisciplines(repository);
                    resultMessage.append("Created disciplines.xml\n");
                } else {
                    XmlFileManager.loadDisciplines(repository);
                    resultMessage.append("Loaded disciplines.xml\n");
                }
            } catch (IOException e) {
                resultMessage.append("Error loading disciplines.xml: ").append(e.getMessage()).append("\n");
                hasErrors = true;
            }

            // students.xml
            try {
                boolean exists = XmlFileManager.fileExists("students.xml");
                if (!exists) {
                    XmlFileManager.saveStudents(repository);
                    resultMessage.append("Created students.xml\n");
                } else {
                    XmlFileManager.loadStudents(repository);
                    resultMessage.append("Loaded students.xml\n");
                }
            } catch (IOException e) {
                resultMessage.append("Error loading students.xml: ").append(e.getMessage()).append("\n");
                hasErrors = true;
            }

            session.setCurrentFilePath("all_files");
            session.setFileOpen(true);
            session.setHasUnsavedChanges(false);

            String finalMessage = "Opened all files:\n" + resultMessage.toString();

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
        return "Opens all files: specialties.xml, disciplines.xml, students.xml";
    }

    @Override
    public String getName() {
        return "openall";
    }
}
