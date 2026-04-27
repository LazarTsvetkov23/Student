package bg.tu_varna.sit.f24621656.contracts;

import bg.tu_varna.sit.f24621656.commands.CommandResult;

/**
 * Интерфейс за всички команди в системата.
 *
 * <p>Този интерфейс дефинира контракта, който всяка команда трябва да спазва.
 * Спазва принципа Interface Segregation - интерфейсът е малък и фокусиран.
 *
 * @author Student
 * @version 1.0
 */
public interface Command {

    /**
     * Изпълнява командата с подадените аргументи.
     *
     * @param args масив от аргументи (първият е името на командата)
     * @return резултат от изпълнението на командата
     */
    CommandResult execute(String[] args);

    /**
     * Връща синтаксиса за употреба на командата.
     *
     * @return низ с формата на употреба (напр. "open &lt;file&gt;")
     */
    String getUsage();

    /**
     * Връща кратко описание на командата.
     *
     * @return описание на командата
     */
    String getDescription();

    /**
     * Връща името на командата.
     *
     * @return име на командата (напр. "open", "close")
     */
    String getName();
}
