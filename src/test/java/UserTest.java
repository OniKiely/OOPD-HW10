import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import org.junit.*;

public class UserTest {
    private ChatServer s;
    private User a, b, c;

    @Before public void setUp() {
        s = new ChatServer();
        a = new User("A"); b = new User("B"); c = new User("C");
        s.registerUser(a); s.registerUser(b); s.registerUser(c);
    }
    @Test public void getName() { assertEquals("A", a.getName()); }
    @Test public void getHistory() { assertNotNull(a.getHistory()); }
    @Test public void setMediator() {
        User d = new User("D");
        d.setMediator(s);
        d.send("x", a);   // would NPE without a mediator
    }
    @Test public void send() {
        a.send("hi", b, c);
        assertEquals(1, b.getHistory().getMessages().size());
        assertEquals(1, c.getHistory().getMessages().size());
    }
    @Test public void recordSent() {
        Message m = new Message(a, Arrays.asList(b), "x");
        a.recordSent(m);
        assertSame(m, a.getHistory().getLastMessage());
        a.undoLastMessage();                       // memento was saved
        assertTrue(a.getHistory().getMessages().isEmpty());
    }
    @Test public void receive() {
        Message m = new Message(b, Arrays.asList(a), "x");
        a.receive(m);
        assertSame(m, a.getHistory().getLastMessage());
    }
    @Test public void undoLastMessage() {
        a.send("first", b);
        a.send("second", b);
        a.undoLastMessage();
        assertEquals("first", b.getHistory().getLastMessage().getContent());
        assertEquals("first", a.getHistory().getLastMessage().getContent());
    }
    @Test public void undoLastMessage_nothingToUndo() {
        a.undoLastMessage();   // must not throw
        assertTrue(a.getHistory().getMessages().isEmpty());
    }
    @Test public void block() {
        b.block(a);
        a.send("hi", b);
        assertTrue(b.getHistory().getMessages().isEmpty());
    }
    @Test public void unblock() {
        b.block(a);
        b.unblock(a);
        a.send("hi", b);
        assertEquals(1, b.getHistory().getMessages().size());
    }
    @Test public void viewHistory() {
        a.send("visible", b);
        PrintStream old = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try { a.viewHistory(); } finally { System.setOut(old); }
        assertTrue(out.toString().contains("visible"));
    }
    @Test public void iterator() {
        a.send("to b", b);
        a.send("to c", c);
        Iterator<Message> it = a.iterator(b);
        assertEquals("to b", it.next().getContent());
        assertFalse(it.hasNext());
    }
    @Test public void toString_isName() { assertEquals("A", a.toString()); }
}
