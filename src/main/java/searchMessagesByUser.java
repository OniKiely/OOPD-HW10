import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

// Iterator:
public class searchMessagesByUser implements Iterator<Message> {
    private final List<Message> messages;
    private final User user;
    private int position = 0;

    public searchMessagesByUser(List<Message> messages, User user) {
        this.messages = messages;
        this.user = user;
    }

    @Override
    public boolean hasNext() {
        while (position < messages.size()) {
            if (messages.get(position).involves(user)) return true;
            position++;
        }
        return false;
    }

    @Override
    public Message next() {
        if (!hasNext()) throw new NoSuchElementException();
        return messages.get(position++);
    }
}
