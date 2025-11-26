package factory;

import model.Task;
import model.enums.Priority;

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
