package bg.tu_varna.sit.f24621656.file;

import bg.tu_varna.sit.f24621656.contracts.DataRepository;
import bg.tu_varna.sit.f24621656.enums.DisciplineType;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Grade;
import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.models.Student;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class XmlFileManager {
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
        return currentDirectory + "/" + filename;
    }

    public static void saveAllData(DataRepository repository, String filepath) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<university>\n\n");

        sb.append("  <specialties>\n");
        for (Specialty specialty : repository.getAllSpecialties()) {
            sb.append("    <specialty>\n");
            sb.append("      <name>").append(xmlEscape(specialty.getName())).append("</name>\n");
            sb.append("      <minElectiveCredits>").append(specialty.getMinElectiveCredits()).append("</minElectiveCredits>\n");
            sb.append("    </specialty>\n");
        }
        sb.append("  </specialties>\n\n");

        sb.append("  <disciplines>\n");
        for (Discipline discipline : repository.getAllDisciplines()) {
            sb.append("    <discipline>\n");
            sb.append("      <name>").append(xmlEscape(discipline.getName())).append("</name>\n");
            sb.append("      <type>").append(discipline.getType()).append("</type>\n");
            sb.append("      <credits>").append(discipline.getCredits()).append("</credits>\n");
            sb.append("      <availableCourses>\n");
            for (int course : discipline.getAvailableCourses()) {
                sb.append("        <course>").append(course).append("</course>\n");
            }
            sb.append("      </availableCourses>\n");
            sb.append("    </discipline>\n");
        }
        sb.append("  </disciplines>\n\n");

        sb.append("  <students>\n");
        for (Student student : repository.getAllStudents()) {
            sb.append("    <student>\n");
            sb.append("      <name>").append(xmlEscape(student.getName())).append("</name>\n");
            sb.append("      <facultyNumber>").append(student.getFacultyNumber()).append("</facultyNumber>\n");
            sb.append("      <course>").append(student.getCourse()).append("</course>\n");
            sb.append("      <specialty>").append(xmlEscape(student.getSpecialty().getName())).append("</specialty>\n");
            sb.append("      <group>").append(student.getGroup()).append("</group>\n");
            sb.append("      <status>").append(student.getStatus()).append("</status>\n");
            sb.append("      <grades>\n");
            for (Grade grade : student.getGrades()) {
                sb.append("        <grade>\n");
                sb.append("          <discipline>").append(xmlEscape(grade.getDiscipline().getName())).append("</discipline>\n");
                sb.append("          <value>").append(grade.getValue()).append("</value>\n");
                sb.append("        </grade>\n");
            }
            sb.append("      </grades>\n");
            sb.append("      <enrolledDisciplines>\n");
            for (Discipline discipline : student.getEnrolledDisciplines()) {
                sb.append("        <discipline>").append(xmlEscape(discipline.getName())).append("</discipline>\n");
            }
            sb.append("      </enrolledDisciplines>\n");
            sb.append("    </student>\n");
        }
        sb.append("  </students>\n\n");

        sb.append("</university>\n");
        writeFile(filepath, sb.toString());
    }

    public static void loadAllData(DataRepository repository, String filepath) throws IOException {
        if (!Files.exists(Paths.get(filepath))) return;

        String xml = readFile(filepath);
        repository.clear();

        String specialtiesSection = extractTagContent(xml, "specialties");
        if (specialtiesSection != null) {
            List<String> items = extractTagContents(specialtiesSection, "specialty");
            for (String item : items) {
                String name = extractTagContent(item, "name");
                String creditsStr = extractTagContent(item, "minElectiveCredits");

                int credits;
                if (creditsStr == null || creditsStr.isEmpty()) {
                    credits = 0;
                } else {
                    credits = Integer.parseInt(creditsStr);
                }

                repository.addSpecialty(new Specialty(name, credits));
            }
        }

        String disciplinesSection = extractTagContent(xml, "disciplines");
        List<Discipline> loadedDisciplines = new ArrayList<>();
        if (disciplinesSection != null) {
            List<String> items = extractTagContents(disciplinesSection, "discipline");
            for (String item : items) {
                String name = extractTagContent(item, "name");
                DisciplineType type = DisciplineType.valueOf(extractTagContent(item, "type"));
                int credits = Integer.parseInt(extractTagContent(item, "credits"));
                Discipline discipline = new Discipline(name, type);
                discipline.setCredits(credits);
                String coursesSection = extractTagContent(item, "availableCourses");
                if (coursesSection != null) {
                    List<String> courseItems = extractTagContents(coursesSection, "course");
                    for (String courseStr : courseItems) {
                        discipline.addAvailableCourse(Integer.parseInt(courseStr.trim()));
                    }
                }
                repository.addDiscipline(discipline);
                loadedDisciplines.add(discipline);
            }
        }

        for (Specialty specialty : repository.getAllSpecialties()) {
            for (Discipline discipline : loadedDisciplines) {
                specialty.addDiscipline(discipline);
            }
        }

        String studentsSection = extractTagContent(xml, "students");
        if (studentsSection != null) {
            List<String> items = extractTagContents(studentsSection, "student");
            for (String item : items) {
                String name = extractTagContent(item, "name");
                String fn = extractTagContent(item, "facultyNumber");
                int course = Integer.parseInt(extractTagContent(item, "course"));
                String specialtyName = extractTagContent(item, "specialty");
                int group = Integer.parseInt(extractTagContent(item, "group"));
                StudentStatus status = StudentStatus.valueOf(extractTagContent(item, "status"));

                Specialty specialty = repository.findSpecialtyByName(specialtyName);
                if (specialty == null) {
                    specialty = new Specialty(specialtyName);
                    repository.addSpecialty(specialty);
                    for (Discipline discipline : loadedDisciplines) {
                        specialty.addDiscipline(discipline);
                    }
                }

                Student student = new Student(name, fn, course, specialty, group);
                student.setStatus(status);

                String gradesSection = extractTagContent(item, "grades");
                if (gradesSection != null) {
                    List<String> gradeItems = extractTagContents(gradesSection, "grade");
                    for (String gradeXml : gradeItems) {
                        String discName = extractTagContent(gradeXml, "discipline");
                        double value = Double.parseDouble(extractTagContent(gradeXml, "value"));
                        Discipline discipline = repository.findDisciplineByName(discName);
                        if (discipline != null) {
                            student.addGradeDirectly(new Grade(discipline, value));
                        }
                    }
                }

                String enrolledSection = extractTagContent(item, "enrolledDisciplines");
                if (enrolledSection != null) {
                    List<String> enrolledItems = extractTagContents(enrolledSection, "discipline");
                    for (String discName : enrolledItems) {
                        Discipline discipline = repository.findDisciplineByName(discName);
                        if (discipline != null) {
                            student.addEnrolledDisciplineDirectly(discipline);
                        }
                    }
                }

                repository.addStudent(student);
            }
        }
    }

    private static String readFile(String filepath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);
    }

    private static void writeFile(String filepath, String content) throws IOException {
        Path path = Paths.get(filepath);
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);  // Създава директориите рекурсивно
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