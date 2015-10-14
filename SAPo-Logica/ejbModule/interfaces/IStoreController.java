package interfaces;

import java.util.List;

import javax.ejb.Local;

import datatypes.DataBuyList;
import datatypes.DataCategory;
import datatypes.DataElementBuyList;
import datatypes.DataProduct;
import datatypes.DataStore;
import datatypes.DataUser;

@Local
public interface IStoreController {
	
	public void createStore(String name, String addr, String tel, String city, DataUser dUser);
	
	public void createProduct(String name, String description, int stockIni, int precioCompra, int precioVenta, DataStore store);
	
	public List<DataProduct> findSpecificProductsStore(int idStore);
	
	public List<DataCategory> findSpecificCategoriesStore(int idStore);
	
	public List<DataProduct> findGenericProductsStore(int idStore);
	
	public List<DataCategory> findGenericCategoriesStore(int idStore);
	
	public void editProductBasic(DataProduct product);
	
	public List<DataBuyList> findBuyListsStore(int idStore);
	
	public void editElementBuyList(DataElementBuyList element);
	
}
