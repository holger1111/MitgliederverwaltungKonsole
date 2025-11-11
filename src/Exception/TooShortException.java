package Exception;

public class TooShortException extends Exception {
    private static final long serialVersionUID = 1L;

    public TooShortException(String message) {
        super(message);
    }
}
