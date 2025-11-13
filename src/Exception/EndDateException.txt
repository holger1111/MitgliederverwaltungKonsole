package Exception;

public class EndDateException extends DateException {
    private static final long serialVersionUID = 1L;

    public EndDateException() {
        super();
    }

    public EndDateException(String message) {
        super(message);
    }

    public EndDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EndDateException(Throwable cause) {
        super(cause);
    }
}