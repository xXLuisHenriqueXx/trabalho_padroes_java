package service;

import command.ChangeStatusCommand;
import command.Command;
import factory.PriorityTaskFactory;
import factory.SimpleTaskFactory;
import factory.TaskFactory;
import java.util.Optional;
import model.Task;
import model.TaskList;
import model.User;
import model.state.TaskState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        TaskService.class
    );

    public void setResponsible(Task task, User user) {
        if (task == null || user == null) {
            throw new IllegalArgumentException("Tarefa ou usuario invalido.");
        }

        task.setResponsible(user);
        task.notifyObservers("Responsavel atribuido: " + user.getName());

        LOGGER.info(
            "Responsavel '{}' atribuido a tarefa '{}'.",
            user.getName(),
            task.getTitle()
        );
    }

    public void changeStateWithCommand(Task task, TaskState newState) {
        if (task == null || newState == null) {
            throw new IllegalArgumentException(
                "Parametros invalidos para alterar estado."
            );
        }

        Command command = new ChangeStatusCommand(task, newState);
        command.execute();

        LOGGER.info(
            "Estado da tarefa '{}' alterado para '{}'.",
            task.getTitle(),
            newState.getStateName()
        );
    }

    public void reopenTask(Task task) {
        if (task == null) return;

        task.reopen();

        LOGGER.info("Tarefa reaberta: '{}'.", task.getTitle());
    }

    public TaskFactory chooseFactory(int type) {
        return switch (type) {
            case 1 -> new PriorityTaskFactory();
            default -> new SimpleTaskFactory();
        };
    }

    public Optional<Task> searchTaskByTitle(TaskList taskList, String title) {
        if (taskList == null || title == null) return Optional.empty();

        return taskList
            .getTasks()
            .stream()
            .filter(task -> title.equalsIgnoreCase(task.getTitle()))
            .findFirst();
    }
}
