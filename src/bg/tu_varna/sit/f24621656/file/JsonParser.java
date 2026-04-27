package bg.tu_varna.sit.f24621656.file;

import bg.tu_varna.sit.f24621656.contracts.DataRepository;
import bg.tu_varna.sit.f24621656.enums.DisciplineType;
import bg.tu_varna.sit.f24621656.enums.StudentStatus;
import bg.tu_varna.sit.f24621656.models.Discipline;
import bg.tu_varna.sit.f24621656.models.Grade;
import bg.tu_varna.sit.f24621656.models.Specialty;
import bg.tu_varna.sit.f24621656.models.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Парсер за JSON формат.
 *
 * <p>Този клас отговаря за конвертирането между Java обекти и JSON низове.
 * Спазва принципа Single Responsibility - единствената отговорност
 * на този клас е JSON парсването.
 *
 * @author Student
 * @version 1.0
 */
public class JsonParser {

    /**
     * Конвертира данните от репозиторито в JSON низ.
     *
     * @param repository репозиторито с данни
     * @return JSON низ, представляващ данните
     */
    public static String toJson(DataRepository repository) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        // Specialties
        sb.append("  \"specialties\": [\n");
        List<Specialty> specialties = repository.getAllSpecialties();
        for (int i = 0; i < specialties.size(); i++) {
            Specialty specialty = specialties.get(i);
            sb.append("    {\n");
            sb.append("      \"name\": \"").append(escapeJson(specialty.getName())).append("\",\n");
            sb.append("      \"minElectiveCredits\": ").append(specialty.getMinElectiveCredits()).append("\n");
            sb.append("    }");
            if (i < specialties.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ],\n");

        // Disciplines
        sb.append("  \"disciplines\": [\n");
        List<Discipline> disciplines = repository.getAllDisciplines();
        for (int i = 0; i < disciplines.size(); i++) {
            Discipline discipline = disciplines.get(i);
            sb.append("    {\n");
            sb.append("      \"name\": \"").append(escapeJson(discipline.getName())).append("\",\n");
            sb.append("      \"type\": \"").append(discipline.getType()).append("\",\n");
            sb.append("      \"credits\": ").append(discipline.getCredits()).append(",\n");
            sb.append("      \"availableCourses\": [");
            List<Integer> courses = discipline.getAvailableCourses();
            for (int j = 0; j < courses.size(); j++) {
                sb.append(courses.get(j));
                if (j < courses.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]\n");
            sb.append("    }");
            if (i < disciplines.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ],\n");

        // Students
        sb.append("  \"students\": [\n");
        List<Student> students = repository.getAllStudents();
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            sb.append("    {\n");
            sb.append("      \"name\": \"").append(escapeJson(student.getName())).append("\",\n");
            sb.append("      \"facultyNumber\": \"").append(student.getFacultyNumber()).append("\",\n");
            sb.append("      \"course\": ").append(student.getCourse()).append(",\n");
            sb.append("      \"specialty\": \"").append(escapeJson(student.getSpecialty().getName())).append("\",\n");
            sb.append("      \"group\": ").append(student.getGroup()).append(",\n");
            sb.append("      \"status\": \"").append(student.getStatus()).append("\",\n");
            sb.append("      \"grades\": [\n");
            List<Grade> grades = student.getGrades();
            for (int j = 0; j < grades.size(); j++) {
                Grade grade = grades.get(j);
                sb.append("        {\n");
                sb.append("          \"discipline\": \"").append(escapeJson(grade.getDiscipline().getName())).append("\",\n");
                sb.append("          \"value\": ").append(grade.getValue()).append("\n");
                sb.append("        }");
                if (j < grades.size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append("      ],\n");
            sb.append("      \"enrolledDisciplines\": [\n");
            List<Discipline> enrolled = student.getEnrolledDisciplines();
            for (int j = 0; j < enrolled.size(); j++) {
                sb.append("        \"").append(escapeJson(enrolled.get(j).getName())).append("\"");
                if (j < enrolled.size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append("      ]\n");
            sb.append("    }");
            if (i < students.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}\n");

        return sb.toString();
    }

    /**
     * Зарежда данни от JSON низ в репозиторито.
     *
     * @param json JSON низ с данни
     * @param repository репозиторито, в което да се заредят данните
     */
    public static void fromJson(String json, DataRepository repository) {
        repository.clear();

        Map<String, Specialty> specialtyMap = new HashMap<>();

        // Parse specialties
        String specialtiesSection = extractSection(json, "specialties");
        if (specialtiesSection != null) {
            List<Map<String, String>> specialtyItems = parseJsonArray(specialtiesSection);
            for (Map<String, String> item : specialtyItems) {
                String name = item.get("name");
                int minCredits = Integer.parseInt(item.get("minElectiveCredits"));
                Specialty specialty = new Specialty(name, minCredits);
                repository.addSpecialty(specialty);
                specialtyMap.put(name, specialty);
            }
        }

        // Parse disciplines
        String disciplinesSection = extractSection(json, "disciplines");
        if (disciplinesSection != null) {
            List<Map<String, String>> disciplineItems = parseJsonArray(disciplinesSection);
            for (Map<String, String> item : disciplineItems) {
                String name = item.get("name");
                DisciplineType type = DisciplineType.valueOf(item.get("type"));
                int credits = Integer.parseInt(item.get("credits"));
                String coursesStr = item.get("availableCourses");

                Discipline discipline = new Discipline(name, type);
                discipline.setCredits(credits);

                if (coursesStr != null && !coursesStr.isEmpty()) {
                    String[] courses = coursesStr.replace("[", "").replace("]", "").split(",");
                    for (String course : courses) {
                        try {
                            discipline.addAvailableCourse(Integer.parseInt(course.trim()));
                        } catch (NumberFormatException e) {
                            // ignore
                        }
                    }
                }

                repository.addDiscipline(discipline);
            }
        }

        // Link disciplines to specialties
        for (Specialty specialty : repository.getAllSpecialties()) {
            for (Discipline discipline : repository.getAllDisciplines()) {
                if (!specialty.getDisciplines().contains(discipline)) {
                    specialty.addDiscipline(discipline);
                }
            }
        }

        // Parse students
        String studentsSection = extractSection(json, "students");
        if (studentsSection != null) {
            List<Map<String, String>> studentItems = parseJsonArray(studentsSection);
            for (Map<String, String> item : studentItems) {
                String name = item.get("name");
                String facultyNumber = item.get("facultyNumber");
                int course = Integer.parseInt(item.get("course"));
                String specialtyName = item.get("specialty");
                int group = Integer.parseInt(item.get("group"));
                StudentStatus status = StudentStatus.valueOf(item.get("status"));

                Specialty specialty = repository.findSpecialtyByName(specialtyName);
                if (specialty == null) {
                    specialty = new Specialty(specialtyName);
                    repository.addSpecialty(specialty);
                    specialtyMap.put(specialtyName, specialty);
                }

                Student student = new Student(name, facultyNumber, course, specialty, group);
                student.setStatus(status);

                // Grades
                String gradesSection = extractSection(item.get("grades"), null);
                if (gradesSection != null && !gradesSection.isEmpty()) {
                    List<Map<String, String>> gradeItems = parseJsonArray(gradesSection);
                    for (Map<String, String> gradeItem : gradeItems) {
                        String discName = gradeItem.get("discipline");
                        double value = Double.parseDouble(gradeItem.get("value"));
                        Discipline discipline = repository.findDisciplineByName(discName);
                        if (discipline != null) {
                            Grade grade = new Grade(discipline, value);
                            student.addGrade(grade);
                        }
                    }
                }

                // Enrolled disciplines
                String enrolledSection = extractSection(item.get("enrolledDisciplines"), null);
                if (enrolledSection != null && !enrolledSection.isEmpty()) {
                    List<String> enrolledNames = parseJsonStringArray(enrolledSection);
                    for (String discName : enrolledNames) {
                        Discipline discipline = repository.findDisciplineByName(discName);
                        if (discipline != null) {
                            student.enrollInDiscipline(discipline);
                        }
                    }
                }

                repository.addStudent(student);
            }
        }
    }

    /**
     * Извлича секция от JSON низ.
     *
     * @param json JSON низът
     * @param sectionName име на секцията
     * @return съдържанието на секцията или null
     */
    private static String extractSection(String json, String sectionName) {
        if (sectionName == null) {
            return json;
        }
        String search = "\"" + sectionName + "\":";
        int start = json.indexOf(search);
        if (start == -1) {
            return null;
        }
        start = json.indexOf("[", start);
        if (start == -1) {
            return null;
        }

        int bracketCount = 1;
        int end = start + 1;
        while (end < json.length() && bracketCount > 0) {
            char c = json.charAt(end);
            if (c == '[') {
                bracketCount++;
            }
            else if (c == ']') {
                bracketCount--;
            }
            end++;
        }
        return json.substring(start + 1, end - 1);
    }

    /**
     * Парсва JSON масив от обекти.
     *
     * @param arrayStr низът, представляващ масива
     * @return списък от карти с ключове и стойности
     */
    private static List<Map<String, String>> parseJsonArray(String arrayStr) {
        List<Map<String, String>> result = new ArrayList<>();
        if (arrayStr == null || arrayStr.trim().isEmpty()) {
            return result;
        }

        int depth = 0;
        int start = -1;
        for (int i = 0; i < arrayStr.length(); i++) {
            char c = arrayStr.charAt(i);
            if (c == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth++;
            }
            else if (c == '}') {
                depth--;
                if (depth == 0 && start != -1) {
                    String itemStr = arrayStr.substring(start + 1, i);
                    result.add(parseJsonObject(itemStr));
                    start = -1;
                }
            }
        }
        return result;
    }

    /**
     * Парсва JSON обект в карта.
     *
     * @param objStr низът, представляващ обекта
     * @return карта с ключове и стойности
     */
    private static Map<String, String> parseJsonObject(String objStr) {
        Map<String, String> result = new HashMap<>();
        String[] lines = objStr.split(",");
        for (String line : lines) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                String key = parts[0].trim().replace("\"", "");
                String value = parts[1].trim().replace("\"", "");
                result.put(key, value);
            }
        }
        return result;
    }

    /**
     * Парсва JSON масив от низове.
     *
     * @param arrayStr низът, представляващ масива
     * @return списък от низове
     */
    private static List<String> parseJsonStringArray(String arrayStr) {
        List<String> result = new ArrayList<>();
        if (arrayStr == null || arrayStr.trim().isEmpty()) return result;

        String[] items = arrayStr.split(",");
        for (String item : items) {
            String cleaned = item.trim().replace("\"", "");
            if (!cleaned.isEmpty()) {
                result.add(cleaned);
            }
        }
        return result;
    }

    /**
     * Ескейпва специални символи за JSON.
     *
     * @param text текстът за ескейпване
     * @return ескейпнат текст
     */
    private static String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}