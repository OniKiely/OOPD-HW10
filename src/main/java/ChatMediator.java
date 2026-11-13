import java.util.List;

public interface ChatMediator {
    void registerUser(User user);
    void unregisterUser(User user);
    void sendMessage(User sender, List<User> recipients, String content);
    void blockUser(User blocker, User blocked);
    void unblockUser(User blocker, User blocked);
}
