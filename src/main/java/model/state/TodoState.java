package model.state;

import model.Task;
import observer.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodoState extends TaskState {

    public static final Logger LOGGER = LoggerFactory.getLogger(
        TodoState.class
    );

    public TodoState(Task task) {
        super(task);
    }

    @Override
    public void execute() {
        task.setState(new InProgressState(task));

        task.notifyObservers(
            new NotificationEvent(
                "Tarefa: " + task.getTitle(),
                "A tarefa foi iniciada."
            )
        );
    }

    @Override
    public void complete() {
        LOGGER.warn(
            "Nao e possivel concluir uma tarefa que ainda nao foi iniciada."
        );
    }

    @Override
    public void reopen() {
        LOGGER.warn("A tarefa ja esta no estado [ A Fazer ]");
    }

    @Override
    public String getStateName() {
        return "A Fazer";
    }
}
