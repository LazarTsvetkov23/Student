package bg.tu_varna.sit.f24621656.commands.file;

import bg.tu_varna.sit.f24621656.commands.BaseCommand;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.session.Session;

/**
 * Closes the currently open file.
 * Fails if there are unsaved changes.
 *
 * @author Lazar Tsvetkov
 * @version 1.0
 */
public class CloseCommand extends BaseCommand {
    /**
     * Constructs a CloseCommand with the given session.
     *
     * @param session the current session
     */
    public CloseCommand(Session session) {
        super(session);
    }

    /**
     * Executes the close command.
     * Checks for unsaved changes, then closes the session and clears the repository.
     *
     * @param args no arguments expected
     * @return CommandResult indicating success or error
     */
    @Override
    public CommandResult execute(String[] args) {
        try {
            if (args.length > 1) {
                return CommandResult.error("Usage: close (no arguments)");
            }

            if (!getSession().isFileOpen()) {
                return CommandResult.error("No file is open. Use 'open' first.");
            }

            if (getSession().hasUnsavedChanges()) {
                return CommandResult.error("Cannot close: You have unsaved changes! Use 'save' first.");
            }

            String fileName = getFileName(getSession().getCurrentFilePath());
            getSession().closeFile();

            return CommandResult.success("Successfully closed " + fileName);

        } catch (Exception e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "close";
    }

    @Override
    public String getDescription() {
        return "Closes the currently open file";
    }

    @Override
    public String getName() {
        return "close";
    }
}