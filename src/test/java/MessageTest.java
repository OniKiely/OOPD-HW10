import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class MessageTest {
    private User a, b, c;
    private Message m;

    @Before public void setUp() {
        a = new User("A"); b = new User("B"); c = new User("C");
        m = new Message(a, Arrays.asList(b), "hello");
    }
    @Test public void constructor_setsFieldsAndTimestampsIncrease() {
        Message m2 = new Message(a, Arrays.asList(b), "again");
        assertTrue(m2.getTimestamp().isAfter(m.getTimestamp()));
    }
    @Test public void getSender() { assertSame(a, m.getSender()); }
    @Test public void getRecipients() {
        assertEquals(Arrays.asList(b), m.getRecipients());
        try { m.getRecipients().add(c); fail(); } catch (UnsupportedOperationException expected) { }
    }
    @Test public void getTimestamp() { assertNotNull(m.getTimestamp()); }
    @Test public void getContent() { assertEquals("hello", m.getContent()); }
    @Test public void involves() {
        assertTrue(m.involves(a));
        assertTrue(m.involves(b));
        assertFalse(m.involves(c));
    }
    @Test public void toString_containsSenderRecipientContent() {
        String s = m.toString();
        assertTrue(s.contains("A -> B: hello"));
    }
}
