package model;

import java.util.ArrayList;
import java.util.List;
import observer.BaseSubject;
import observer.NotificationEvent;
import observer.Observer;

public class Board extends BaseSubject implements Observer {

    private String name;
    private final List<TaskList> taskLists;
    private final List<User> members;

    public Board(String name) {
        this.name = name;
        this.taskLists = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public void addTaskList(TaskList taskList) {
        if (taskList != null && !taskLists.contains(taskList)) {
            taskLists.add(taskList);

            notifyObservers(
                new NotificationEvent(
                    "Board: " + name,
                    "Lista de tarefas adicionada"
                )
            );
        }
    }

    public void removeTaskList(TaskList taskList) {
        if (taskLists.remove(taskList)) {
            notifyObservers(
                new NotificationEvent(
                    "Board: " + name,
                    "Lista de tarefas removida"
                )
            );
        }
    }

    public void addMember(User user) {
        if (user != null && !members.contains(user)) {
            members.add(user);
            addObserver(user);

            notifyObservers(
                new NotificationEvent(
                    "Board: " + name,
                    "Novo membro adicionado"
                )
            );
        }
    }

    @Override
    public void update(NotificationEvent event) {
        notifyObservers(event);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        notifyObservers(
            new NotificationEvent("Board: " + name, "Nome do quadro alterado")
        );
    }

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    public List<User> getMembers() {
        return members;
    }
}
