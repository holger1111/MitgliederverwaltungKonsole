package New.Exception;

public class NotAllNecessaryDataEnteredException extends EntryException {
    private static final long serialVersionUID = 1L;

    public NotAllNecessaryDataEnteredException(String message) {
        super(message);
    }

    public NotAllNecessaryDataEnteredException(String message, Throwable cause) {
        super(message, cause);
    }
}
