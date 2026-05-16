package bg.tu_varna.sit.f24621656;

import bg.tu_varna.sit.f24621656.commands.CommandParser;
import bg.tu_varna.sit.f24621656.commands.CommandResult;
import bg.tu_varna.sit.f24621656.contracts.DataRepository;
import bg.tu_varna.sit.f24621656.models.University;
import bg.tu_varna.sit.f24621656.session.Session;

import java.util.Scanner;

/**
 * Main application entry point.
 * Initializes the repository, session, command parser and starts the interactive console loop.
 *
 * @author Lazar Tsvetkov
 */
public class Application {
  /**
   * The main method that starts the student information system.
   * Displays a welcome message, reads user input from the console,
   * parses and executes commands until "exit" is entered.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    DataRepository repository = new University();
    Session session = new Session(repository);
    CommandParser parser = new CommandParser(session);
    Scanner scanner = new Scanner(System.in);

    System.out.println("=========================================");
    System.out.println("   Student Information System");
    System.out.println("=========================================");
    System.out.println("Type 'help' for available commands");
    System.out.println("Use 'open <file>' to load/save data");
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