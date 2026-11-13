import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Stores the messages a user has sent and received. Also acts as the Memento caretaker. */
public class ChatHistory implements IterableByUser {
    private final List<Message> messages = new ArrayList<>();

    public void addMessage(Message m) { messages.add(m); }

    /** Removes the message the memento was taken from; returns true if it was present. */
    public boolean removeMessage(MessageMemento memento) {
        return messages.removeIf(memento::matches);
    }

    /** The most recent message in this history, or null if empty. */
    public Message getLastMessage() {
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }

    /** The most recent message sent by the given user, or null if none. */
    public Message getLastMessageSent(User sender) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i).getSender().equals(sender)) return messages.get(i);
        }
        return null;
    }

    public List<Message> getMessages() { return new ArrayList<>(messages); }

    /** Iterates over messages sent or received by userToSearchWith. */
    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return new searchMessagesByUser(messages, userToSearchWith);
    }
}
