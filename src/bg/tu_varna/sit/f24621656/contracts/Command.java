package bg.tu_varna.sit.f24621656.contracts;

import bg.tu_varna.sit.f24621656.commands.CommandResult;

/**
 * Defines the contract for all commands in the application.
 * Every command must provide execution logic, usage help, description and its name.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public interface Command {
    /**
     * Executes the command with the given arguments.
     *
     * @param args array of command arguments (first element is the command name)
     * @return CommandResult containing success status and message
     */
    CommandResult execute(String[] args);

    /**
     * Returns the usage syntax of the command.
     *
     * @return usage string (e.g., "open <filename.xml>")
     */
    String getUsage();

    /**
     * Returns a brief description of what the command does.
     *
     * @return description string
     */
    String getDescription();

    /**
     * Returns the name of the command (the keyword used to invoke it).
     *
     * @return command name (e.g., "open", "enroll")
     */
    String getName();
}
