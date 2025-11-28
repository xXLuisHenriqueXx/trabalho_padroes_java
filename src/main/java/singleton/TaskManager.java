package singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Board;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        TaskManager.class
    );

    private final List<User> users;
    private final List<Board> boards;

    private TaskManager() {
        this.users = new ArrayList<>();
        this.boards = new ArrayList<>();
    }

    private static class TaskManagerHelper {

        private static final TaskManager INSTANCE = new TaskManager();
    }

    public static TaskManager getInstance() {
        return TaskManagerHelper.INSTANCE;
    }

    public void registerUser(User user) {
        if (user != null && !users.contains(user)) {
            users.add(user);
            LOGGER.info("Usuario registrado: {}.", user.getName());
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
        return Collections.unmodifiableList(users);
    }

    public void registerBoard(Board board) {
        if (board != null && !boards.contains(board)) {
            boards.add(board);
            LOGGER.info("Quadro registrado: {}.", board.getName());
        }
    }

    public List<Board> getBoards() {
        return Collections.unmodifiableList(boards);
    }
}
