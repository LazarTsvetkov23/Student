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
        sb.append("\n╔═════════════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                                 📖 AVAILABLE COMMANDS                                ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════════════════════════════╣\n");
        sb.append("║                                                                                      ║\n");
        sb.append("║    📁 FILE COMMANDS:                                                                 ║\n");
        sb.append("║    open <file>                           - Opens a single XML file                   ║\n");
        sb.append("║    openall                               - Opens all XML files                       ║\n");
        sb.append("║    save                                  - Saves the currently open file             ║\n");
        sb.append("║    saveall                               - Saves all XML files                       ║\n");
        sb.append("║    saveas <file>                         - Saves the current file with a new name    ║\n");
        sb.append("║    close                                 - Closes the currently open file            ║\n");
        sb.append("║    closeall                              - Closes all open files                     ║\n");
        sb.append("║    help                                  - Shows this help message                   ║\n");
        sb.append("║    exit                                  - Exits the program                         ║\n");
        sb.append("║                                                                                      ║\n");
        sb.append("║    🏫 SPECIALTY COMMANDS:                                                            ║\n");
        sb.append("║    addspecialty \"<name>\" <minCredits>  - Adds a new specialty                      ║\n");
        sb.append("║    listspecialties                       - Lists all specialties                     ║\n");
        sb.append("║    removespecialty \"<name>\"            - Removes a specialty                       ║\n");
        sb.append("║                                                                                      ║\n");
        sb.append("║    📚 DISCIPLINE COMMANDS:                                                           ║\n");
        sb.append("║    adddiscipline <name> <type> <credits> <courses> - Adds a discipline               ║\n");
        sb.append("║    listdisciplines                       - Lists all disciplines                     ║\n");
        sb.append("║    removediscipline <name>               - Removes a discipline                      ║\n");
        sb.append("║                                                                                      ║\n");
        sb.append("║    👨‍🎓 STUDENT COMMANDS:                                                              ║\n");
        sb.append("║    enroll <fn> <program> <group> <name>  - Enrolls a student                         ║\n");
        sb.append("║    print <fn>                            - Prints student information                ║\n");
        sb.append("║    printall <program> <year>             - Prints all students in program/year       ║\n");
        sb.append("║    advance <fn>                          - Advances student to next course           ║\n");
        sb.append("║    graduate <fn>                         - Marks student as graduated                ║\n");
        sb.append("║    interrupt <fn>                        - Interrupts a student                      ║\n");
        sb.append("║    resume <fn>                           - Resumes an interrupted student            ║\n");
        sb.append("║    change <fn> <option> <value>          - Changes program/group/year                ║\n");
        sb.append("║                                                                                      ║\n");
        sb.append("║    📝 GRADE COMMANDS:                                                                ║\n");
        sb.append("║    enrollin <fn> <course>                - Enrolls student in discipline             ║\n");
        sb.append("║    addgrade <fn> <course> <grade>        - Adds a grade for a student                ║\n");
        sb.append("║    protocol <course>                     - Shows protocol for a discipline           ║\n");
        sb.append("║    report <fn>                           - Shows academic report for a student       ║\n");
        sb.append("║                                                                                      ║\n");
        sb.append("╚══════════════════════════════════════════════════════════════════════════════════════╝\n");

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