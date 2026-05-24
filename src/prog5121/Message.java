// Message class for QuickChat Part 2 - handles messaging featurespackage prog5121;

import java.util.ArrayList;
import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static int messageCounter = 0;

    public Message() {
        this.messageID = "";
        this.messageNumber = 0;
        this.recipient = "";
        this.messageText = "";
        this.messageHash = "";
    }

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    Message(int messageNumber, String recipient, String messageText, String forcedMessageID) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = forcedMessageID;
        this.messageHash = createMessageHash();
    }

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
        String firstTwo = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "").toUpperCase();
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "").toUpperCase();
        return firstTwo + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    public String sentMessage(String choice) {
        switch (choice) {
            case "1":
                sentMessages.add(this);
                messageCounter++;
                return "Message successfully sent.";
            case "2":
                return "Press 0 to delete the message.";
            case "3":
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
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

    public void storeMessage() {
        storedMessages.add(this);
        System.out.println("Message stored successfully.");
    }

    public String getMessageID()    { return messageID; }
    public String getMessageHash()  { return messageHash; }
    public String getRecipient()    { return recipient; }
    public String getMessageText()  { return messageText; }
    public int getMessageNumber()   { return messageNumber; }

    public static int getMessageCounter() { return messageCounter; }

    public static void clearMessages() {
        sentMessages.clear();
        storedMessages.clear();
        messageCounter = 0;
    }
}