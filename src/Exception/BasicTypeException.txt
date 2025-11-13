package Exception;

public class BasicTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	public BasicTypeException() {
		super();
	}

	public BasicTypeException(String message) {
		super(message);
	}

	public BasicTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BasicTypeException(Throwable cause) {
		super(cause);
	}
}
