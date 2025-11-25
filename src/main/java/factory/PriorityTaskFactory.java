package main.java.factory;

import main.java.model.Task;
import main.java.model.enums.Priority;

public class PriorityTaskFactory implements TaskFactory {

    @Override
    public Task createTask(
        String title,
        String description,
        Priority priority
    ) {
        return new Task(title, description, priority);
    }
}
