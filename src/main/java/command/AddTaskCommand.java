package command;

import model.Task;
import model.TaskList;

public class AddTaskCommand implements Command {

    private final TaskList taskList;
    private final Task task;
    private boolean executed = false;

    public AddTaskCommand(TaskList taskList, Task task) {
        this.taskList = taskList;
        this.task = task;
    }

    @Override
    public void execute() {
        if (!taskList.getTasks().contains(task)) {
            taskList.addTask(task);

            executed = true;
        }
    }

    @Override
    public void undo() {
        if (executed && taskList.getTasks().contains(task)) {
            taskList.removeTask(task);

            executed = false;
        }
    }
}
