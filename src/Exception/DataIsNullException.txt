package Exception;

public class DataIsNullException extends EntryException {
    private static final long serialVersionUID = 1L;

    public DataIsNullException(String message) {
        super(message);
    }

    public DataIsNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
