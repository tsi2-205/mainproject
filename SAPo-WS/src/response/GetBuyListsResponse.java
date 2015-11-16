package response;

import java.util.List;

import datatypes.DataBuyList;

public class GetBuyListsResponse {

	int respCode;
	String message;
	List<DataBuyList> buyList;
	
	public GetBuyListsResponse(int respCode, String message, List<DataBuyList> buyList) {
		super();
		this.respCode = respCode;
		this.message = message;
		this.buyList = buyList;
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

	public List<DataBuyList> getBuyList() {
		return buyList;
	}

	public void setBuyList(List<DataBuyList> buyList) {
		this.buyList = buyList;
	}
}
