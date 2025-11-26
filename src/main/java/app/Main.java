package app;

import factory.PriorityTaskFactory;
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
            LOGGER.info("\n--- MENU ---");
            LOGGER.info("1. Criar Lista");
            LOGGER.info("2. Criar Tarefa");
            LOGGER.info("3. Mover Tarefa");
            LOGGER.info("4. Alterar Estado da Tarefa");
            LOGGER.info("5. Ver Listas e Tarefas");
            LOGGER.info("6. Ver Minhas Notificações");
            LOGGER.info("0. Sair");
            System.out.print("Escolha: ");

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
                default -> LOGGER.info("Opção inválida.");
            }
        }
    }

    private static User createInitialUser() {
        System.out.print("Digite seu nome: ");
        String name = scanner.nextLine();

        User user = new User(name, name.toLowerCase() + "@email.com");
        TaskManager.getInstance().registerUser(user);

        return user;
    }

    private static Board createInitialBoard(User user) {
        System.out.print("Nome do board: ");
        String name = scanner.nextLine();

        Board board = boardService.createBoard(name);
        boardService.addMember(board, user);

        LOGGER.info("Board criado!");

        return board;
    }

    private static void createTaskList(Board board) {
        System.out.print("Nome da lista: ");
        String name = scanner.nextLine();

        taskListService.createTaskList(board, name);

        LOGGER.info("Lista criada!");
    }

    private static void createTask(Board board) {
        TaskList taskList = chooseTaskList(board);
        if (taskList == null) return;

        System.out.print("Título da tarefa: ");
        String title = scanner.nextLine();

        System.out.print("Descrição: ");
        String description = scanner.nextLine();

        LOGGER.info("Prioridade: 1-BAIXA | 2-MEDIA | 3-ALTA");
        int option = Integer.parseInt(scanner.nextLine());
        Priority priority = switch (option) {
            case 1 -> Priority.LOW;
            case 2 -> Priority.MEDIUM;
            case 3 -> Priority.HIGH;
            default -> Priority.MEDIUM;
        };

        LOGGER.info("Tipo de tarefa: 1-Prioritária");
        int type = Integer.parseInt(scanner.nextLine());
        TaskFactory factory = (type == 1)
            ? new PriorityTaskFactory()
            : new PriorityTaskFactory();

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
            default -> LOGGER.info("Estado inválido.");
        }
    }

    private static void changeState(Board board) {
        TaskList taskList = chooseTaskList(board);
        if (taskList == null) return;

        showTasks(taskList);

        Task task = chooseTask(taskList);
        if (task == null) return;

        LOGGER.info("Novo estado: 1-A FAZER | 2-EM ANDAMENTO | 3-CONCLUÍDA");
        int option = Integer.parseInt(scanner.nextLine());

        changeInternalState(task, option);

        LOGGER.info("Estado alterado!");
    }

    private static void showBoard(Board board) {
        LOGGER.info("\n==== QUADRO: " + board.getName() + " ====");

        for (TaskList taskList : board.getTaskLists()) {
            LOGGER.info("\n[Lista] " + taskList.getName());
            showTasks(taskList);
        }
    }

    private static void showNotifications(User user) {
        LOGGER.info("\n=== Suas notificações ===");
        user.viewNotifications();
    }

    private static TaskList chooseTaskList(Board board) {
        List<TaskList> taskLists = board.getTaskLists();

        if (taskLists.isEmpty()) {
            LOGGER.info("Não há listas ainda.");

            return null;
        }

        LOGGER.info("\nEscolha uma lista:");

        for (int i = 0; i < taskLists.size(); i++) {
            LOGGER.info((i + 1) + ". " + taskLists.get(i).getName());
        }

        System.out.print("Opção: ");
        int option = Integer.parseInt(scanner.nextLine()) - 1;

        if (option < 0 || option >= taskLists.size()) {
            LOGGER.info("Inválida.");
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
                "  " +
                    (i + 1) +
                    ". " +
                    task.getTitle() +
                    " [" +
                    task.getState().getStateName() +
                    "]"
            );
        }
    }

    private static Task chooseTask(TaskList taskList) {
        if (taskList.getTasks().isEmpty()) {
            LOGGER.info("Nenhuma tarefa nessa lista.");

            return null;
        }

        System.out.print("Escolha a tarefa: ");
        int option = Integer.parseInt(scanner.nextLine()) - 1;

        if (option < 0 || option >= taskList.getTasks().size()) {
            LOGGER.info("Inválida.");

            return null;
        }

        return taskList.getTasks().get(option);
    }
}
