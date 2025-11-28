package app.controller;

import java.util.List;
import java.util.Scanner;
import model.*;
import model.enums.Priority;
import model.state.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;
import singleton.TaskManager;
import util.ConsoleUtils;

public class AppController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        AppController.class
    );

    private final Scanner scanner = new Scanner(System.in);

    private final BoardService boardService = new BoardService();
    private final TaskListService taskListService = new TaskListService();
    private final TaskService taskService = new TaskService();

    private User currentUser;
    private Board currentBoard;

    public void start() {
        LOGGER.info("===== Sistema de Gerenciamento de Tarefas =====");

        initUserAndBoard();

        LOGGER.info("Pressione ENTER para continuar...");
        readLine("");
        ConsoleUtils.clear();

        menuLoop();
    }

    private void initUserAndBoard() {
        currentUser = createInitialUser();
        currentBoard = createInitialBoard(currentUser);
    }

    private void menuLoop() {
        while (true) {
            printMenu();

            int option = readInt("Escolha: ");

            switch (option) {
                case 1 -> createTaskList();
                case 2 -> createTask();
                case 3 -> moveTask();
                case 4 -> changeState();
                case 5 -> showBoard();
                case 6 -> showNotifications();
                case 7 -> renameBoard();
                case 8 -> renameTask();
                case 9 -> changeTaskDescription();
                case 10 -> changeTaskPriority();
                case 11 -> removeTaskFromList();
                case 12 -> undoLastCommand();
                case 0 -> {
                    LOGGER.info("Saindo...");
                    return;
                }
                default -> LOGGER.error("Opcao invalida.");
            }

            LOGGER.info("Pressione ENTER para continuar...");
            readLine("");
            ConsoleUtils.clear();
        }
    }

    private void printMenu() {
        LOGGER.info("===== MENU =====");
        LOGGER.info("1. Criar Lista");
        LOGGER.info("2. Criar Tarefa");
        LOGGER.info("3. Mover Tarefa");
        LOGGER.info("4. Alterar Estado da Tarefa");
        LOGGER.info("5. Ver Listas e Tarefas");
        LOGGER.info("6. Ver Minhas Notificacoes");
        LOGGER.info("7. Alterar nome do Board");
        LOGGER.info("8. Alterar titulo da Tarefa");
        LOGGER.info("9. Alterar descricao da Tarefa");
        LOGGER.info("10. Alterar prioridade da Tarefa");
        LOGGER.info("11. Remover tarefa da lista");
        LOGGER.info("12. Desfazer ultimo comando da lista");
        LOGGER.info("0. Sair");
    }

    private User createInitialUser() {
        String name = readLine("Digite seu nome: ");
        User user = new User(name, name.toLowerCase() + "@email.com");
        TaskManager.getInstance().registerUser(user);

        return user;
    }

    private Board createInitialBoard(User user) {
        String name = readLine("Nome do board: ");

        Board board = boardService.createBoard(name);
        boardService.addMember(board, user);

        LOGGER.info("Board criado!");

        return board;
    }

    private void createTaskList() {
        String name = readLine("Nome da lista: ");
        taskListService.createTaskList(currentBoard, name);
        LOGGER.info("Lista criada!");
    }

    private void createTask() {
        TaskList taskList = chooseTaskList();
        if (taskList == null) return;

        String title = readLine("Titulo: ");
        String description = readLine("Descricao: ");

        int type = readInt("Tipo: 1-Prioritaria | 2-Simples: ");
        var factory = taskService.chooseFactory(type);

        Priority priority = factory.supportsPriority()
            ? choosePriority()
            : Priority.LOW;

        taskListService.addTaskWithCommand(
            taskList,
            factory,
            title,
            description,
            priority
        );

        LOGGER.info("Tarefa criada!");
    }

    private Priority choosePriority() {
        int option = readInt("Prioridade: 1-Baixa | 2-Media | 3-Alta: ");

        return switch (option) {
            case 1 -> Priority.LOW;
            case 2 -> Priority.MEDIUM;
            case 3 -> Priority.HIGH;
            default -> Priority.MEDIUM;
        };
    }

    private void moveTask() {
        TaskList origin = chooseTaskList();
        if (origin == null) return;

        Task task = chooseTask(origin);
        if (task == null) return;

        TaskList destiny = chooseTaskList();
        if (destiny == null) return;

        taskListService.moveTaskWithCommand(origin, destiny, task);

        LOGGER.info("Tarefa movida!");
    }

    private void changeState() {
        TaskList list = chooseTaskList();
        if (list == null) return;

        Task task = chooseTask(list);
        if (task == null) return;

        int option = readInt(
            "Novo estado: 1-A Fazer | 2-Em Andamento | 3-Concluida: "
        );

        switch (option) {
            case 1 -> taskService.changeStateWithCommand(
                task,
                new TodoState(task)
            );
            case 2 -> taskService.changeStateWithCommand(
                task,
                new InProgressState(task)
            );
            case 3 -> taskService.changeStateWithCommand(
                task,
                new CompleteState(task)
            );
            default -> LOGGER.info("Invalido.");
        }

        LOGGER.info("Estado alterado!");
    }

    private void showBoard() {
        LOGGER.info("==== QUADRO: {} ====", currentBoard.getName());
        currentBoard.getTaskLists().forEach(this::showTasks);
    }

    private void showNotifications() {
        LOGGER.info("=== Suas notificacoes ===");
        currentUser.viewNotifications();
    }

    private void renameBoard() {
        String newName = readLine("Novo nome do board: ");
        currentBoard.setName(newName);
        LOGGER.info("Nome do board alterado!");
    }

    private void renameTask() {
        TaskList list = chooseTaskList();
        if (list == null) return;

        Task task = chooseTask(list);
        if (task == null) return;

        String newTitle = readLine("Novo titulo: ");
        task.setTitle(newTitle);

        LOGGER.info("Titulo alterado!");
    }

    private void changeTaskDescription() {
        TaskList list = chooseTaskList();
        if (list == null) return;

        Task task = chooseTask(list);
        if (task == null) return;

        String description = readLine("Nova descricao: ");
        task.setDescription(description);

        LOGGER.info("Descricao alterada!");
    }

    private void changeTaskPriority() {
        TaskList list = chooseTaskList();
        if (list == null) return;

        Task task = chooseTask(list);
        if (task == null) return;

        Priority priority = choosePriority();
        task.setPriority(priority);

        LOGGER.info("Prioridade alterada!");
    }

    private void removeTaskFromList() {
        TaskList list = chooseTaskList();
        if (list == null) return;

        Task task = chooseTask(list);
        if (task == null) return;

        taskListService.removeTaskWithCommand(list, task);

        LOGGER.info("Tarefa removida!");
    }

    private void undoLastCommand() {
        TaskList list = chooseTaskList();
        if (list == null) return;

        list.undoLastCommand();
        LOGGER.info("ultimo comando desfeito!");
    }

    private TaskList chooseTaskList() {
        List<TaskList> lists = currentBoard.getTaskLists();

        if (lists.isEmpty()) {
            LOGGER.info("Nao ha listas.");
            return null;
        }

        LOGGER.info("Escolha uma lista:");
        for (int i = 0; i < lists.size(); i++) {
            LOGGER.info("{}. {}", i + 1, lists.get(i).getName());
        }

        int option = readInt("Opcao: ") - 1;

        return (option >= 0 && option < lists.size())
            ? lists.get(option)
            : null;
    }

    private Task chooseTask(TaskList list) {
        var tasks = list.getTasks();
        if (tasks.isEmpty()) {
            LOGGER.info("Nenhuma tarefa.");
            return null;
        }

        showTasks(list);

        int option = readInt("Escolha a tarefa: ") - 1;

        return (option >= 0 && option < tasks.size())
            ? tasks.get(option)
            : null;
    }

    private void showTasks(TaskList list) {
        LOGGER.info("[Lista]  {}", list.getName());

        if (list.getTasks().isEmpty()) {
            LOGGER.info("(sem tarefas)");
            return;
        }

        for (int i = 0; i < list.getTasks().size(); i++) {
            Task task = list.getTasks().get(i);
            LOGGER.info(
                "{}. [{}] {} [{}]",
                i + 1,
                task.getPriority(),
                task.getTitle(),
                task.getState().getStateName()
            );
        }
    }

    private String readLine(String msg) {
        LOGGER.info(msg);
        return scanner.nextLine();
    }

    private int readInt(String msg) {
        LOGGER.info(msg);
        return Integer.parseInt(scanner.nextLine());
    }
}
