package controladores;

import interfaces.IStoreController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.sql.Blob;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.rowset.serial.SerialException;

import datatypes.DataCategory;
import datatypes.DataHistoricPrecioCompra;
import datatypes.DataHistoricPrecioVenta;
import datatypes.DataHistoricStock;
import datatypes.DataProduct;
import datatypes.DataStore;
import datatypes.DataUser;
import entities.Customer;
import entities.HistoricPrecioCompra;
import entities.HistoricPrecioVenta;
import entities.HistoricStock;
import entities.Product;
import entities.Registered;
import entities.SpecificProduct;
import entities.Stock;
import entities.Store;
import exceptions.ExistStoreException;

@Stateless
public class StoreController implements IStoreController {
	
	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;
	
	public void createStore (String name, String addr, String tel, String city, DataUser dUser) throws ExistStoreException {
		String queryStr = "SELECT s FROM Store s WHERE s.name = :name AND (s.owner.id = :idUser OR EXISTS (SELECT usr FROM s.guests usr WHERE usr.id = :idUser))";
		Query query = em.createQuery(queryStr, Store.class);
		query.setParameter("name", name);
		query.setParameter("idUser", dUser.getId());
		if (!query.getResultList().isEmpty()) {
			throw new ExistStoreException("Ya tiene un almacen asociado con nombre " + name);
		}
		Registered u = em.find(Registered.class, dUser.getId());
		Store s = new Store(name, addr, tel, city, u);
		Customer c = new Customer();
		em.persist(c);
		em.merge(c);
		s.setCustomer(c);
		em.persist(s);
		em.flush();
		
	}
	
	public List<DataProduct> findProductsStoreName(int idStore, String name) {
		List<DataProduct> result = new LinkedList<DataProduct>();
		
		return result;
	}
	
//	public List<DataProduct> findGenericProductsStore(int idStore) {
//		Store store = em.find(Store.class, idStore);
//		List<DataProduct> result = new LinkedList<DataProduct>();
//		for (Product p: store.getGenericsProducts()) {
//			result.add(new DataProduct(p));
//		}
//		return result;
//	}
	
//	public List<DataCategory> findGenericCategoriesStore(int idStore) {
//		Store store = em.find(Store.class, idStore);
//		List<DataCategory> result = new LinkedList<DataCategory>();
//		for (Category c: store.getCategories()) {
//			if (c instanceof GenericCategory) {
//				result.add(new DataCategory(c));
//			}
//		}
//		return result;
//	}
	
	public List<DataHistoricStock> findHistoricStockProduct(int idProduct) {
//		String queryStr = "SELECT hs FROM HistoricStock hs join SpecificProduct p join Store s WHERE s.id = :idStore AND p.id = :idProduct Order by hs.fecha desc";
//		Query query = em.createQuery(queryStr, HistoricStock.class);
//		query.setParameter("idStore", idStore);
//		query.setParameter("idProduct", idProduct);
		
		SpecificProduct p = em.find(SpecificProduct.class, idProduct);
		List<DataHistoricStock> result = new LinkedList<DataHistoricStock>();
		for (HistoricStock hs: p.getHistoricStock()) {
			result.add(new DataHistoricStock(hs));
		}
		return result;
	}
	
