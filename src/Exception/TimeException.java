package Exception;

public class TimeException extends DateException {
    private static final long serialVersionUID = 1L;

    public TimeException() {
        super();
    }

    public TimeException(String message) {
        super(message);
    }

    public TimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeException(Throwable cause) {
        super(cause);
    }
}