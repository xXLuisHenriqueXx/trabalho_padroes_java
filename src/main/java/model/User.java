package model;

import java.util.ArrayList;
import java.util.List;
import observer.NotificationEvent;
import observer.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User implements Observer {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    private String name;
    private String email;
    private final List<Board> boards;
    private final List<NotificationEvent> notifications;

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
        LOGGER.info("Notificacoes de {}:", name);

        if (notifications.isEmpty()) {
            LOGGER.info("Nenhuma notificacao no momento.");
        } else {
            notifications.forEach(ev ->
                LOGGER.info(" - {}: {}", ev.getSource(), ev.getMessage())
            );
        }
    }

    @Override
    public void update(NotificationEvent event) {
        notifications.add(event);

        LOGGER.info(
            "[{}] recebeu notificacao de {}: {}",
            name,
            event.getSource(),
            event.getMessage()
        );
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

    public List<NotificationEvent> getNotifications() {
        return notifications;
    }
}
