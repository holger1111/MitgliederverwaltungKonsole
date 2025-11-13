package Exception;

public class DataIsNullException extends EntryException {
    private static final long serialVersionUID = 1L;

    public DataIsNullException() {
        super();
    }

    public DataIsNullException(String message) {
        super(message);
    }

    public DataIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataIsNullException(Throwable cause) {
        super(cause);
    }
}