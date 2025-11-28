package model;

import model.enums.Priority;
import model.state.TaskState;
import model.state.TodoState;
import observer.BaseSubject;
import observer.NotificationEvent;

public class Task extends BaseSubject {

    private String title;
    private String description;
    private Priority priority;
    private User responsible;
    private TaskState state;

    public Task(String title, String description, Priority priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.state = new TodoState(this);
    }

    public void execute() {
        state.execute();

        notifyObservers(
            new NotificationEvent("Tarefa: " + title, "Executando tarefa")
        );
    }

    public void complete() {
        state.complete();

        notifyObservers(
            new NotificationEvent("Tarefa: " + title, "Tarefa concluida")
        );
    }

    public void reopen() {
        state.reopen();

        notifyObservers(
            new NotificationEvent("Tarefa: " + title, "Tarefa reaberta")
        );
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

        notifyObservers(
            new NotificationEvent(
                "Tarefa: " + title,
                "Titulo alterado para: " + title
            )
        );
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;

        notifyObservers(
            new NotificationEvent(
                "Tarefa: " + title,
                "Descricao alterada para: " + description
            )
        );
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        notifyObservers(
            new NotificationEvent(
                "Tarefa: " + title,
                "Prioridade alterada para: " + priority
            )
        );
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
        notifyObservers(
            new NotificationEvent(
                "Tarefa: " + title,
                "Responsavel alterado para: " + responsible.getName()
            )
        );
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }
}
