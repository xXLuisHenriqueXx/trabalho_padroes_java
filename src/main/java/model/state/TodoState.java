package main.java.model.state;

import main.java.model.Task;

public class TodoState extends TaskState {

    public TodoState(Task task) {
        super(task);
    }

    @Override
    public void execute() {
        task.setState(new InProgressState(task));
    }

    @Override
    public void complete() {
        System.out.println(
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
