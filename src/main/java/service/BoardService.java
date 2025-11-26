package service;

import java.util.List;
import java.util.Optional;
import main.java.model.Board;
import main.java.model.TaskList;
import main.java.model.User;
import main.java.singleton.TaskManager;

public class BoardService {

    private final TaskManager manager = TaskManager.getInstance();

    public Board createBoard(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do quadro inválido.");
        }

        Board board = new Board(name);
        manager.registerBoard(board);

        System.out.println("Quadro criado: " + name);

        return board;
    }

    public void addTaskListInBoard(Board board, TaskList taskList) {
        if (board == null || taskList == null) {
            throw new IllegalArgumentException(
                "Quadro ou Lista de tarefas inválidos."
            );
        }

        board.addTaskList(taskList);

        System.out.println(
            "Lista '" +
                taskList.getName() +
                "' adicionada ao board '" +
                board.getName() +
                "'."
        );
    }

    public void addMember(Board board, User user) {
        if (board == null || user == null) {
            throw new IllegalArgumentException("Quadro ou Usuario inválidos.");
        }

        board.addMember(user);
        user.addBoard(board);

        System.out.println(
            "Usuario '" +
                user.getName() +
                "' adicionado ao quadro '" +
                board.getName() +
                "'."
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
