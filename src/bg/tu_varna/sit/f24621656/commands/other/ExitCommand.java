package bg.tu_varna.sit.f24621656.commands.other;

import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.contracts.Command;

/**
 * Exits the program. Does not perform any validation or saving.
 * The main loop will terminate after this command returns.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class ExitCommand implements Command {
    /**
     * Executes the exit command.
     * Simply returns a success message; the caller is responsible for terminating the application.
     *
     * @param args no arguments expected
     * @return CommandResult with exit message
     */
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
