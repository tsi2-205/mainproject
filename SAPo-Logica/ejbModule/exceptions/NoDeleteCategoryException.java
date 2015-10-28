package exceptions;

public class NoDeleteCategoryException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public NoDeleteCategoryException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NoDeleteCategoryException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoDeleteCategoryException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NoDeleteCategoryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NoDeleteCategoryException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
