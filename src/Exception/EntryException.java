package Exception;

public class EntryException extends AppException {
    private static final long serialVersionUID = 1L;

    public EntryException() {
        super();
    }

    public EntryException(String message) {
        super(message);
    }

    public EntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntryException(Throwable cause) {
        super(cause);
    }
}