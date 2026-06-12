// Message class for QuickChat Part 2 & 3 - no external libraries needed
package prog5121;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Message {

    // ─── Fields ───────────────────────────────────────────────────────────────
    private String messageID;
    private int    messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // ─── Part 2 static lists ──────────────────────────────────────────────────
    private static ArrayList<Message> sentMessages   = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static int messageCounter = 0;

    // ─── Part 3 arrays ────────────────────────────────────────────────────────
    private static ArrayList<String>  sentMessageArray     = new ArrayList<>();
    private static ArrayList<String>  disregardedMessages  = new ArrayList<>();
    private static ArrayList<Message> storedMessageObjects = new ArrayList<>();
    private static ArrayList<String>  messageHashes        = new ArrayList<>();
    private static ArrayList<String>  messageIDs           = new ArrayList<>();

    // ─── Constructors ─────────────────────────────────────────────────────────

    public Message() {
        this.messageID     = "";
        this.messageNumber = 0;
        this.recipient     = "";
        this.messageText   = "";
        this.messageHash   = "";
    }

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageID     = generateMessageID();
        this.messageHash   = createMessageHash();
    }

    Message(int messageNumber, String recipient, String messageText, String forcedMessageID) {
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageID     = forcedMessageID;
        this.messageHash   = createMessageHash();
    }

    // ─── Part 2 Methods ───────────────────────────────────────────────────────

    private String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long) (rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+[0-9]{9,12}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (messageText == null) {
            return "Message exceeds 250 characters by 0; please reduce the size.";
        }
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = messageText.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    public String createMessageHash() {
        if (messageID == null || messageID.isEmpty() || messageText == null || messageText.isEmpty()) {
            return "";
        }
        String firstTwo  = messageID.substring(0, 2);
        String[] words   = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "").toUpperCase();
        String lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z]", "").toUpperCase();
        return firstTwo + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    public String sentMessage(String choice) {
        switch (choice) {
            case "1":
                sentMessages.add(this);
                sentMessageArray.add(this.messageText);
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                messageCounter++;
                return "Message successfully sent.";
            case "2":
                disregardedMessages.add(this.messageText);
                return "Press 0 to delete the message.";
            case "3":
                storeMessage();
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    public void storeMessage() {
        storedMessages.add(this);
        storedMessageObjects.add(this);
        saveToFile();
        System.out.println("Message stored successfully.");
    }

    // Saves stored messages to messages.txt using simple pipe-delimited format
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("messages.txt"))) {
            for (Message m : storedMessages) {
                // Format: messageID|messageHash|recipient|messageText
                writer.println(m.messageID + "|" + m.messageHash + "|" + m.recipient + "|" + m.messageText);
            }
        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
        }
    }

    // Loads stored messages from messages.txt
    public static void loadStoredMessagesFromJSON(String filePath) {
        storedMessageObjects.clear();
        // Use messages.txt instead regardless of what path is passed
        File file = new File("messages.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|", 4);
                if (parts.length == 4) {
                    String id   = parts[0];
                    String hash = parts[1];
                    String rec  = parts[2];
                    String text = parts[3];
                    Message m   = new Message(0, rec, text, id);
                    m.messageHash = hash;
                    storedMessageObjects.add(m);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load stored messages: " + e.getMessage());
        }
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient: ").append(m.recipient).append("\n");
            sb.append("Message: ").append(m.messageText).append("\n");
        }
        return sb.toString().trim();
    }

    public int returnTotalMessagess() {
        return sentMessages.size();
    }

    // ─── Part 3 Methods ───────────────────────────────────────────────────────

    /** a. Display sender and recipient of all stored messages. */
    public static String displayStoredSenderRecipient(String senderName) {
        if (storedMessageObjects.isEmpty()) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessageObjects) {
            sb.append("Sender: ").append(senderName)
              .append(" | Recipient: ").append(m.recipient).append("\n");
        }
        return sb.toString().trim();
    }

    /** b. Return the longest stored message. */
    public static String getLongestStoredMessage() {
        if (storedMessageObjects.isEmpty()) {
            return "No stored messages found.";
        }
        Message longest = storedMessageObjects.get(0);
        for (Message m : storedMessageObjects) {
            if (m.messageText.length() > longest.messageText.length()) {
                longest = m;
            }
        }
        return longest.messageText;
    }

    /** c. Search by message ID. */
    public static String searchByMessageID(String id) {
        for (Message m : sentMessages) {
            if (m.messageID.equals(id)) {
                return "Recipient: " + m.recipient + "\nMessage: " + m.messageText;
            }
        }
        for (Message m : storedMessageObjects) {
            if (m.messageID.equals(id)) {
                return "Recipient: " + m.recipient + "\nMessage: " + m.messageText;
            }
        }
        return "Message ID not found.";
    }

    /** d. Search all sent or stored messages for a particular recipient. */
    public static String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            if (m.recipient.equals(recipient)) {
                sb.append(m.messageText).append("\n");
            }
        }
        for (Message m : storedMessageObjects) {
            if (m.recipient.equals(recipient)) {
                sb.append(m.messageText).append("\n");
            }
        }
        if (sb.length() == 0) {
            return "No messages found for recipient: " + recipient;
        }
        return sb.toString().trim();
    }

    /** e. Delete a message using its hash. */
    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedMessageObjects.size(); i++) {
            Message m = storedMessageObjects.get(i);
            if (m.messageHash.equals(hash)) {
                String text = m.messageText;
                storedMessageObjects.remove(i);
                storedMessages.removeIf(msg -> msg.messageHash.equals(hash));
                messageHashes.remove(hash);
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        for (int i = 0; i < sentMessages.size(); i++) {
            Message m = sentMessages.get(i);
            if (m.messageHash.equals(hash)) {
                String text = m.messageText;
                sentMessages.remove(i);
                sentMessageArray.removeIf(t -> t.equals(text));
                messageHashes.remove(hash);
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "Hash not found. No message deleted.";
    }

    /** f. Display full report of all stored messages. */
    public static String displayStoredReport() {
        if (storedMessageObjects.isEmpty()) {
            return "No stored messages to report.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=== Stored Messages Report ===\n");
        for (Message m : storedMessageObjects) {
            sb.append("Message ID:   ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient:    ").append(m.recipient).append("\n");
            sb.append("Message:      ").append(m.messageText).append("\n");
            sb.append("-----------------------------\n");
        }
        return sb.toString().trim();
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public String getMessageID()    { return messageID; }
    public String getMessageHash()  { return messageHash; }
    public String getRecipient()    { return recipient; }
    public String getMessageText()  { return messageText; }
    public int    getMessageNumber(){ return messageNumber; }

    public static int                getMessageCounter()       { return messageCounter; }
    public static ArrayList<String>  getSentMessageArray()     { return sentMessageArray; }
    public static ArrayList<String>  getDisregardedMessages()  { return disregardedMessages; }
    public static ArrayList<Message> getStoredMessageObjects() { return storedMessageObjects; }
    public static ArrayList<String>  getMessageHashes()        { return messageHashes; }
    public static ArrayList<String>  getMessageIDs()           { return messageIDs; }

    // ─── Utility ──────────────────────────────────────────────────────────────

    public static void clearMessages() {
        sentMessages.clear();
        storedMessages.clear();
        sentMessageArray.clear();
        disregardedMessages.clear();
        storedMessageObjects.clear();
        messageHashes.clear();
        messageIDs.clear();
        messageCounter = 0;
    }

    public static void addToSentMessages(Message m) {
        sentMessages.add(m);
        sentMessageArray.add(m.getMessageText());
    }

    public static void addToStoredObjects(Message m) {
        storedMessageObjects.add(m);
    }
}