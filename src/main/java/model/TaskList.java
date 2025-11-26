package model;

import command.Command;
import java.util.ArrayList;
import java.util.List;

public class TaskList {

    private String name;
    private final List<Task> tasks;
    private final List<Command> history;

    public TaskList(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
        this.history = new ArrayList<>();
    }

    public void addTask(Task task) {
        if (task != null && !tasks.contains(task)) {
            tasks.add(task);
            System.out.println(
                "Tarefa adicionada à lista '" + name + "': " + task.getTitle()
            );
        }
    }

    public void removeTask(Task task) {
        if (tasks.remove(task)) {
            System.out.println(
                "Tarefa removida da lista '" + name + "': " + task.getTitle()
            );
        }
    }

    public void moveTask(Task task, TaskList destiny) {
        if (tasks.contains(task) && destiny != null) {
            removeTask(task);
            destiny.addTask(task);
            System.out.println(
                "Tarefa movida de '" +
                    name +
                    "' para '" +
                    destiny.getName() +
                    "'"
            );
        }
    }

    public void executeCommand(Command comand) {
        if (comand != null) {
            comand.execute();
            history.add(comand);
        }
    }

    public void undoLastCommand() {
        if (!history.isEmpty()) {
            Command lastCommand = history.remove(history.size() - 1);
            lastCommand.undo();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return (
            "Lista{" +
            "nome = '" +
            name +
            '\'' +
            ", tarefas = " +
            tasks.size() +
            '}'
        );
    }
}
