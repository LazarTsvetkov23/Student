package bg.tu_varna.sit.f24621656.session;

import bg.tu_varna.sit.f24621656.contracts.DataRepository;

public class Session {
    private final DataRepository repository;
    private String currentFilePath;
    private boolean isFileOpen;
    private boolean isHasUnsavedChanges;

    public Session(DataRepository repository) {
        this.repository = repository;
        this.currentFilePath = null;
        this.isFileOpen = false;
        this.isHasUnsavedChanges = false;
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

    public boolean isHasUnsavedChanges() {
        return isHasUnsavedChanges;
    }

    public void setHasUnsavedChanges(boolean isHasUnsavedChanges) {
        this.isHasUnsavedChanges = isHasUnsavedChanges;
    }

    public void closeFile() {
        if(!isFileOpen) {
            return;
        }
        repository.clear();
        currentFilePath = null;
        isFileOpen = false;
        isHasUnsavedChanges = false;
    }
}
