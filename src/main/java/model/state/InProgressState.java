package model.state;

import model.Task;
import observer.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InProgressState extends TaskState {

    public static final Logger LOGGER = LoggerFactory.getLogger(
        InProgressState.class
    );

    public InProgressState(Task task) {
        super(task);
    }

    @Override
    public void execute() {
        LOGGER.warn("A tarefa ja esta em progresso.");
    }

    @Override
    public void complete() {
        task.setState(new CompleteState(task));

        task.notifyObservers(
            new NotificationEvent(
                "Tarefa: " + task.getTitle(),
                "Tarefa concluida"
            )
        );
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
        return "Em Progresso";
    }
}
