package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.contracts.Command;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {
    private final Map<String, Command> commands;

    public CommandParser(Session session) {
        this.commands = new HashMap<>();
        initializeCommands(session);
    }

    private void initializeCommands(Session session) {
        // File commands
        registerCommand(new OpenCommand(session));
        registerCommand(new SaveCommand(session));
        registerCommand(new CloseCommand(session));

        // Specialty commands
        registerCommand(new AddSpecialtyCommand(session));

        // Other commands
        registerCommand(new ExitCommand());
        registerCommand(new HelpCommand(commands));
    }

    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public CommandResult parseAndExecute(String input) {
        if (input == null || input.trim().isEmpty()) {
            return CommandResult.success("");
        }

        String[] parts = input.trim().split("\\s+");
        String commandName = parts[0].toLowerCase();

        Command command = commands.get(commandName);

        if (command == null) {
            return CommandResult.error("Unknown command: '" + commandName + "'. Type 'help' for available commands.");
        }

        return command.execute(parts);
    }
}