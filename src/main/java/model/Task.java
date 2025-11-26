package model;

import java.util.ArrayList;
import java.util.List;
import model.enums.Priority;
import model.state.TaskState;
import model.state.TodoState;
import observer.Observer;

public class Task {

    private String title;
    private String description;
    private Priority priority;
    private User responsible;
    private TaskState state;

    private final List<Observer> observers;

    public Task(String title, String description, Priority priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.state = new TodoState(this);
        this.observers = new ArrayList<>();
    }

    public void execute() {
        state.execute();
        notifyObservers("Tarefa iniciada: " + title);
    }

    public void complete() {
        state.complete();
        notifyObservers("Tarefa concluída: " + title);
    }

    public void reopen() {
        state.reopen();
        notifyObservers("Tarefa reaberta: " + title);
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

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
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
            ", estado = " +
            state.getStateName() +
            '}'
        );
    }
}
