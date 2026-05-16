package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.contracts.Command;
import bg.tu_varna.sit.f24621656.contracts.DataRepository;
import bg.tu_varna.sit.f24621656.session.Session;

/**
 * Abstract base class for all commands.
 * Provides access to the session and repository, and a helper method to extract file name from path.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public abstract class BaseCommand implements Command {
    /** The current session containing repository and file state. */
    private final Session session;

    /** The data repository for accessing students, specialties, disciplines. */
    private final DataRepository repository;

    /**
     * Constructs a BaseCommand with the given session.
     *
     * @param session the current session
     */
    public BaseCommand(Session session) {
        this.session = session;
        this.repository = session.getRepository();
    }

    /**
     * Extracts the file name from a full file path.
     * Handles both forward and backward slashes.
     *
     * @param filepath the full file path
     * @return the file name without directory part
     */
    protected String getFileName(String filepath) {
        String fileName = filepath;

        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        } else if (fileName.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        }

        return fileName;
    }

    /**
     * Returns the current session.
     *
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * Returns the data repository.
     *
     * @return the repository
     */
    public DataRepository getRepository() {
        return repository;
    }
}