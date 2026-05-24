import prog5121.Message;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    // Reset static state after every test so tests don't interfere with each other
    @After
    public void tearDown() {
        Message.clearMessages();
    }

    // ===========================
    //   assertEquals Tests
    // ===========================

    @Test
    public void testMessageLength_Success() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    public void testMessageLength_Failure() {
        // 260 characters — exceeds limit by 10
        String longMessage = "A".repeat(260);
        Message msg = new Message(0, "+27718693002", longMessage);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", msg.checkMessageLength());
    }

    @Test
    public void testRecipientCell_Success() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testRecipientCell_Failure() {
        // No international code — should fail
        Message msg = new Message(0, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. "
            + "Please correct the number and try again.",
            msg.checkRecipientCell()
        );
    }

    @Test
    public void testMessageHash_Correct() {
        // Force messageID to start with "00" so the hash is deterministic
        Message msg = new Message(0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?", "0012345678");
        assertEquals("00:0:HITONIGHT", msg.createMessageHash());
    }

    @Test
    public void testMessageID_Created() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        // The ID must be generated and no more than 10 characters
        System.out.println("Message ID generated: " + msg.getMessageID());
        assertTrue(msg.checkMessageID());
    }

    @Test
    public void testSentMessage_Send() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", msg.sentMessage("1"));
    }

    @Test
    public void testSentMessage_Disregard() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Press 0 to delete the message.", msg.sentMessage("2"));
    }

    @Test
    public void testSentMessage_Store() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", msg.sentMessage("3"));
    }

    // ===========================
    //   assertTrue / assertFalse Tests
    // ===========================

    @Test
    public void testMessageID_Valid() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(msg.checkMessageID());
    }

    @Test
    public void testRecipientCell_ValidReturnsTrue() {
        Message msg = new Message(0, "+27718693002", "test");
        assertTrue(msg.checkRecipientCell().contains("successfully"));
    }

    @Test
    public void testRecipientCell_InvalidReturnsFalse() {
        Message msg = new Message(0, "08575975889", "test");
        assertFalse(msg.checkRecipientCell().contains("successfully"));
    }
}