	public List<DataHistoricStock> findHistoricStock(int idStore) {
		String queryStr = "SELECT hs FROM HistoricStock hs join hs.store s WHERE s.id = :idStore Order by hs.fecha desc";
		Query query = em.createQuery(queryStr, HistoricStock.class);
		query.setParameter("idStore", idStore);
		
		List<DataHistoricStock> result = new LinkedList<DataHistoricStock>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricStock((HistoricStock)o));
		}
		return result;
	}
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompraProducto(int idProduct) {
//		String queryStr = "SELECT hpc FROM HistoricPrecioCompra hpc join hpc.product p join hpc.store s WHERE s.id = :idStore AND p.id = :idProduct Order by hpc.fecha desc";
//		Query query = em.createQuery(queryStr, HistoricPrecioCompra.class);
//		query.setParameter("idStore", idStore);
//		query.setParameter("idProduct", idProduct);
		
		SpecificProduct p = em.find(SpecificProduct.class, idProduct);
		List<DataHistoricPrecioCompra> result = new LinkedList<DataHistoricPrecioCompra>();
		for (HistoricPrecioCompra hpc: p.getHistoricsPreciosCompra()) {
			result.add(new DataHistoricPrecioCompra(hpc));
		}
		return result;
	}
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompra(int idStore) {
		String queryStr = "SELECT hpc FROM HistoricPrecioCompra hpc join hpc.store s WHERE s.id = :idStore Order by hpc.fecha desc";
		Query query = em.createQuery(queryStr, HistoricPrecioCompra.class);
		query.setParameter("idStore", idStore);
		
		List<DataHistoricPrecioCompra> result = new LinkedList<DataHistoricPrecioCompra>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricPrecioCompra((HistoricPrecioCompra)o));
		}
		return result;
	}
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVentaProducto(int idProduct) {
//		String queryStr = "SELECT hpv FROM HistoricPrecioVenta hpv join Product p join Store s WHERE s.id = :idStore AND p.id = :idProduct Order by hpv.fecha desc";
//		Query query = em.createQuery(queryStr, HistoricPrecioVenta.class);
//		query.setParameter("idStore", idStore);
//		query.setParameter("idProduct", idProduct);
		SpecificProduct p = em.find(SpecificProduct.class, idProduct);
		List<DataHistoricPrecioVenta> result = new LinkedList<DataHistoricPrecioVenta>();
		for (HistoricPrecioVenta hpv: p.getHistoricsPreciosVenta()) {
			result.add(new DataHistoricPrecioVenta(hpv));
		}
		return result;
	}
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVenta(int idStore) {
		String queryStr = "SELECT hpv FROM HistoricPrecioVenta hpv join hpv.store s WHERE s.id = :idStore Order by hpv.fecha desc";
		Query query = em.createQuery(queryStr, HistoricPrecioVenta.class);
		query.setParameter("idStore", idStore);
		
		List<DataHistoricPrecioVenta> result = new LinkedList<DataHistoricPrecioVenta>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricPrecioVenta((HistoricPrecioVenta)o));
		}
		return result;
	}
	
	
	public String getCustomizeStore(int id) throws SQLException, IOException{
		String queryStr = " SELECT c FROM Store c" + " WHERE c.id = :id";
		Query query = em.createQuery(queryStr, Store.class);
		query.setParameter("id", id);
		Store s = (Store)query.getSingleResult();
		Customer c = s.getCustomer();
		Blob css=c.getCss();
		String ret=null;
		if (css!=null){
			byte[] bdata = css.getBytes(1, (int) css.length());
			ret =  new String(bdata);
		}
		return ret;
//		return "aa";
	}
	
	/*public void setCustomizeStore(int store,File rutaCss) throws SerialException, SQLException{
		Store s= em.find(Store.class, store);
        Customer c= s.getCustomer();
        c.setCss(rutaCss);
        em.persist(c);
	}	*/
	
	public void setCustomizeStore(int store, byte[] rutaCss) throws SerialException, SQLException{
		Store s= em.find(Store.class, store);
        Customer c= s.getCustomer();
        Blob b = new javax.sql.rowset.serial.SerialBlob(rutaCss);
        c.setCss(b);
        em.persist(c);
	}
	
	public List<DataUser> getShareUsersFromStore(int storeId) {
		Store s = em.find(Store.class, storeId);
		List<DataUser> result = new LinkedList<DataUser>();
		String queryStr = "SELECT r FROM Registered r WHERE r NOT IN (SELECT g FROM Store s join s.guests g WHERE s.id = :idStore) ";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("idStore", storeId);
		for (Object o: query.getResultList()) {
			Registered r = (Registered)o;
			if (s.getOwner().getId() != r.getId()) {
				result.add(new DataUser((Registered)o));
			}
		}
		return result;
	}
	
	public List<DataStore> getStores() {
		List<DataStore> ret = new LinkedList<DataStore>();
		String queryStr = "SELECT s FROM Store s";
		Query query = em.createQuery(queryStr, Store.class);
		for (Object o: query.getResultList()) {
			ret.add(new DataStore((Store)o));
		}
		return ret;
	}
	
	public DataUser getOwnerStore(int idStore) {
		Registered user = (Registered) (em.find(Store.class, idStore)).getOwner();
		return new DataUser(user.getId(), user.getEmail(), null, user.getFbId(), user.getName(),user.getAccount(), 1);
		
	}
	
	public List<DataUser> getGuestsStore(int idStore) {
		List<DataUser> ret = new LinkedList<DataUser>();
		List<Registered> users = (List<Registered>) (em.find(Store.class, idStore)).getGuests();
		for (Registered r: users) {
			ret.add(new DataUser(r.getId(), r.getEmail(), null, r.getFbId(), r.getName(), r.getAccount(), 1));
		}
		return ret;
	}
	
	public int getValueStore(int idStore) {
		int val = 0;
		Store s = em.find(Store.class, idStore);
		for (Stock stk: s.getStocks()) {
			val += stk.getCantidad() * stk.getPrecioVenta();
		}
		return val;
	}
	
	public DataCategory findCategoryProduct(int idProduct) {
		Product p = em.find(Product.class, idProduct);
		DataCategory category = new DataCategory(p.getCategory());
		return category;
	}
	
	public String findImageProduct(int idProduct){
		return em.find(SpecificProduct.class, idProduct).getImagenProducto();
	}
	
	public List<DataUser> findUsers() {
		List<DataUser> ret = new LinkedList<DataUser>();
		String queryStr = "SELECT reg FROM Registered reg";
		Query query = em.createQuery(queryStr, Registered.class);
		for (Object o: query.getResultList()) {
			Registered r = (Registered)o;
			ret.add(new DataUser(r.getId(), r.getEmail(), r.getPassword(), r.getFbId(), r.getName(), r.getAccount(), 1));
		}
		return ret;
	}
	
	public void shareStore(int storeId, List<DataUser> users) {
		Store s = em.find(Store.class, storeId);
		for (DataUser u: users) {
			Registered r = em.find(Registered.class, u.getId());
			s.getGuests().add(r);
		}
		em.merge(s);
	}

	public List<DataHistoricStock> findHistoricStockProductDate(int idStore, int idProduct, Calendar fechaIni, Calendar fechaFin) {
		List<DataHistoricStock> ret = new LinkedList<DataHistoricStock>();
		SpecificProduct sp = em.find(SpecificProduct.class, idProduct);
		for (HistoricStock hs: sp.getHistoricStock()) {
			if ((!hs.getFecha().before(fechaIni)) && (!hs.getFecha().after(fechaFin))) {
				ret.add(new DataHistoricStock(hs));
			}
		}
		return ret;
	}

}
