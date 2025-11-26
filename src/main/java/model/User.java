package model;

import java.util.ArrayList;
import java.util.List;
import observer.Observer;

public class User implements Observer {

    private String name;
    private String email;
    private final List<Board> boards;
    private final List<String> notifications;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.boards = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public void addBoard(Board board) {
        if (board != null && !boards.contains(board)) {
            boards.add(board);
            board.addMember(this);
            board.addObserver(this);
        }
    }

    public void removerBoard(Board board) {
        if (boards.remove(board)) {
            board.removeObserver(this);
        }
    }

    public void viewNotifications() {
        System.out.println("Notificações de " + name + ":");
        if (notifications.isEmpty()) {
            System.out.println("Nenhuma notificação no momento.");
        } else {
            notifications.forEach(msg -> System.out.println(" - " + msg));
        }
    }

    @Override
    public void update(String message) {
        notifications.add(message);
        System.out.println("[" + name + "] recebeu notificação: " + message);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    @Override
    public String toString() {
        return (
            "Usuario{" +
            "nome = '" +
            name +
            '\'' +
            ", email = '" +
            email +
            '\'' +
            ", quadros = " +
            boards.size() +
            '}'
        );
    }
}
