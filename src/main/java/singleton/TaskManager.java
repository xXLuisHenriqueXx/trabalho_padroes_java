package singleton;

import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        TaskManager.class
    );

    private static TaskManager instance;

    private final List<User> users;
    private final List<Board> boards;

    private TaskManager() {
        this.users = new ArrayList<>();
        this.boards = new ArrayList<>();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }

        return instance;
    }

    public void registerUser(User user) {
        if (user != null && !users.contains(user)) {
            users.add(user);

            LOGGER.info("Usuário registrado: '{}'.", user.getName());
        }
    }

    public User searchUserByEmail(String email) {
        return users
            .stream()
            .filter(user -> user.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElse(null);
    }

    public List<User> getUsers() {
        return users;
    }

    public void registerBoard(Board board) {
        if (board != null && !boards.contains(board)) {
            boards.add(board);
            LOGGER.info("Quadro registrado: '{}'.", board.getName());
        }
    }

    public List<Board> getBoards() {
        return boards;
    }

    @Override
    public String toString() {
        return (
            "TaskManager{" +
            "users=" +
            users.size() +
            ", boards=" +
            boards.size() +
            '}'
        );
    }
}
