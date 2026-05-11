package bg.tu_varna.sit.f24621656.contracts;

import bg.tu_varna.sit.f24621656.commands.base.CommandResult;

public interface Command {
    CommandResult execute(String[] args);
    String getUsage();
    String getDescription();
    String getName();
}
