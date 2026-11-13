import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/** A message sent by a user to one or more recipients. */
public class Message {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static LocalDateTime lastStamp = LocalDateTime.MIN;

    private final User sender;
    private final List<User> recipients;
    private final LocalDateTime timestamp;
    private final String content;

    public Message(User sender, List<User> recipients, String content) {
        this.sender = sender;
        this.recipients = Collections.unmodifiableList(recipients);
        this.timestamp = nextTimestamp();
        this.content = content;
    }

    /** Guarantees strictly increasing timestamps so a message can be identified by (sender, timestamp). */
    private static synchronized LocalDateTime nextTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        if (!now.isAfter(lastStamp)) now = lastStamp.plusNanos(1000);
        lastStamp = now;
        return now;
    }

    public User getSender() { return sender; }
    public List<User> getRecipients() { return recipients; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getContent() { return content; }

    /** True if the given user sent or was a recipient of this message. */
    public boolean involves(User user) {
        return sender.equals(user) || recipients.contains(user);
    }

    @Override
    public String toString() {
        StringBuilder to = new StringBuilder();
        for (User u : recipients) {
            if (to.length() > 0) to.append(", ");
            to.append(u.getName());
        }
        return "[" + timestamp.format(FMT) + "] " + sender.getName() + " -> " + to + ": " + content;
    }
}
