package bg.tu_varna.sit.f24621656.commands;

/**
 * Represents the result of executing a command.
 * Contains a success flag and a message to be displayed to the user.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class CommandResult {
    /** Indicates whether the command executed successfully. */
    private final boolean success;

    /** The message to display (error or success). */
    private final String message;

    /**
     * Private constructor.
     *
     * @param success true if successful, false otherwise
     * @param message the message text
     */
    private CommandResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Creates a successful result with the given message.
     *
     * @param message the success message
     * @return a CommandResult instance with success=true
     */
    public static CommandResult success(String message) {
        return new CommandResult(true, message);
    }

    /**
     * Creates an error result with the given message.
     * Automatically prefixes "Error: " if not already present.
     *
     * @param message the error message
     * @return a CommandResult instance with success=false
     */
    public static CommandResult error(String message) {
        String errorMessage = message;
        if (!message.startsWith("Error:")) {
            errorMessage = "Error: " + message;
        }
        return new CommandResult(false, errorMessage);
    }

    /**
     * Returns the success flag.
     *
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the message.
     *
     * @return the message string
     */
    public String getMessage() {
        return message;
    }
}
