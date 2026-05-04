package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.session.Session;

public class CloseAllCommand extends BaseCommand {
    public CloseAllCommand(Session session) {
        super(session);
    }

    @Override
    public CommandResult execute(String[] args) {
        try {
            validateArgs(args, 1);
            requireFileOpen();

            if (session.hasUnsavedChanges()) {
                return CommandResult.error("⚠️ Cannot close: You have unsaved changes! Use 'save' or 'saveall' first.");
            }

            session.closeFile();

            return CommandResult.success("🔒 Successfully closed all files");

        } catch (IllegalArgumentException e) {
            return CommandResult.error(e.getMessage());
        } catch (IllegalStateException e) {
            return CommandResult.error(e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "closeall";
    }

    @Override
    public String getDescription() {
        return "Closes all currently open files";
    }

    @Override
    public String getName() {
        return "closeall";
    }
}
