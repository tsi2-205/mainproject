package exceptions;

public class ExistCategoryException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public ExistCategoryException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExistCategoryException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExistCategoryException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ExistCategoryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ExistCategoryException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
