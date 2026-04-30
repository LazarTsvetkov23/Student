package bg.tu_varna.sit.f24621656.commands;

import bg.tu_varna.sit.f24621656.contracts.Command;

import java.util.Map;

public class HelpCommand implements Command {
    private final Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public CommandResult execute(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=========================================\n");
        sb.append("Available Commands\n");
        sb.append("=========================================\n\n");

        sb.append("FILE COMMANDS:\n");
        sb.append("  open <file>.xml                        - Opens XML files\n");
        sb.append("  save                                   - Saves all data to XML files\n\n");
        //TODO: sb.append("  close                                - Closes the currently open file\n\n");

        sb.append("SPECIALTY COMMANDS:\n");
        sb.append("  addspecialty <name> <minCredits> - Adds a new specialty\n\n");

        //TODO: sb.append("STUDENT COMMANDS:\n");
        //TODO: sb.append("  enroll <fn> <program> <group> <name> - Enrolls a student\n");
        //TODO: sb.append("  print <fn>     - Prints student information\n\n");

        sb.append("OTHER COMMANDS:\n");
        sb.append("  help            - Shows this help message\n");
        sb.append("  exit            - Exits the program\n");
        sb.append("=========================================\n");

        return CommandResult.success(sb.toString());
    }

    @Override
    public String getUsage() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows this help message";
    }

    @Override
    public String getName() {
        return "help";
    }
}