package bg.tu_varna.sit.f24621656.contracts;

public interface Command {
    void execute(String[] args) throws IllegalArgumentException;
    String getUsage();
    String getDescription();
    String getName();
}
