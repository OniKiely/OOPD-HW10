import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class ChatHistoryTest {
    private User a, b, c;
    private ChatHistory h;
    private Message m1, m2;

    @Before public void setUp() {
        a = new User("A"); b = new User("B"); c = new User("C");
        h = new ChatHistory();
        m1 = new Message(a, Arrays.asList(b), "one");
        m2 = new Message(b, Arrays.asList(a), "two");
    }
    @Test public void addMessage() {
        h.addMessage(m1);
        assertEquals(1, h.getMessages().size());
    }
    @Test public void removeMessage() {
        h.addMessage(m1); h.addMessage(m2);
        assertTrue(h.removeMessage(new MessageMemento("one", m1.getTimestamp())));
        assertEquals(Arrays.asList(m2), h.getMessages());
        assertFalse(h.removeMessage(new MessageMemento("one", m1.getTimestamp())));
    }
    @Test public void getLastMessage() {
        assertNull(h.getLastMessage());
        h.addMessage(m1); h.addMessage(m2);
        assertSame(m2, h.getLastMessage());
    }
    @Test public void getLastMessageSent() {
        h.addMessage(m1); h.addMessage(m2);
        assertSame(m1, h.getLastMessageSent(a));
        assertNull(h.getLastMessageSent(c));
    }
    @Test public void getMessages_returnsCopy() {
        h.addMessage(m1);
        h.getMessages().clear();
        assertEquals(1, h.getMessages().size());
    }
    @Test public void iterator() {
        h.addMessage(m1);
        h.addMessage(new Message(b, Arrays.asList(c), "b to c"));
        Iterator<Message> it = h.iterator(a);
        assertTrue(it.hasNext());
        assertSame(m1, it.next());
        assertFalse(it.hasNext());
    }
}
