package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.contracts.Command;

//WORK

public class ExitCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        return CommandResult.success("Exiting the program...");
    }

    @Override
    public String getUsage() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "Exits the program";
    }

    @Override
    public String getName() {
        return "exit";
    }
}
