import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Stores the messages a user has sent and received
public class ChatHistory implements IterableByUser {
    private final List<Message> messages = new ArrayList<>();

    public void addMessage(Message m) {
        messages.add(m);
    }

    public boolean removeMessage(MessageMemento memento) {
        return messages.removeIf(memento::matches);
    }

    public Message getLastMessage() {
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }

    // The most recent message sent by the given user,
    public Message getLastMessageSent(User sender) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i).getSender().equals(sender)) return messages.get(i);
        }
        return null;
    }

    public List<Message> getMessages() { return new ArrayList<>(messages); }

    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return new searchMessagesByUser(messages, userToSearchWith);
    }
}
