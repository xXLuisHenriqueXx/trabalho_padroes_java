package model.state;

import model.Task;
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
    }

    @Override
    public void complete() {
        LOGGER.info(
            "Não é possível concluir uma tarefa que ainda não foi iniciada."
        );
    }

    @Override
    public void reopen() {
        System.out.println("A tarefa já está no estado 'A Fazer'.");
    }

    @Override
    public String getStateName() {
        return "A Fazer";
    }
}
