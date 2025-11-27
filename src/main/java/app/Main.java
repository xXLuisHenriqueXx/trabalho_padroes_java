package app;

import factory.TaskFactory;
import java.util.*;
import model.Board;
import model.Task;
import model.TaskList;
import model.User;
import model.enums.Priority;
import model.state.CompleteState;
import model.state.InProgressState;
import model.state.TodoState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BoardService;
import service.TaskListService;
import service.TaskService;
import singleton.TaskManager;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final Scanner scanner = new Scanner(System.in);

    private static final BoardService boardService = new BoardService();
    private static final TaskListService taskListService =
        new TaskListService();
    private static final TaskService taskService = new TaskService();

    public static void main(String[] args) {
        LOGGER.info("===== Sistema de Gerenciamento de Tarefas =====");

        User currentUser = createInitialUser();
        Board board = createInitialBoard(currentUser);

        while (true) {
            LOGGER.info("\n===== MENU =====");
            LOGGER.info("1. Criar Lista");
            LOGGER.info("2. Criar Tarefa");
            LOGGER.info("3. Mover Tarefa");
            LOGGER.info("4. Alterar Estado da Tarefa");
            LOGGER.info("5. Ver Listas e Tarefas");
            LOGGER.info("6. Ver Minhas Notificacoes");
            LOGGER.info("0. Sair");
            LOGGER.info("Escolha: ");

            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> createTaskList(board);
                case 2 -> createTask(board);
                case 3 -> moveTask(board);
                case 4 -> changeState(board);
                case 5 -> showBoard(board);
                case 6 -> showNotifications(currentUser);
                case 0 -> {
                    LOGGER.info("Saindo...");

                    return;
                }
                default -> LOGGER.info("Opcao invalida.");
            }
        }
    }

    private static User createInitialUser() {
        LOGGER.info("Digite seu nome: ");
        String name = scanner.nextLine();

        User user = new User(name, name.toLowerCase() + "@email.com");
        TaskManager.getInstance().registerUser(user);

        return user;
    }

    private static Board createInitialBoard(User user) {
        LOGGER.info("Nome do board: ");
        String name = scanner.nextLine();

        Board board = boardService.createBoard(name);
        boardService.addMember(board, user);

        LOGGER.info("Board criado!");

        return board;
    }

    private static void createTaskList(Board board) {
        LOGGER.info("Nome da lista: ");
        String name = scanner.nextLine();

        taskListService.createTaskList(board, name);

        LOGGER.info("Lista criada!");
    }

    private static void createTask(Board board) {
        TaskList taskList = chooseTaskList(board);
        if (taskList == null) return;

        LOGGER.info("Titulo da tarefa: ");
        String title = scanner.nextLine();

        LOGGER.info("Descricao: ");
        String description = scanner.nextLine();

        LOGGER.info("Tipo de tarefa: 1 - Prioritaria | 2 - Simples");
        int type = Integer.parseInt(scanner.nextLine());
        TaskFactory factory = taskService.chooseFactory(type);

        Priority priority = Priority.LOW;

        if (factory.supportsPriority()) {
            LOGGER.info("Prioridade: 1 - Baixa | 2 - Media | 3 - Alta");
            int option = Integer.parseInt(scanner.nextLine());
            priority = switch (option) {
                case 1 -> Priority.LOW;
                case 2 -> Priority.MEDIUM;
                case 3 -> Priority.HIGH;
                default -> Priority.MEDIUM;
            };
        }

        taskListService.addTaskWithCommand(
            taskList,
            factory,
            title,
            description,
            priority
        );

        LOGGER.info("Tarefa criada!");
    }

    private static void moveTask(Board board) {
        TaskList origin = chooseTaskList(board);
        if (origin == null) return;

        showTasks(origin);

        Task task = chooseTask(origin);
        if (task == null) return;

        TaskList destiny = chooseTaskList(board);
        if (destiny == null) return;

        taskListService.moveTaskWithCommand(origin, destiny, task);

        LOGGER.info("Tarefa movida!");
    }

    private static void changeInternalState(Task task, int option) {
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
            default -> LOGGER.info("Estado invalido.");
        }
    }

    private static void changeState(Board board) {
        TaskList taskList = chooseTaskList(board);
        if (taskList == null) return;

        showTasks(taskList);

        Task task = chooseTask(taskList);
        if (task == null) return;

        LOGGER.info(
            "Novo estado: 1 - A Fazer | 2 - Em Andamento | 3 - Concluida"
        );
        int option = Integer.parseInt(scanner.nextLine());

        changeInternalState(task, option);

        LOGGER.info("Estado alterado!");
    }

    private static void showBoard(Board board) {
        LOGGER.info("\n==== QUADRO:  {} ====", board.getName());

        for (TaskList taskList : board.getTaskLists()) {
            LOGGER.info("\n[Lista]  {}", taskList.getName());
            showTasks(taskList);
        }
    }

    private static void showNotifications(User user) {
        LOGGER.info("\n=== Suas notificacoes ===");
        user.viewNotifications();
    }

    private static TaskList chooseTaskList(Board board) {
        List<TaskList> taskLists = board.getTaskLists();

        if (taskLists.isEmpty()) {
            LOGGER.info("Nao ha listas ainda.");

            return null;
        }

        LOGGER.info("\nEscolha uma lista:");

        for (int i = 0; i < taskLists.size(); i++) {
            LOGGER.info("{}. {}", (i + 1), taskLists.get(i).getName());
        }

        LOGGER.info("Opcao: ");
        int option = Integer.parseInt(scanner.nextLine()) - 1;

        if (option < 0 || option >= taskLists.size()) {
            LOGGER.info("Invalida.");
            return null;
        }

        return taskLists.get(option);
    }

    private static void showTasks(TaskList taskList) {
        if (taskList.getTasks().isEmpty()) {
            LOGGER.info("  (sem tarefas)");

            return;
        }

        for (int i = 0; i < taskList.getTasks().size(); i++) {
            Task task = taskList.getTasks().get(i);

            LOGGER.info(
                "{}. [ {} ]  {} [ {} ]",
                (i + 1),
                task.getPriority(),
                task.getTitle(),
                task.getState().getStateName()
            );
        }
    }

    private static Task chooseTask(TaskList taskList) {
        if (taskList.getTasks().isEmpty()) {
            LOGGER.info("Nenhuma tarefa nessa lista.");

            return null;
        }

        LOGGER.info("Escolha a tarefa: ");
        int option = Integer.parseInt(scanner.nextLine()) - 1;

        if (option < 0 || option >= taskList.getTasks().size()) {
            LOGGER.info("Invalida.");

            return null;
        }

        return taskList.getTasks().get(option);
    }
}
