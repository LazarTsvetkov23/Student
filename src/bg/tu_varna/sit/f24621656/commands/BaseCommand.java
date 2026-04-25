package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.contracts.Command;
import bg.tu_varna.sit.f24621656.session.Session;

public abstract class BaseCommand implements Command {
    private final Session session;

    public BaseCommand(Session session) {
        this.session = session;
    }

    protected Session getSession() {
        return session;
    }

    protected void validateArgs(String[] args, int minCount, int maxCount) throws IllegalArgumentException {
        if(args.length < minCount) {
            throw new IllegalArgumentException("Missing arguments. Usage: " + getUsage());
        }
        if (maxCount > 0 && args.length > maxCount) {
            throw new IllegalArgumentException("Too many arguments. Usage: " + getUsage());
        }
    }

    protected void validateFileOpen() throws IllegalArgumentException {
        if (!session.isFileOpen()) {
            throw new IllegalArgumentException("No file is open. Use 'open' first.");
        }
    }

    protected void printSuccess(String message) {
        System.out.println("Success: " + message);
    }

    protected void printError(String message) {
        System.out.println("Error: " + message);
    }

    protected void printInfo(String message) {
        System.out.println(message);
    }
}