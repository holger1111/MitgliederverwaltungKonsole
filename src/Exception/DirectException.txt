package Exception;

public class DirectException extends Exception {
    private static final long serialVersionUID = 1L;

    public DirectException() {
        super();
    }

    public DirectException(String message) {
        super(message);
    }

    public DirectException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectException(Throwable cause) {
        super(cause);
    }
}