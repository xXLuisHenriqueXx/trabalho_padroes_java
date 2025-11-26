package model.state;

import model.Task;

public class InProgressState extends TaskState {

    public InProgressState(Task task) {
        super(task);
    }

    @Override
    public void execute() {
        System.out.println("A tarefa já está em progresso.");
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
