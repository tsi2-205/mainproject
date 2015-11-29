package request;

import datatypes.DataBuyList;

public class SaveBuyListRequest {

	int userId;
	int storeId;
	DataBuyList buyList;

	public SaveBuyListRequest() {
		super();
	}

	public SaveBuyListRequest(DataBuyList buyList, int storeId, int userId) {
		super();
		this.buyList = buyList;
		this.storeId = storeId;
		this.userId = userId;
	}
	
	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public DataBuyList getBuyList() {
		return buyList;
	}

	public void setBuyList(DataBuyList buyList) {
		this.buyList = buyList;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
