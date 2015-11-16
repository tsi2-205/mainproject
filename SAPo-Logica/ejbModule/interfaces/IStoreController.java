package interfaces;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Local;
import javax.sql.rowset.serial.SerialException;

import datatypes.DataCategory;
import datatypes.DataHistoricPrecioCompra;
import datatypes.DataHistoricPrecioVenta;
import datatypes.DataHistoricStock;
import datatypes.DataProduct;
import datatypes.DataStore;
import datatypes.DataUser;
import exceptions.ExistStoreException;

@Local
public interface IStoreController {
	
	public void createStore(String name, String addr, String tel, String city, DataUser dUser) throws ExistStoreException;
	
	public List<DataProduct> findProductsStoreName(int idStore, String name);
	
//	public List<DataProduct> findGenericProductsStore(int idStore);
	
//	public List<DataCategory> findGenericCategoriesStore(int idStore);
	
	public List<DataHistoricStock> findHistoricStockProduct(int idProduct);
	
	public List<DataHistoricStock> findHistoricStock(int idStore);
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompraProducto(int idProduct);
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompra(int idStore);
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVentaProducto(int idProduct);
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVenta(int idStore);

	public void setCustomizeStore(int store, byte[] rutaCss) throws SerialException, SQLException;
	
	public String getCustomizeStore(int id) throws SQLException, IOException;
	
	public List<DataUser> getShareUsersFromStore(int storeId);
	
	public List<DataStore> getStores();
	
	public DataUser getOwnerStore(int idStore);
	
	public List<DataUser> getGuestsStore(int idStore);
	
	public int getValueStore(int idStore);
	
	public DataCategory findCategoryProduct(int idProduct);
	
	public List<DataUser> findUsers();
	
	public void shareStore(int storeId, List<DataUser> users);


	public List<DataHistoricStock> findHistoricStockProductDate(int idStore, int idProduct, Calendar fechaIni, Calendar fechaFin);


}
