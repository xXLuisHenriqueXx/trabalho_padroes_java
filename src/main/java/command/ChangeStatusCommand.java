package command;

import model.Task;
import model.state.TaskState;
import observer.NotificationEvent;

public class ChangeStatusCommand implements Command {

    private final Task task;
    private final TaskState newState;
    private TaskState previouState;
    private boolean executed = false;

    public ChangeStatusCommand(Task task, TaskState newState) {
        this.task = task;
        this.newState = newState;
    }

    @Override
    public void execute() {
        if (task != null && newState != null) {
            previouState = task.getState();
            task.setState(newState);
            task.notifyObservers(
                new NotificationEvent(
                    "Tarefa: " + task.getTitle(),
                    "Status alterado para: " + newState.getStateName()
                )
            );

            executed = true;
        }
    }

    @Override
    public void undo() {
        if (executed && previouState != null) {
            task.setState(previouState);
            task.notifyObservers(
                new NotificationEvent(
                    "Tarefa: " + task.getTitle(),
                    "Status revertido para: " + previouState.getStateName()
                )
            );

            executed = false;
        }
    }
}
