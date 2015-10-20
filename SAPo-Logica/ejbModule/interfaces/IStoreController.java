package interfaces;

import java.util.List;

import javax.ejb.Local;

import datatypes.DataBuyList;
import datatypes.DataCategory;
import datatypes.DataElementBuyList;
import datatypes.DataHistoricPrecioCompra;
import datatypes.DataHistoricPrecioVenta;
import datatypes.DataHistoricStock;
import datatypes.DataProductAdditionalAttribute;
import datatypes.DataStock;
import datatypes.DataStore;
import datatypes.DataUser;

@Local
public interface IStoreController {
	
	public void createStore(String name, String addr, String tel, String city, DataUser dUser);
	
	public void createSpecificProduct(String name, String description, int stockIni, int precioCompra, int precioVenta, DataStore store, List<DataProductAdditionalAttribute> additionalAttributes);
	
	public List<DataStock> findStockProductsStore(int idStore);
	
	public List<DataCategory> findSpecificCategoriesStore(int idStore);
	
//	public List<DataProduct> findGenericProductsStore(int idStore);
	
	public List<DataCategory> findGenericCategoriesStore(int idStore);
	
	public void editProductBasic(DataStock stock, int idStore);
	
	public List<DataHistoricStock> findHistoricStockProducto(int idStore, int idProduct);
	
	public List<DataHistoricStock> findHistoricStock(int idStore);
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompraProducto(int idStore, int idProduct);
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompra(int idStore);
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVentaProducto(int idStore, int idProduct);
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVenta(int idStore);
	
	public List<DataBuyList> findBuyListsStore(int idStore);
	
	public void editElementBuyList(DataElementBuyList element);
	
}
