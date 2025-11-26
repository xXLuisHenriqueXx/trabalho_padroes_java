package model.state;

import model.Task;
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
        LOGGER.info("A tarefa já está em progresso.");
    }

    @Override
    public void complete() {
        task.setState(new CompleteState(task));
    }

    @Override
    public void reopen() {
        task.setState(new TodoState(task));
    }

    @Override
    public String getStateName() {
        return "Em Progresso";
    }
}
