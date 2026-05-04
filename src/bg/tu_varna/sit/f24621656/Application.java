package bg.tu_varna.sit.f24621656;

import bg.tu_varna.sit.f24621656.commands.CommandParser;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.contracts.DataRepository;
import bg.tu_varna.sit.f24621656.models.University;
import bg.tu_varna.sit.f24621656.session.Session;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Application {
  public static void main(String[] args) {
    DataRepository repository = new University();
    Session session = new Session(repository);
    CommandParser parser = new CommandParser(session);
    Scanner scanner = new Scanner(System.in);

    System.out.println("╔════════════════════════════════════════════════════════════════════════╗");
    System.out.println("║                    STUDENT INFORMATION SYSTEM                         ║");
    System.out.println("╚════════════════════════════════════════════════════════════════════════╝");
    System.out.println();

    System.out.println("📁 Data will be stored in the following files:");

    if (Files.exists(Paths.get("specialties.xml"))) {
      System.out.println("  ✓ specialties.xml");
    } else {
      System.out.println("  • specialties.xml (not created yet)");
    }

    if (Files.exists(Paths.get("disciplines.xml"))) {
      System.out.println("  ✓ disciplines.xml");
    } else {
      System.out.println("  • disciplines.xml (not created yet)");
    }

    if (Files.exists(Paths.get("students.xml"))) {
      System.out.println("  ✓ students.xml");
    } else {
      System.out.println("  • students.xml (not created yet)");
    }

    System.out.println();
    System.out.println("💡 Type 'help' for available commands");
    System.out.println("📂 Use 'open <file>' to load/save data");
    System.out.println();

    while (true) {
      System.out.print("> ");
      String input = scanner.nextLine();
      CommandResult result = parser.parseAndExecute(input);

      if (result.getMessage() != null && !result.getMessage().isEmpty()) {
        if (result.isSuccess()) {
          System.out.println(result.getMessage());
        } else {
          System.err.println(result.getMessage());
        }
      }

      if (input.trim().equalsIgnoreCase("exit")) {
        break;
      }
    }

    scanner.close();
  }
}