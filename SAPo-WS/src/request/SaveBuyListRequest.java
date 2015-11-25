package request;

import datatypes.DataBuyList;

public class SaveBuyListRequest {

	int storeId;
	DataBuyList buyList;

	public SaveBuyListRequest() {
		super();
	}

	public SaveBuyListRequest(DataBuyList buyList, int storeId) {
		super();
		this.buyList = buyList;
		this.storeId = storeId;
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
}
