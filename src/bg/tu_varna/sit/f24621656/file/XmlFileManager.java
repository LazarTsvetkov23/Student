package bg.tu_varna.sit.f24621656.file;

import bg.tu_varna.sit.f24621656.contracts.DataRepository;
import bg.tu_varna.sit.f24621656.models.Specialty;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class XmlFileManager {
    private static final String SPECIALTIES_FILE = "specialties.xml";
    private static final String DISCIPLINES_FILE = "disciplines.xml";
    private static final String STUDENTS_FILE = "students.xml";

    private static String currentDirectory = "";

    public static void setCurrentDirectory(String dir) {
        if (dir == null || dir.isEmpty()) {
            currentDirectory = "";
        } else {
            currentDirectory = dir;
        }
    }

    public static String getFullPath(String filename) {
        if (currentDirectory.isEmpty()) {
            return filename;
        }
        return currentDirectory + File.separator + filename;
    }

    public static String getSpecialtiesFilePath() {
        return getFullPath(SPECIALTIES_FILE);
    }

    public static String getDisciplinesFilePath() {
        return getFullPath(DISCIPLINES_FILE);
    }

    public static String getStudentsFilePath() {
        return getFullPath(STUDENTS_FILE);
    }

    public static void loadAllData(DataRepository repository) throws IOException {
        loadSpecialties(repository);
        // loadDisciplines(repository); // TODO: ще добавя по-късно
        // loadStudents(repository);    // TODO: ще добавя по-късно
    }

    public static void saveAllData(DataRepository repository) throws IOException {
        saveSpecialties(repository);
        // saveDisciplines(repository);  // TODO: ще добавя по-късно
        // saveStudents(repository);     // TODO: ще добавя по-късно
    }

    // ==================== Specialties ====================

    public static void loadSpecialties(DataRepository repository) throws IOException {
        String filepath = getFullPath(SPECIALTIES_FILE);

        if (!Files.exists(Paths.get(filepath))) {
            return;
        }

        String xml = readFile(filepath);
        String specialtiesSection = extractTagContent(xml, "specialties");

        if (specialtiesSection != null) {
            List<String> specialtyItems = extractTagContents(specialtiesSection, "specialty");

            for (String item : specialtyItems) {
                String name = extractTagContent(item, "name");
                String minCreditsStr = extractTagContent(item, "minElectiveCredits");

                int minCredits;

                if (minCreditsStr.isEmpty()) {
                    minCredits = 0;
                } else {
                    minCredits = Integer.parseInt(minCreditsStr);
                }

                Specialty specialty = new Specialty(name, minCredits);
                repository.addSpecialty(specialty);
            }
        }
    }

    public static void saveSpecialties(DataRepository repository) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<specialties>\n");

        for (Specialty specialty : repository.getAllSpecialties()) {
            sb.append("  <specialty>\n");
            sb.append("    <name>").append(xmlEscape(specialty.getName())).append("</name>\n");
            sb.append("    <minElectiveCredits>").append(specialty.getMinElectiveCredits()).append("</minElectiveCredits>\n");
            sb.append("  </specialty>\n");
        }

        sb.append("</specialties>\n");
        writeFile(getFullPath(SPECIALTIES_FILE), sb.toString());
    }

    // ==================== Helper Methods ====================

    private static String readFile(String filepath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);
    }

    private static void writeFile(String filepath, String content) throws IOException {
        Path path = Paths.get(filepath);
        Path parent = path.getParent();

        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        Files.write(path, content.getBytes(StandardCharsets.UTF_8));
    }

    private static String extractTagContent(String xml, String tagName) {
        String openTag = "<" + tagName + ">";
        String closeTag = "</" + tagName + ">";

        int start = xml.indexOf(openTag);
        if (start == -1) {
            return null;
        }

        start += openTag.length();

        int end = xml.indexOf(closeTag, start);
        if (end == -1) {
            return null;
        }

        return xml.substring(start, end).trim();
    }

    private static List<String> extractTagContents(String xml, String tagName) {
        List<String> results = new ArrayList<>();

        String openTag = "<" + tagName + ">";
        String closeTag = "</" + tagName + ">";

        int index = 0;
        while (true) {
            int start = xml.indexOf(openTag, index);
            if (start == -1) {
                break;
            }

            start += openTag.length();

            int end = xml.indexOf(closeTag, start);
            if (end == -1) {
                break;
            }

            results.add(xml.substring(start, end).trim());
            index = end + closeTag.length();
        }
        return results;
    }

    private static String xmlEscape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}