package Exception;

public class TooLongException extends Exception {
    private static final long serialVersionUID = 1L;

    public TooLongException(String message) {
        super(message);
    }
}
