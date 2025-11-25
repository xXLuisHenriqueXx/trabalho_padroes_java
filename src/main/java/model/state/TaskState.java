package main.java.model.state;

import main.java.model.Task;

public abstract class TaskState {

    protected Task task;

    public TaskState(Task task) {
        this.task = task;
    }

    public abstract void execute();

    public abstract void complete();

    public abstract void reopen();

    public abstract String getStateName();
}
