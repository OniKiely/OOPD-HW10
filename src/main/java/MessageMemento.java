import java.time.LocalDateTime;

/** Memento: an immutable snapshot of a message a user sent (content + timestamp). */
public class MessageMemento {
    private final String content;
    private final LocalDateTime timestamp;

    public MessageMemento(String content, LocalDateTime timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    /** True if the given message is the one this memento was taken from. */
    public boolean matches(Message m) {
        return m.getTimestamp().equals(timestamp) && m.getContent().equals(content);
    }
}
