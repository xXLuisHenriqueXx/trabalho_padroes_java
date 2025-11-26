package model.state;

import model.Task;
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
        LOGGER.info(
            "A tarefa já foi concluída. Reabra antes de iniciar novamente."
        );
    }

    @Override
    public void complete() {
        System.out.println("A tarefa já está concluída.");
    }

    @Override
    public void reopen() {
        task.setState(new TodoState(task));
    }

    @Override
    public String getStateName() {
        return "Concluída";
    }
}
