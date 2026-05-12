package bg.tu_varna.sit.f24621656.commands;

public class CommandResult {
    private final boolean success;
    private final String message;
    private final Object data;

    private CommandResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static CommandResult success(String message) {
        return new CommandResult(true, message, null);
    }

    public static CommandResult success(String message, Object data) {
        return new CommandResult(true, message, data);
    }

    public static CommandResult error(String message) {
        String errorMessage = message;
        if (!message.startsWith("Error:")) {
            errorMessage = "Error: " + message;
        }
        return new CommandResult(false, errorMessage, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
