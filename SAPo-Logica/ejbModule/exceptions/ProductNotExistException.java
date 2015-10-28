package exceptions;

public class ProductNotExistException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public ProductNotExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductNotExistException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProductNotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ProductNotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ProductNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
