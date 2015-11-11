package interfaces;

import java.util.List;

import javax.ejb.Local;

import datatypes.DataProductAdditionalAttribute;
import datatypes.DataStock;
import exceptions.ExistCategoryException;

@Local
public interface IProductController {
	
	public void createSpecificProduct(String name, String description, Integer stockMin, Integer stockMax, int idStore, List<DataProductAdditionalAttribute> additionalAttributes, int idCategory);
	
	public void editProductStore(DataStock stock, int idStore, Integer idCategory) throws ExistCategoryException;
	
	public void changeStockProduct(int idStore, int idProduct, int movCant, int movPrecio, int tipo);
	
	

}
