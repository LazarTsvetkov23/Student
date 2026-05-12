package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.contracts.Command;
import bg.tu_varna.sit.f24621656.contracts.DataRepository;
import bg.tu_varna.sit.f24621656.session.Session;

public abstract class BaseCommand implements Command {
    private final Session session;
    private final DataRepository repository;

    public BaseCommand(Session session) {
        this.session = session;
        this.repository = session.getRepository();
    }

    protected String getFileName(String filepath) {
        String fileName = filepath;

        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        } else if (fileName.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        }

        return fileName;
    }

    public Session getSession() {
        return session;
    }

    public DataRepository getRepository() {
        return repository;
    }
}