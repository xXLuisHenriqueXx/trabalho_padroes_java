package factory;

import model.Task;
import model.enums.Priority;

public class SimpleTaskFactory implements TaskFactory {

    @Override
    public Task createTask(
        String title,
        String description,
        Priority priority
    ) {
        return new Task(title, description, Priority.LOW);
    }
}
