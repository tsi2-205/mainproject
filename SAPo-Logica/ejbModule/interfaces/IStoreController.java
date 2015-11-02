package interfaces;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;
import javax.sql.rowset.serial.SerialException;

import datatypes.DataBuyList;
import datatypes.DataCategory;
import datatypes.DataElementBuyList;
import datatypes.DataHistoricPrecioCompra;
import datatypes.DataHistoricPrecioVenta;
import datatypes.DataHistoricStock;
import datatypes.DataProduct;
import datatypes.DataProductAdditionalAttribute;
import datatypes.DataStock;
import datatypes.DataStore;
import datatypes.DataUser;
import exceptions.ExistStoreException;
import exceptions.NoDeleteCategoryException;
import exceptions.ProductNotExistException;

@Local
public interface IStoreController {
	
	public void createStore(String name, String addr, String tel, String city, DataUser dUser) throws ExistStoreException;
	
	public void createSpecificProduct(String name, String description, int stockIni, int precioCompra, int precioVenta, DataStore store, List<DataProductAdditionalAttribute> additionalAttributes, int idCategory);
	
	public void createSpecificCategory(String name, String description, DataStore store, DataCategory fatherCat) throws ExistStoreException;
	
	public void editSpecificCategory(String name, String description, DataCategory category) throws ExistStoreException;
	
	public void deleteSpecificCategory(DataCategory category) throws NoDeleteCategoryException;
	
	public List<DataStock> findStockProductsStore(int idStore, Integer idCategory);
	
	public List<DataProduct> findProductsStore(int idStore, Integer idCategory);
	
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
	
	public void createBuyListStore(int idStore, List<DataStock> listProducts, String name, String description) throws ProductNotExistException;
	
	public void deleteBuyListsStore(int idBuyList, int idStore);
	
	public void editBuyListStore(int idStore, List<DataElementBuyList> listProducts, String name, String description, DataBuyList dataBuyList) throws ProductNotExistException;
	
	public void setCustomizeStore(int store, File rutaCss) throws SerialException, SQLException;
	
	public File getCustomizeStore(int id) throws SQLException, IOException;
}
