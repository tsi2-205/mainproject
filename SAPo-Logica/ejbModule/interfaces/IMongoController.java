package interfaces;

import java.util.List;

import controladores.MongoController;

public interface IMongoController {
	
	
	public List<datatypes.MongoAdditionalData> getAdditionalDataFromProduct (String productid);
	
	public void addAdditionalDataToProduct ( List<datatypes.MongoAdditionalData> listData );
}
