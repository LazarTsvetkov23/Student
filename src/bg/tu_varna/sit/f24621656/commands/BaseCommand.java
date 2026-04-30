package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.contracts.Command;
import bg.tu_varna.sit.f24621656.contracts.DataRepository;
import bg.tu_varna.sit.f24621656.session.Session;

public abstract class BaseCommand implements Command {
    protected final Session session;
    protected final DataRepository repository;

    public BaseCommand(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null");
        }
        this.session = session;
        this.repository = session.getRepository();
    }

    protected void validateArgs(String[] args, int minCount, int maxCount) throws IllegalArgumentException {
        if (args.length < minCount) {
            throw new IllegalArgumentException("Missing arguments. Usage: " + getUsage());
        }
        if (maxCount > 0 && args.length > maxCount) {
            throw new IllegalArgumentException("Too many arguments. Usage: " + getUsage());
        }
    }

    protected void validateArgs(String[] args, int exactCount) throws IllegalArgumentException {
        validateArgs(args, exactCount, exactCount);
    }

    protected void requireFileOpen() throws IllegalStateException {
        if (!session.isFileOpen()) {
            throw new IllegalStateException("No file is open. Use 'open' first.");
        }
    }
}