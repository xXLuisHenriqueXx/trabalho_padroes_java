package main.java;

import java.util.*;
import main.java.factory.PriorityTaskFactory;
import main.java.factory.TaskFactory;
import main.java.model.Board;
import main.java.model.Task;
import main.java.model.TaskList;
import main.java.model.User;
import main.java.model.enums.Priority;
import main.java.model.state.CompleteState;
import main.java.model.state.InProgressState;
import main.java.model.state.TodoState;
import main.java.service.BoardService;
import main.java.service.TaskListService;
import main.java.service.TaskService;
import main.java.singleton.TaskManager;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final BoardService boardService = new BoardService();
    private static final TaskListService taskListService =
        new TaskListService();
    private static final TaskService taskService = new TaskService();

    public static void main(String[] args) {
        System.out.println("===== Sistema de Gerenciamento de Tarefas =====");

        User currentUser = createInitialUser();
        Board board = createInitialBoard(currentUser);

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Criar Lista");
            System.out.println("2. Criar Tarefa");
            System.out.println("3. Mover Tarefa");
            System.out.println("4. Alterar Estado da Tarefa");
            System.out.println("5. Ver Listas e Tarefas");
            System.out.println("6. Ver Minhas Notificações");
            System.out.println("0. Sair");
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
                    System.out.println("Saindo...");

                    return;
                }
                default -> System.out.println("Opção inválida.");
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

        System.out.println("Board criado!");

        return board;
    }

    private static void createTaskList(Board board) {
        System.out.print("Nome da lista: ");
        String name = scanner.nextLine();

        taskListService.createTaskList(board, name);

        System.out.println("Lista criada!");
    }

    private static void createTask(Board board) {
        TaskList taskList = chooseTaskList(board);
        if (taskList == null) return;

        System.out.print("Título da tarefa: ");
        String title = scanner.nextLine();

        System.out.print("Descrição: ");
        String description = scanner.nextLine();

        System.out.println("Prioridade: 1-BAIXA | 2-MEDIA | 3-ALTA");
        int option = Integer.parseInt(scanner.nextLine());
        Priority priority = switch (option) {
            case 1 -> Priority.LOW;
            case 2 -> Priority.MEDIUM;
            case 3 -> Priority.HIGH;
            default -> Priority.MEDIUM;
        };

        System.out.println("Tipo de tarefa: 1-Prioritária");
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

        System.out.println("Tarefa criada!");
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

        System.out.println("Tarefa movida!");
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
            default -> System.out.println("Estado inválido.");
        }
    }

    private static void changeState(Board board) {
        TaskList taskList = chooseTaskList(board);
        if (taskList == null) return;

        showTasks(taskList);

        Task task = chooseTask(taskList);
        if (task == null) return;

        System.out.println(
            "Novo estado: 1-A FAZER | 2-EM ANDAMENTO | 3-CONCLUÍDA"
        );
        int option = Integer.parseInt(scanner.nextLine());

        changeInternalState(task, option);

        System.out.println("Estado alterado!");
    }

    private static void showBoard(Board board) {
        System.out.println("\n==== QUADRO: " + board.getName() + " ====");

        for (TaskList taskList : board.getTaskLists()) {
            System.out.println("\n[Lista] " + taskList.getName());
            showTasks(taskList);
        }
    }

    private static void showNotifications(User user) {
        System.out.println("\n=== Suas notificações ===");
        user.viewNotifications();
    }

    private static TaskList chooseTaskList(Board board) {
        List<TaskList> taskLists = board.getTaskLists();

        if (taskLists.isEmpty()) {
            System.out.println("Não há listas ainda.");

            return null;
        }

        System.out.println("\nEscolha uma lista:");

        for (int i = 0; i < taskLists.size(); i++) {
            System.out.println((i + 1) + ". " + taskLists.get(i).getName());
        }

        System.out.print("Opção: ");
        int option = Integer.parseInt(scanner.nextLine()) - 1;

        if (option < 0 || option >= taskLists.size()) {
            System.out.println("Inválida.");
            return null;
        }

        return taskLists.get(option);
    }

    private static void showTasks(TaskList taskList) {
        if (taskList.getTasks().isEmpty()) {
            System.out.println("  (sem tarefas)");

            return;
        }

        for (int i = 0; i < taskList.getTasks().size(); i++) {
            Task task = taskList.getTasks().get(i);

            System.out.println(
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
            System.out.println("Nenhuma tarefa nessa lista.");

            return null;
        }

        System.out.print("Escolha a tarefa: ");
        int option = Integer.parseInt(scanner.nextLine()) - 1;

        if (option < 0 || option >= taskList.getTasks().size()) {
            System.out.println("Inválida.");

            return null;
        }

        return taskList.getTasks().get(option);
    }
}
