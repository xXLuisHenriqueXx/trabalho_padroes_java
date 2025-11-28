package command;

import model.Task;
import model.TaskList;
import observer.NotificationEvent;

public class MoveTaskCommand implements Command {

    private final TaskList origin;
    private final TaskList destiny;
    private final Task task;
    private boolean executed = false;

    public MoveTaskCommand(TaskList origin, TaskList destiny, Task task) {
        this.origin = origin;
        this.destiny = destiny;
        this.task = task;
    }

    @Override
    public void execute() {
        if (
            origin != null &&
            destiny != null &&
            origin.getTasks().contains(task)
        ) {
            origin.moveTask(task, destiny);

            task.notifyObservers(
                new NotificationEvent(
                    "Tarefa: " + task.getTitle(),
                    "Movida de " +
                        origin.getName() +
                        " para " +
                        destiny.getName()
                )
            );

            executed = true;
        }
    }

    @Override
    public void undo() {
        if (executed && destiny.getTasks().contains(task)) {
            destiny.moveTask(task, origin);

            task.notifyObservers(
                new NotificationEvent(
                    "Tarefa: " + task.getTitle(),
                    "Movida de " +
                        destiny.getName() +
                        " para " +
                        origin.getName()
                )
            );

            executed = false;
        }
    }
}
