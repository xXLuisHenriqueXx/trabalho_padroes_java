package service;

import java.util.List;
import java.util.Optional;
import model.Board;
import model.TaskList;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import singleton.TaskManager;

public class BoardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        BoardService.class
    );

    private final TaskManager manager = TaskManager.getInstance();

    public Board createBoard(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do quadro inválido.");
        }

        Board board = new Board(name);
        manager.registerBoard(board);

        LOGGER.info("Quadro criado: {}", name);

        return board;
    }

    public void addTaskListInBoard(Board board, TaskList taskList) {
        if (board == null || taskList == null) {
            throw new IllegalArgumentException(
                "Quadro ou Lista de tarefas inválidos."
            );
        }

        board.addTaskList(taskList);

        LOGGER.info(
            "Lista '{}' adicionada ao board '{}'.",
            taskList.getName(),
            board.getName()
        );
    }

    public void addMember(Board board, User user) {
        if (board == null || user == null) {
            throw new IllegalArgumentException("Quadro ou Usuario inválidos.");
        }

        board.addMember(user);
        user.addBoard(board);

        LOGGER.info(
            "Usuario '{}' adicionado ao quadro '{}'.",
            user.getName(),
            board.getName()
        );
    }

    public Optional<Board> searchBoardByName(String name) {
        if (name == null) return Optional.empty();

        List<Board> boards = manager.getBoards();

        return boards
            .stream()
            .filter(board -> name.equalsIgnoreCase(board.getName()))
            .findFirst();
    }

    public List<Board> listBoards() {
        return manager.getBoards();
    }
}
