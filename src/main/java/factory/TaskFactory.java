package main.java.factory;

import main.java.model.Task;
import main.java.model.enums.Priority;

public interface TaskFactory {
    Task createTask(String title, String description, Priority priority);
}
