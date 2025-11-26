package service;

import java.util.Optional;
import main.java.command.ChangeStatusCommand;
import main.java.command.Command;
import main.java.model.Task;
import main.java.model.TaskList;
import main.java.model.User;
import main.java.model.state.TaskState;

public class TaskService {

    public void setResponsible(Task tarefa, User user) {
        if (tarefa == null || user == null) {
            throw new IllegalArgumentException("Tarefa ou usuário inválido.");
        }

        tarefa.setResponsible(user);
        tarefa.notifyObservers("Responsável atribuído: " + user.getName());

        System.out.println(
            "Responsável '" +
                user.getName() +
                "' atribuído à tarefa '" +
                tarefa.getTitle() +
                "'."
        );
    }

    public void changeStateWithCommand(Task task, TaskState newState) {
        if (task == null || newState == null) {
            throw new IllegalArgumentException(
                "Parâmetros inválidos para alterar estado."
            );
        }

        Command command = new ChangeStatusCommand(task, newState);
        command.execute();

        System.out.println(
            "Estado da tarefa '" +
                task.getTitle() +
                "' alterado para " +
                newState.getStateName()
        );
    }

    public void reopenTask(Task task) {
        if (task == null) return;

        task.reopen();

        System.out.println("Tarefa reaberta: " + task.getTitle());
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
