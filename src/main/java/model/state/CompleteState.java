package main.java.model.state;

import main.java.model.Task;

public class CompleteState extends TaskState {

    public CompleteState(Task task) {
        super(task);
    }

    @Override
    public void execute() {
        System.out.println(
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
