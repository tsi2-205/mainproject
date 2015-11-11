package interfaces;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
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
import exceptions.CategoryNotExistException;
import exceptions.ExistCategoryException;
import exceptions.ExistStoreException;
import exceptions.NoDeleteCategoryException;
import exceptions.ProductNotExistException;

@Local
public interface IStoreController {
	
	public void createStore(String name, String addr, String tel, String city, DataUser dUser) throws ExistStoreException;
	
	public void createSpecificCategory(String name, String description, DataStore store, DataCategory fatherCat) throws ExistStoreException;
	
	public void editSpecificCategory(String name, String description, DataCategory category) throws ExistStoreException;
	
	public void deleteSpecificCategory(DataCategory category) throws NoDeleteCategoryException;
	
	public List<DataStock> findStockProductsStore(int idStore, Integer idCategory);
	
	public List<DataProduct> findProductsStore(int idStore, Integer idCategory);
	
	public List<DataProduct> findProductsStoreName(int idStore, String name);
	
	public List<DataCategory> findSpecificCategoriesStore(int idStore);
	
//	public List<DataProduct> findGenericProductsStore(int idStore);
	
//	public List<DataCategory> findGenericCategoriesStore(int idStore);
	
	public void editProductBasic(DataStock stock, int idStore);
	
	public List<DataHistoricStock> findHistoricStockProduct(int idProduct);
	
	public List<DataHistoricStock> findHistoricStock(int idStore);
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompraProducto(int idProduct);
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompra(int idStore);
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVentaProducto(int idProduct);
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVenta(int idStore);
	
	public List<DataBuyList> findBuyListsStore(int idStore);
	
	public void editElementBuyList(DataElementBuyList element);
	
	public void createBuyListStore(int idStore, List<DataStock> listProducts, String name, String description) throws ProductNotExistException;
	
	public void deleteBuyListsStore(int idBuyList, int idStore);
	
	public void editBuyListStore(int idStore, List<DataElementBuyList> listProducts, String name, String description, DataBuyList dataBuyList) throws ProductNotExistException;

	public DataBuyList findBuyList(int idBuyList);
	
	public void checkElementBuyList(int idElementBuyList, int idStore, int precio);

	public void setCustomizeStore(int store, byte[] rutaCss) throws SerialException, SQLException;
	
	public String getCustomizeStore(int id) throws SQLException, IOException;
	
	public List<DataUser> getShareUsersFromStore(int storeId);
	
	public List<DataProduct> findGenericsProducts(Integer idCategory) throws CategoryNotExistException;
	
	public void createGenericProduct(String name, String description, List<DataProductAdditionalAttribute> additionalAttributes, int idCategory) throws CategoryNotExistException;
	
	public void createGenericCategory(String name, String description, DataCategory fatherCat) throws ExistCategoryException;
	
	public void editGenericCategory(String name, String description, DataCategory category) throws ExistCategoryException;
	
	public void deleteGenericCategory(DataCategory category) throws NoDeleteCategoryException;
	
	public List<DataStore> getStores();
	
	public DataUser getOwnerStore(int idStore);
	
	public List<DataUser> getGuestsStore(int idStore);
	
	public int getValueStore(int idStore);
	
	public DataCategory findCategoryProduct(int idProduct);
	
	public void editGenericProduct(DataProduct product, int idCategory) throws ExistCategoryException;
	
	public void deleteAttributeProduct(int idProduct, int idAttribute);
	
	public List<DataUser> findUsers();
	
	public List<DataCategory> findGenericCategories();
	
	public void shareStore(int storeId, List<DataUser> users);


	public List<DataHistoricStock> findHistoricStockProductDate(int idStore, int idProduct, Calendar fechaIni, Calendar fechaFin);

	
	public List<DataStock> buyRecommendation(int id, DataBuyList db);


}
