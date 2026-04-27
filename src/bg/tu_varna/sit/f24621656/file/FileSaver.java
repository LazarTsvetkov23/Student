package bg.tu_varna.sit.f24621656.file;

import bg.tu_varna.sit.f24621656.contracts.DataRepository;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Клас за записване на данни във файл.
 *
 * <p>Този клас отговаря само за писане на файлове,
 * спазвайки принципа Single Responsibility.
 *
 * @author Student
 * @version 1.0
 */
public class FileSaver {

    /**
     * Записва данни от репозиторито във файл.
     *
     * @param filepath път до файла
     * @param repository репозиторито с данните за записване
     * @throws IOException при грешка при записване на файла
     */
    public void save(String filepath, DataRepository repository) throws IOException {
        String json = JsonParser.toJson(repository);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), StandardCharsets.UTF_8))) {
            writer.write(json);
        }
    }
}