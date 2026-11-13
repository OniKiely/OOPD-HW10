import java.util.*;

/** A chat participant (Memento originator). Never talks to other users directly. */
public class User implements IterableByUser {
    private final String name;
    private ChatMediator mediator;
    private final ChatHistory history = new ChatHistory();
    private final Deque<MessageMemento> sentMementos = new ArrayDeque<>();

    public User(String name) { this.name = name; }

    public String getName() { return name; }
    public ChatHistory getHistory() { return history; }
    public void setMediator(ChatMediator mediator) { this.mediator = mediator; }

    public void send(String content, User... to) {
        mediator.sendMessage(this, Arrays.asList(to), content);
    }

    /** Called by the mediator: stores the sent message and saves a memento of it. */
    void recordSent(Message m) {
        history.addMessage(m);
        sentMementos.push(new MessageMemento(m.getContent(), m.getTimestamp()));
    }

    /** Called by the mediator when a message is delivered. */
    public void receive(Message m) {
        history.addMessage(m);
        System.out.println("  " + name + " received: " + m);
    }

    /** Undo the last message this user sent (restores state from the memento). */
    public void undoLastMessage() {
        MessageMemento memento = sentMementos.poll();
        if (memento == null) {
            System.out.println("  " + name + ": nothing to undo.");
            return;
        }
        System.out.println("  " + name + " undoes: \"" + memento.getContent() + "\"");
        ((ChatServer) mediator).retractMessage(this, memento);
    }

    public void block(User other) { mediator.blockUser(this, other); }
    public void unblock(User other) { mediator.unblockUser(this, other); }

    public void viewHistory() {
        System.out.println("  --- Chat history for " + name + " ---");
        List<Message> all = history.getMessages();
        if (all.isEmpty()) System.out.println("  (empty)");
        for (Message m : all) System.out.println("  " + m);
    }

    /** Iterate over this user's history by messages sent/received by the given user. */
    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return history.iterator(userToSearchWith);
    }

    @Override
    public String toString() { return name; }
}
