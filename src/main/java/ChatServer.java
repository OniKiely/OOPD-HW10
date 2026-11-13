import java.util.*;

/** Mediator: all user-to-user communication goes through here. */
public class ChatServer implements ChatMediator {
    private final Set<User> users = new LinkedHashSet<>();
    // blocker -> set of users whose messages the blocker does not want to receive
    private final Map<User, Set<User>> blocked = new HashMap<>();

    @Override
    public void registerUser(User user) {
        if (users.add(user)) {
            user.setMediator(this);
            System.out.println("[Server] " + user.getName() + " registered.");
        }
    }

    @Override
    public void unregisterUser(User user) {
        if (users.remove(user)) {
            blocked.remove(user);
            System.out.println("[Server] " + user.getName() + " unregistered.");
        }
    }

    @Override
    public void blockUser(User blocker, User toBlock) {
        blocked.computeIfAbsent(blocker, k -> new HashSet<>()).add(toBlock);
        System.out.println("[Server] " + blocker.getName() + " now blocks " + toBlock.getName() + ".");
    }

    @Override
    public void unblockUser(User blocker, User toUnblock) {
        Set<User> set = blocked.get(blocker);
        if (set != null && set.remove(toUnblock)) {
            System.out.println("[Server] " + blocker.getName() + " unblocked " + toUnblock.getName() + ".");
        }
    }

    public boolean isBlocked(User recipient, User sender) {
        return blocked.getOrDefault(recipient, Collections.emptySet()).contains(sender);
    }

    /** Creates the message, records it for the sender, and forwards it to each eligible recipient. */
    @Override
    public void sendMessage(User sender, List<User> recipients, String content) {
        if (!users.contains(sender)) {
            System.out.println("[Server] " + sender.getName() + " is not registered; message rejected.");
            return;
        }
        Message message = new Message(sender, recipients, content);
        sender.recordSent(message);
        for (User r : recipients) {
            if (!users.contains(r)) {
                System.out.println("[Server] " + r.getName() + " is not registered; skipped.");
            } else if (isBlocked(r, sender)) {
                System.out.println("[Server] Message to " + r.getName() + " blocked (" + r.getName()
                        + " blocks " + sender.getName() + ").");
            } else {
                r.receive(message);
            }
        }
    }

    /** Retracts a previously sent message from the sender's and all recipients' histories. */
    public void retractMessage(User sender, MessageMemento memento) {
        sender.getHistory().removeMessage(memento);
        for (User u : users) {
            u.getHistory().removeMessage(memento);
        }
    }
}
