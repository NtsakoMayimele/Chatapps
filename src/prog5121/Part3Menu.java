package prog5121;

import java.util.Scanner;

/**
 * Handles the "Stored Messages" menu (Option 3) in QuickChat Part 3.
 */
public class Part3Menu {

    private Scanner scanner;
    private String  senderName;

    public Part3Menu(Scanner scanner, String senderName) {
        this.scanner    = scanner;
        this.senderName = senderName;
    }

    public void showMenu() {
        // Reload from JSON every time the menu opens
        Message.loadStoredMessagesFromJSON("messages.json");

        boolean running = true;
        while (running) {
            System.out.println("\n===== Stored Messages =====");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message by ID");
            System.out.println("d) Search messages for a particular recipient");
            System.out.println("e) Delete a message using message hash");
            System.out.println("f) Display full report of all stored messages");
            System.out.println("0) Back to main menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    System.out.println("\n" + Message.displayStoredSenderRecipient(senderName));
                    break;

                case "b":
                    System.out.println("\nLongest stored message:");
                    System.out.println(Message.getLongestStoredMessage());
                    break;

                case "c":
                    System.out.print("Enter Message ID: ");
                    String id = scanner.nextLine().trim();
                    System.out.println("\n" + Message.searchByMessageID(id));
                    break;

                case "d":
                    System.out.print("Enter recipient number: ");
                    String recipient = scanner.nextLine().trim();
                    System.out.println("\nMessages for " + recipient + ":");
                    System.out.println(Message.searchByRecipient(recipient));
                    break;

                case "e":
                    System.out.print("Enter message hash to delete: ");
                    String hash = scanner.nextLine().trim();
                    System.out.println("\n" + Message.deleteByHash(hash));
                    break;

                case "f":
                    System.out.println("\n" + Message.displayStoredReport());
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}