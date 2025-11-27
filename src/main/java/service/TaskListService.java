package service;

import command.AddTaskCommand;
import command.Command;
import command.MoveTaskCommand;
import factory.TaskFactory;
import java.util.Optional;
import model.Board;
import model.Task;
import model.TaskList;
import model.enums.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        TaskListService.class
    );

    public TaskList createTaskList(Board board, String taskListName) {
        if (board == null || taskListName == null || taskListName.isBlank()) {
            throw new IllegalArgumentException(
                "Quadro ou nome de lista invalido."
            );
        }

        TaskList taskList = new TaskList(taskListName);
        board.addTaskList(taskList);

        LOGGER.info(
            "Lista ({}) criada no quadro {}.",
            taskListName,
            board.getName()
        );

        return taskList;
    }

    public void addTaskWithCommand(
        TaskList taskList,
        TaskFactory factory,
        String title,
        String description,
        Priority priority
    ) {
        if (taskList == null || factory == null) {
            throw new IllegalArgumentException("Lista ou fabrica invalida.");
        }

        Task task = factory.createTask(title, description, priority);

        Command command = new AddTaskCommand(taskList, task);
        taskList.executeCommand(command);

        LOGGER.info(
            "Tarefa {} adicionada a lista {}.",
            title,
            taskList.getName()
        );
    }

    public void moveTaskWithCommand(
        TaskList origin,
        TaskList destiny,
        Task task
    ) {
        if (origin == null || destiny == null || task == null) {
            throw new IllegalArgumentException(
                "Parametros invalidos para mover tarefa."
            );
        }

        Command command = new MoveTaskCommand(origin, destiny, task);
        origin.executeCommand(command);

        LOGGER.info("Executado comando de mover tarefa {}.", task.getTitle());
    }

    public Optional<TaskList> searchTaskListByName(
        Board board,
        String taskListName
    ) {
        if (board == null || taskListName == null) return Optional.empty();

        return board
            .getTaskLists()
            .stream()
            .filter(list -> taskListName.equalsIgnoreCase(list.getName()))
            .findFirst();
    }
}
