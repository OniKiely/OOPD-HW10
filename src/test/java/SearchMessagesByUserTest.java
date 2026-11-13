import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class SearchMessagesByUserTest {
    private User a, b, c;
    private List<Message> list;
    private Message ab, bc, ca;

    @Before public void setUp() {
        a = new User("A"); b = new User("B"); c = new User("C");
        ab = new Message(a, Arrays.asList(b), "ab");
        bc = new Message(b, Arrays.asList(c), "bc");
        ca = new Message(c, Arrays.asList(a), "ca");
        list = Arrays.asList(ab, bc, ca);
    }
    @Test public void hasNext() {
        assertTrue(new searchMessagesByUser(list, a).hasNext());
        assertFalse(new searchMessagesByUser(new ArrayList<Message>(), a).hasNext());
        assertFalse(new searchMessagesByUser(Arrays.asList(bc), a).hasNext());
    }
    @Test public void next() {
        Iterator<Message> it = new searchMessagesByUser(list, a);
        assertSame(ab, it.next());
        assertSame(ca, it.next());      // skips bc
        try { it.next(); fail(); } catch (NoSuchElementException expected) { }
    }
}
