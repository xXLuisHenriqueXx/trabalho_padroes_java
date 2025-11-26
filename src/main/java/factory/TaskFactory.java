package factory;

import model.Task;
import model.enums.Priority;

public interface TaskFactory {
    Task createTask(String title, String description, Priority priority);
}
