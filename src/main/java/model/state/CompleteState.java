package model.state;

import model.Task;
import observer.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompleteState extends TaskState {

    public static final Logger LOGGER = LoggerFactory.getLogger(
        CompleteState.class
    );

    public CompleteState(Task task) {
        super(task);
    }

    @Override
    public void execute() {
        LOGGER.warn(
            "A tarefa ja foi concluida. Reabra antes de iniciar novamente."
        );
    }

    @Override
    public void complete() {
        LOGGER.warn("A tarefa ja esta concluida.");
    }

    @Override
    public void reopen() {
        task.setState(new TodoState(task));

        task.notifyObservers(
            new NotificationEvent(
                "Tarefa: " + task.getTitle(),
                "Tarefa reaberta"
            )
        );
    }

    @Override
    public String getStateName() {
        return "Concluida";
    }
}
