package main.java.model;

import java.util.ArrayList;
import java.util.List;
import main.java.model.enums.Priority;
import main.java.observer.Observer;

public class Task {

    private String title;
    private String description;
    private Priority priority;
    private User responsible;

    private final List<Observer> observers;

    public Task(String title, String description, Priority priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update("Tarefa [" + title + "]: " + message);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyObservers("Título alterado para: " + title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyObservers("Descrição atualizada.");
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        notifyObservers("Prioridade alterada para: " + priority);
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
        notifyObservers("Responsável definido: " + responsible.getName());
    }

    @Override
    public String toString() {
        return (
            "Tarefa{" +
            "titulo = '" +
            title +
            '\'' +
            ", prioridade = " +
            priority +
            '}'
        );
    }
}
