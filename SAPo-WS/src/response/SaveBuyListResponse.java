package response;

public class SaveBuyListResponse {

	int respCode;
	String message;
	
	public SaveBuyListResponse(int respCode, String message) {
		super();
		this.respCode = respCode;
		this.message = message;
	}

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
