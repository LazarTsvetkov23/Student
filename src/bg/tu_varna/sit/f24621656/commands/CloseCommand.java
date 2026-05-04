package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.session.Session;

public class CloseCommand extends BaseCommand {
    public CloseCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            validateArgs(args, 1);
            requireFileOpen();

            String fileName = session.getCurrentFilePath();

            if (session.hasUnsavedChanges()) {
                return CommandResult.error("Cannot close: You have unsaved changes! Use 'save' first.");
            }

            session.closeFile();

            String shortName = fileName;
            if (shortName.contains("/")) {
                shortName = shortName.substring(shortName.lastIndexOf("/") + 1);
            } else if (shortName.contains("\\")) {
                shortName = shortName.substring(shortName.lastIndexOf("\\") + 1);
            }

            return CommandResult.success("Successfully closed " + shortName);

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
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