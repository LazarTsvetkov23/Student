package bg.tu_varna.sit.f24621656.session;

import bg.tu_varna.sit.f24621656.contracts.DataRepository;

public class Session {
    private final DataRepository repository;
    private String currentFilePath;
    private boolean isFileOpen;
    private boolean hasUnsavedChanges;

    public Session(DataRepository repository) {
        this.repository = repository;
        this.currentFilePath = null;
        this.isFileOpen = false;
        this.hasUnsavedChanges = false;
    }

    public DataRepository getRepository() {
        return repository;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public void setCurrentFilePath(String currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    public boolean isFileOpen() {
        return isFileOpen;
    }

    public void setFileOpen(boolean fileOpen) {
        isFileOpen = fileOpen;
    }

    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    public void setHasUnsavedChanges(boolean hasUnsavedChanges) {
        this.hasUnsavedChanges = hasUnsavedChanges;
    }

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