package bg.tu_varna.sit.f24621656.file;

import bg.tu_varna.sit.f24621656.contracts.DataRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Клас за зареждане на данни от файл.
 *
 * <p>Този клас отговаря само за четене на файлове,
 * спазвайки принципа Single Responsibility.
 *
 * @author Student
 * @version 1.0
 */
public class FileLoader {

    /**
     * Зарежда данни от файл в репозиторито.
     *
     * @param filepath път до файла
     * @param repository репозиторито, в което да се заредят данните
     * @throws IOException при грешка при четене на файла
     */
    public void load(String filepath, DataRepository repository) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }

        JsonParser.fromJson(content.toString(), repository);
    }

    /**
     * Проверява дали файл съществува.
     *
     * @param filepath път до файла
     * @return true ако файлът съществува
     */
    public boolean fileExists(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }
}
