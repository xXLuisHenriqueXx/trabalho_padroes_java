package main.java.command;

import main.java.model.Task;
import main.java.model.TaskList;

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

            executed = true;
        }
    }

    @Override
    public void undo() {
        if (executed && destiny.getTasks().contains(task)) {
            destiny.moveTask(task, origin);

            executed = false;
        }
    }
}
