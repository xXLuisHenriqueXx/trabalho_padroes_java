package observer;

public class NotificationEvent {

    private final String source;
    private final String message;

    public NotificationEvent(String source, String message) {
        this.source = source;
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }
}
