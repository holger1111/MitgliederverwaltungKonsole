package Exception;

public class DiscountException extends DirectException {
    private static final long serialVersionUID = 1L;

    public DiscountException() {
        super();
    }

    public DiscountException(String message) {
        super(message);
    }

    public DiscountException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiscountException(Throwable cause) {
        super(cause);
    }
}