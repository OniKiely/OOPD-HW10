import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class ChatServerTest {
    private ChatServer s;
    private User a, b, c;

    @Before public void setUp() {
        s = new ChatServer();
        a = new User("A"); b = new User("B"); c = new User("C");
        s.registerUser(a); s.registerUser(b); s.registerUser(c);
    }
    @Test public void registerUser() {
        User d = new User("D");
        s.registerUser(d);
        d.send("hi", a);
        assertEquals(1, a.getHistory().getMessages().size());
    }
    @Test public void unregisterUser() {
        s.unregisterUser(b);
        a.send("hi", b);
        assertTrue(b.getHistory().getMessages().isEmpty());
    }
    @Test public void blockUser() {
        s.blockUser(b, a);
        assertTrue(s.isBlocked(b, a));
    }
    @Test public void unblockUser() {
        s.blockUser(b, a);
        s.unblockUser(b, a);
        assertFalse(s.isBlocked(b, a));
    }
    @Test public void isBlocked() {
        assertFalse(s.isBlocked(b, a));
        s.blockUser(b, a);
        assertTrue(s.isBlocked(b, a));
        assertFalse(s.isBlocked(a, b));   // not symmetric
    }
    @Test public void sendMessage() {
        s.sendMessage(a, Arrays.asList(b, c), "hey");
        assertEquals(1, a.getHistory().getMessages().size());
        assertEquals(1, b.getHistory().getMessages().size());
        assertEquals(1, c.getHistory().getMessages().size());
    }
    @Test public void sendMessage_skipsBlockedRecipient() {
        s.blockUser(b, a);
        s.sendMessage(a, Arrays.asList(b, c), "hey");
        assertTrue(b.getHistory().getMessages().isEmpty());
        assertEquals(1, c.getHistory().getMessages().size());
    }
    @Test public void retractMessage() {
        s.sendMessage(a, Arrays.asList(b), "oops");
        Message sent = a.getHistory().getLastMessage();
        s.retractMessage(a, new MessageMemento("oops", sent.getTimestamp()));
        assertTrue(a.getHistory().getMessages().isEmpty());
        assertTrue(b.getHistory().getMessages().isEmpty());
    }
}
