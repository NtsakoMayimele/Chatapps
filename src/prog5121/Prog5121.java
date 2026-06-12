// Main class for QuickChat - handles registration, login and messaging
package prog5121;

import java.util.Scanner;

public class Prog5121 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("       Welcome to the Chat App          ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        // ===== PART 1: Registration =====
        System.out.println("\n==== Registration =====");
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Enter a username (must contain _ and be 5 chars or less): ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter a password (8+ chars, capital, number, special char): ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter your cell phone number (e.g. +27838968976): ");
        String cellPhone = scanner.nextLine().trim();

        login user = new login(firstName, lastName, username, password, cellPhone);
        String registrationResult = user.registerUser();
        System.out.println("\n" + registrationResult);

        if (!registrationResult.contains("successfully")) {
            System.out.println("Registration failed. Please restart and try again.");
            scanner.close();
            return;
        }

        // ===== PART 1: Login =====
        System.out.println("\n==== Login ====");
        System.out.print("Enter your username: ");
        String loginUsername = scanner.nextLine().trim();
        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine().trim();

        String loginStatus = user.returnLoginStatus(loginUsername, loginPassword);
        System.out.println("\n" + loginStatus);

        if (!user.loginUser(loginUsername, loginPassword)) {
            System.out.println("Login failed. Please restart and try again.");
            scanner.close();
            return;
        }

        // ===== PART 2 & 3: Messaging =====
        System.out.println("\nWelcome to QuickChat.");

        // Load any previously stored messages from JSON at startup
        Message.loadStoredMessagesFromJSON("messages.json");

        int maxMessages = 0;
        while (maxMessages <= 0) {
            try {
                System.out.print("\nHow many messages would you like to send? ");
                maxMessages = Integer.parseInt(scanner.nextLine().trim());
                if (maxMessages <= 0) {
                    System.out.println("Please enter a number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        Message msgUtil = new Message();
        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");   // Part 3
            System.out.println("0) Quit");
            System.out.print("Choose an option: ");
            String menuChoice = scanner.nextLine().trim();

            switch (menuChoice) {

                case "1":
                    if (Message.getMessageCounter() >= maxMessages) {
                        System.out.println("You have reached your message limit of " + maxMessages + " message(s).");
                        break;
                    }

                    String recipient = "";
                    while (true) {
                        System.out.print("Enter recipient number (e.g. +27XXXXXXXXX): ");
                        recipient = scanner.nextLine().trim();
                        Message recipientCheck = new Message(0, recipient, "x");
                        if (recipientCheck.checkRecipientCell().contains("successfully")) {
                            break;
                        }
                        System.out.println(recipientCheck.checkRecipientCell());
                    }

                    String messageText = "";
                    while (true) {
                        System.out.print("Enter your message (max 250 characters): ");
                        messageText = scanner.nextLine().trim();
                        Message lengthCheck = new Message(0, recipient, messageText);
                        if (lengthCheck.checkMessageLength().equals("Message ready to send.")) {
                            break;
                        }
                        System.out.println(lengthCheck.checkMessageLength());
                    }

                    Message newMsg = new Message(Message.getMessageCounter(), recipient, messageText);
                    System.out.println("\nMessage ID generated: " + newMsg.getMessageID());

                    System.out.println("\n1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("3) Store Message to send later");
                    System.out.print("Choose: ");
                    String sendChoice = scanner.nextLine().trim();

                    String sendResult = newMsg.sentMessage(sendChoice);
                    System.out.println("\n" + sendResult);

                    if (sendChoice.equals("1")) {
                        System.out.println("\nMessage ID: "  + newMsg.getMessageID());
                        System.out.println("Message Hash: " + newMsg.getMessageHash());
                        System.out.println("Recipient: "    + newMsg.getRecipient());
                        System.out.println("Message: "      + newMsg.getMessageText());
                    } else if (sendChoice.equals("2")) {
                        System.out.print("Enter 0 to confirm delete: ");
                        scanner.nextLine();
                    }
                    break;

                case "2":
                    System.out.println("\n--- Recently Sent Messages ---");
                    System.out.println(msgUtil.printMessages());
                    break;

                case "3":
                    // Part 3 — Stored Messages sub-menu
                    Part3Menu part3 = new Part3Menu(scanner, firstName + " " + lastName);
                    part3.showMenu();
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please enter 1, 2, 3, or 0.");
            }
        }

        System.out.println("\n==========================================");
        System.out.println("Total messages sent: " + msgUtil.returnTotalMessagess());
        if (msgUtil.returnTotalMessagess() > 0) {
            System.out.println("\n--- Messages Sent This Session ---");
            System.out.println(msgUtil.printMessages());
        }
        System.out.println("==========================================");

        scanner.close();
    }
}