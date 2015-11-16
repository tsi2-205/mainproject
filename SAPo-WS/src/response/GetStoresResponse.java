package response;

import java.util.List;

import datatypes.DataStore;

public class GetStoresResponse {
	
	int respCode;
	String message;
	List<DataStore> myStores;
	List<DataStore> sharedStores;
	
	public GetStoresResponse(int respCode, String message, List<DataStore> myStores, List<DataStore> sharedStores) {
		super();
		this.respCode = respCode;
		this.message = message;
		this.myStores = myStores;
		this.sharedStores = sharedStores;
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

	public List<DataStore> getMyStores() {
		return myStores;
	}

	public void setMyStores(List<DataStore> myStores) {
		this.myStores = myStores;
	}

	public List<DataStore> getSharedStores() {
		return sharedStores;
	}

	public void setSharedStores(List<DataStore> sharedStores) {
		this.sharedStores = sharedStores;
	}
}
