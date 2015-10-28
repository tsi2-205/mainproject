package exceptions;

public class ExistStoreException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public ExistStoreException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExistStoreException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExistStoreException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ExistStoreException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ExistStoreException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
