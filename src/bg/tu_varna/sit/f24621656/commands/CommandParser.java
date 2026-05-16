package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.commands.discipline.AddDisciplineCommand;
import bg.tu_varna.sit.f24621656.commands.discipline.ListDisciplinesCommand;
import bg.tu_varna.sit.f24621656.commands.discipline.RemoveDisciplineCommand;
import bg.tu_varna.sit.f24621656.commands.file.*;
import bg.tu_varna.sit.f24621656.commands.grade.AddGradeCommand;
import bg.tu_varna.sit.f24621656.commands.grade.EnrollInCommand;
import bg.tu_varna.sit.f24621656.commands.grade.ProtocolCommand;
import bg.tu_varna.sit.f24621656.commands.grade.ReportCommand;
import bg.tu_varna.sit.f24621656.commands.other.ExitCommand;
import bg.tu_varna.sit.f24621656.commands.other.HelpCommand;
import bg.tu_varna.sit.f24621656.commands.specialty.AddSpecialtyCommand;
import bg.tu_varna.sit.f24621656.commands.specialty.ListSpecialtiesCommand;
import bg.tu_varna.sit.f24621656.commands.specialty.RemoveSpecialtyCommand;
import bg.tu_varna.sit.f24621656.commands.student.*;
import bg.tu_varna.sit.f24621656.contracts.Command;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.HashMap;
import java.util.Map;

/**
 * Parses user input and executes the corresponding command.
 * Maintains a registry of all available commands.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class CommandParser {
    /** Map of command name to Command instance. */
    private final Map<String, Command> commands;

    /**
     * Constructs a CommandParser and registers all commands with the given session.
     *
     * @param session the current session (shared with all commands)
     */
    public CommandParser(Session session) {
        this.commands = new HashMap<>();
        initializeCommands(session);
    }

    /**
     * Registers all command implementations.
     *
     * @param session the current session
     */
    private void initializeCommands(Session session) {
        registerCommand(new OpenCommand(session));
        registerCommand(new SaveCommand(session));
        registerCommand(new SaveAsCommand(session));
        registerCommand(new CloseCommand(session));

        registerCommand(new AddSpecialtyCommand(session));
        registerCommand(new ListSpecialtiesCommand(session));
        registerCommand(new RemoveSpecialtyCommand(session));

        registerCommand(new AddDisciplineCommand(session));
        registerCommand(new ListDisciplinesCommand(session));
        registerCommand(new RemoveDisciplineCommand(session));

        registerCommand(new EnrollCommand(session));
        registerCommand(new PrintCommand(session));
        registerCommand(new PrintAllCommand(session));
        registerCommand(new AdvanceCommand(session));
        registerCommand(new GraduateCommand(session));
        registerCommand(new InterruptCommand(session));
        registerCommand(new ResumeCommand(session));
        registerCommand(new ChangeCommand(session));
        registerCommand(new EnrollInCommand(session));
        registerCommand(new AddGradeCommand(session));
        registerCommand(new ReportCommand(session));
        registerCommand(new ProtocolCommand(session));

        registerCommand(new ExitCommand());
        registerCommand(new HelpCommand(commands));
    }

    /**
     * Adds a command to the registry.
     *
     * @param command the command to register
     */
    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Parses the input string, finds the command and executes it.
     *
     * @param input the raw user input (e.g., "open file.xml")
     * @return the result of the command execution
     */
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