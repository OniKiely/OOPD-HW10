import java.util.Iterator;

public class Driver {
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        User alice = new User("Alice");
        User bob = new User("Bob");
        User carol = new User("Carol");
        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(carol);
        System.out.println();

        System.out.println("Send to one and to multiple users");
        alice.send("Hi Bob!", bob);
        alice.send("Hello everyone!", bob, carol);
        bob.send("Hey Alice, how are you?", alice);
        carol.send("Good morning!", alice, bob);
        System.out.println();

        System.out.println("Undo: ");
        alice.send("Oops, wrong chat!", bob, carol);
        alice.undoLastMessage();
        bob.viewHistory();
        carol.viewHistory();
        System.out.println();

        System.out.println("Blocking: ");
        bob.block(carol);
        carol.send("Bob, can you see this?", bob, alice);
        alice.send("Carol said hi to you, Bob", bob);
        bob.unblock(carol);
        carol.send("Now it should work", bob);
        System.out.println();

        System.out.println("View chat history: ");
        alice.viewHistory();
        bob.viewHistory();
        carol.viewHistory();
        System.out.println();

        System.out.println("Iterator: Bob's messages involving Carol ");
        Iterator<Message> it = bob.iterator(carol);
        while (it.hasNext()) System.out.println("  " + it.next());
        System.out.println();

        System.out.println("Alice's messages involving Bob: ");
        it = alice.iterator(bob);
        while (it.hasNext()) System.out.println("  " + it.next());
    }
}
