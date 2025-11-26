package model;

import java.util.ArrayList;
import java.util.List;
import observer.Observer;

public class Board {

    private String name;
    private final List<TaskList> taskLists;
    private final List<User> members;
    private final List<Observer> observers;

    public Board(String name) {
        this.name = name;
        this.taskLists = new ArrayList<>();
        this.members = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void addTaskList(TaskList taskList) {
        if (taskList != null && !taskLists.contains(taskList)) {
            taskLists.add(taskList);
            notifyObservers(
                "Nova lista de tarefas adicionada: " + taskList.getName()
            );
        }
    }

    public void removeTaskList(TaskList taskList) {
        if (taskLists.remove(taskList)) {
            notifyObservers("Lista de tarefas removida: " + taskList.getName());
        }
    }

    public void addMember(User user) {
        if (user != null && !members.contains(user)) {
            members.add(user);
            notifyObservers("Novo membro adicionado: " + user.getName());
        }
    }

    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String mensagem) {
        for (Observer observer : observers) {
            observer.update("Quadro [" + name + "]: " + mensagem);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyObservers("O nome do quadro foi alterado para: " + name);
    }

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    public List<User> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return (
            "Quadro{" +
            "nome = '" +
            name +
            '\'' +
            ", listas de tarefa = " +
            taskLists.size() +
            ", membros = " +
            members.size() +
            '}'
        );
    }
}
