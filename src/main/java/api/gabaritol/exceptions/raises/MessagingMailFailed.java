package api.gabaritol.exceptions.raises;

public class MessagingMailFailed extends RuntimeException {
    public MessagingMailFailed(String message) {
        super(message);
    }

    public MessagingMailFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
