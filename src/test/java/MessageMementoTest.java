import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class MessageMementoTest {
    private Message m;
    private MessageMemento memento;

    @Before public void setUp() {
        User a = new User("A"), b = new User("B");
        m = new Message(a, Arrays.asList(b), "hi");
        memento = new MessageMemento(m.getContent(), m.getTimestamp());
    }
    @Test public void getContent() { assertEquals("hi", memento.getContent()); }
    @Test public void getTimestamp() { assertEquals(m.getTimestamp(), memento.getTimestamp()); }
    @Test public void matches() {
        assertTrue(memento.matches(m));
        Message other = new Message(m.getSender(), m.getRecipients(), "hi");
        assertFalse(memento.matches(other));
    }
}
