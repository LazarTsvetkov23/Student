package bg.tu_varna.sit.f24621656.session;

import bg.tu_varna.sit.f24621656.contracts.DataRepository;

/**
 * Manages the current application session state.
 * Keeps track of the open file, unsaved changes, and provides access to the repository.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class Session {
    /** The data repository holding all loaded data. */
    private final DataRepository repository;

    /** Path to the currently open file (null if no file is open). */
    private String currentFilePath;

    /** Flag indicating whether a file is currently open. */
    private boolean isFileOpen;

    /** Flag indicating whether there are unsaved changes since the last save. */
    private boolean hasUnsavedChanges;

    /**
     * Constructs a new session with the given repository.
     * Initially no file is open and no unsaved changes.
     *
     * @param repository the data repository to be used
     */
    public Session(DataRepository repository) {
        this.repository = repository;
        this.currentFilePath = null;
        this.isFileOpen = false;
        this.hasUnsavedChanges = false;
    }

    /**
     * Returns the data repository.
     *
     * @return the repository
     */
    public DataRepository getRepository() {
        return repository;
    }

    /**
     * Returns the path of the currently open file.
     *
     * @return file path or null if none open
     */
    public String getCurrentFilePath() {
        return currentFilePath;
    }

    /**
     * Sets the path of the currently open file.
     *
     * @param currentFilePath new file path
     */
    public void setCurrentFilePath(String currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    /**
     * Checks whether a file is open.
     *
     * @return true if a file is open, false otherwise
     */
    public boolean isFileOpen() {
        return isFileOpen;
    }

    /**
     * Sets the file open flag.
     *
     * @param fileOpen new flag value
     */
    public void setFileOpen(boolean fileOpen) {
        isFileOpen = fileOpen;
    }

    /**
     * Checks whether there are unsaved changes.
     *
     * @return true if changes exist, false otherwise
     */
    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    /**
     * Sets the unsaved changes flag.
     *
     * @param hasUnsavedChanges new flag value
     */
    public void setHasUnsavedChanges(boolean hasUnsavedChanges) {
        this.hasUnsavedChanges = hasUnsavedChanges;
    }

    /**
     * Closes the currently open file.
     * Clears the repository, resets the file path and flags.
     * Does nothing if no file is open.
     */
    public void closeFile() {
        if (!isFileOpen) {
            return;
        }

        repository.clear();
        currentFilePath = null;
        isFileOpen = false;
        hasUnsavedChanges = false;
    }
}