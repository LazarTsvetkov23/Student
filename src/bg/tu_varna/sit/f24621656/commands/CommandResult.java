package bg.tu_varna.sit.f24621656.commands;

public class CommandResult {
    private final boolean success;
    private final String message;

    private CommandResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static CommandResult success(String message) {
        return new CommandResult(true, message);
    }

    public static CommandResult error(String message) {
        String errorMessage = message;
        if (!message.startsWith("Error:")) {
            errorMessage = "Error: " + message;
        }
        return new CommandResult(false, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
