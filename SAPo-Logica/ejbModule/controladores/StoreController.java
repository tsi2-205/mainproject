package controladores;

import interfaces.IStoreController;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
import entities.BuyList;
import entities.Category;
import entities.ElementBuyList;
import entities.GenericCategory;
import entities.HistoricPrecioCompra;
import entities.HistoricPrecioVenta;
import entities.HistoricStock;
import entities.Product;
import entities.ProductAdditionalAttribute;
import entities.Registered;
import entities.SpecificCategory;
import entities.SpecificProduct;
import entities.Stock;
import entities.Store;

@Stateless
@WebService
public class StoreController implements IStoreController {
	
	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;
	
	public void createStore (String name, String addr, String tel, String city, DataUser dUser) {
		Registered u = em.find(Registered.class, dUser.getId());
		Store s = new Store(name, addr, tel, city, u);
		em.persist(s);
	}
	
	public void createSpecificProduct(String name, String description, int stockIni, int precioCompra, int precioVenta, DataStore store, List<DataProductAdditionalAttribute> additionalAttributes) {
		Store s = em.find(Store.class, store.getId());
		SpecificProduct p = new SpecificProduct(name, description, s);
		for (DataProductAdditionalAttribute dAdAt: additionalAttributes) {
			ProductAdditionalAttribute newAttribute = new ProductAdditionalAttribute(dAdAt.getNameAttribute(), dAdAt.getValueAttribute());
			em.persist(newAttribute);
			p.getAdditionalAttributes().add(newAttribute);
		}
		Stock stock = new Stock(stockIni, precioVenta, precioCompra, s, p);
		em.persist(s);
		em.persist(p);
		em.persist(stock);
		Calendar fechaActual = new GregorianCalendar();
		HistoricStock hs = new HistoricStock(fechaActual, stockIni, 1, p, s);
		em.persist(hs);
		HistoricPrecioCompra hpc = new HistoricPrecioCompra(fechaActual, precioCompra, 1, p, s);
		em.persist(hpc);
		HistoricPrecioVenta hpv = new HistoricPrecioVenta(fechaActual, precioVenta, 1, p, s);
		em.persist(hpv);
	}
	
	public List<DataStock> findStockProductsStore(int idStore) {
		Store store = em.find(Store.class, idStore);
		List<DataStock> result = new LinkedList<DataStock>();
		for (Stock s: store.getStocks()) {
			result.add(new DataStock(s));
		}
		return result;
	}
	
	public List<DataCategory> findSpecificCategoriesStore(int idStore) {
		Store store = em.find(Store.class, idStore);
		List<DataCategory> result = new LinkedList<DataCategory>();
		for (Category c: store.getCategories()) {
			if (c instanceof SpecificCategory) {
				result.add(new DataCategory(c));
			}
		}
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
	
	public List<DataCategory> findGenericCategoriesStore(int idStore) {
		Store store = em.find(Store.class, idStore);
		List<DataCategory> result = new LinkedList<DataCategory>();
		for (Category c: store.getCategories()) {
			if (c instanceof GenericCategory) {
				result.add(new DataCategory(c));
			}
		}
		return result;
	}
	
	public void editProductBasic(DataStock stock, int idStore) {
		Stock stk = em.find(Stock.class, stock.getId());
		Product p = stk.getProduct();
		Store s = stk.getStore();
		Calendar fechaActual = new GregorianCalendar();
		
		if (stk.getCantidad() != stock.getCantidad()) {
			int tipo;
			if (stk.getCantidad() < stock.getCantidad()) {
				tipo = 1;
			} else {
				tipo = 0;
			}
			HistoricStock hs = new HistoricStock(fechaActual, stock.getCantidad(), tipo, p, s);
			stk.setCantidad(stock.getCantidad());
			em.persist(hs);
		}
		if (stk.getPrecioCompra() != stock.getPrecioCompra()) {
			int tipo;
			if (stk.getPrecioCompra() < stock.getPrecioCompra()) {
				tipo = 1;
			} else {
				tipo = 0;
			}
			HistoricPrecioCompra hpc = new HistoricPrecioCompra(fechaActual, stock.getPrecioCompra(), tipo, p, s);
			stk.setPrecioCompra(stock.getPrecioCompra());
			em.persist(hpc);
		}
		if (stk.getPrecioVenta() != stock.getPrecioVenta()) {
			int tipo;
			if (stk.getPrecioVenta() < stock.getPrecioVenta()) {
				tipo = 1;
			} else {
				tipo = 0;
			}
			HistoricPrecioVenta hpv = new HistoricPrecioVenta(fechaActual, stock.getPrecioVenta(), tipo, p, s);
			stk.setCantidad(stock.getCantidad());
			em.persist(hpv);
			stk.setPrecioVenta(stock.getPrecioVenta());
		}

		p.setName(stock.getProduct().getName());
		em.merge(stk);
		em.merge(p);
	}
	
	public List<DataHistoricStock> findHistoricStockProducto(int idStore, int idProduct) {
		String queryStr = "SELECT hs FROM HistoricStock hs join Product p join Store s WHERE s.id = :idStore AND p.id = :idProduct Order by hs.fecha";
		Query query = em.createQuery(queryStr, HistoricStock.class);
		query.setParameter("idStore", idStore);
		query.setParameter("idProduct", idProduct);
		
		List<DataHistoricStock> result = new LinkedList<DataHistoricStock>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricStock((HistoricStock)o));
		}
		return result;
	}
	
	public List<DataHistoricStock> findHistoricStock(int idStore) {
		String queryStr = "SELECT hs FROM HistoricStock hs join hs.store s WHERE s.id = :idStore Order by hs.fecha";
		Query query = em.createQuery(queryStr, HistoricStock.class);
		query.setParameter("idStore", idStore);
		
		List<DataHistoricStock> result = new LinkedList<DataHistoricStock>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricStock((HistoricStock)o));
		}
		return result;
	}
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompraProducto(int idStore, int idProduct) {
		String queryStr = "SELECT hpc FROM HistoricPrecioCompra hpc join hpc.product p join hpc.store s WHERE s.id = :idStore AND p.id = :idProduct Order by hpc.fecha";
		Query query = em.createQuery(queryStr, HistoricPrecioCompra.class);
		query.setParameter("idStore", idStore);
		query.setParameter("idProduct", idProduct);
		
		List<DataHistoricPrecioCompra> result = new LinkedList<DataHistoricPrecioCompra>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricPrecioCompra((HistoricPrecioCompra)o));
		}
		return result;
	}
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompra(int idStore) {
		String queryStr = "SELECT hpc FROM HistoricPrecioCompra hpc join hpc.store s WHERE s.id = :idStore Order by hpc.fecha";
		Query query = em.createQuery(queryStr, HistoricPrecioCompra.class);
		query.setParameter("idStore", idStore);
		
		List<DataHistoricPrecioCompra> result = new LinkedList<DataHistoricPrecioCompra>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricPrecioCompra((HistoricPrecioCompra)o));
		}
		return result;
	}
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVentaProducto(int idStore, int idProduct) {
		String queryStr = "SELECT hpv FROM HistoricPrecioVenta hpv join Product p join Store s WHERE s.id = :idStore AND p.id = :idProduct Order by hpv.fecha";
		Query query = em.createQuery(queryStr, HistoricPrecioVenta.class);
		query.setParameter("idStore", idStore);
		query.setParameter("idProduct", idProduct);
		
		List<DataHistoricPrecioVenta> result = new LinkedList<DataHistoricPrecioVenta>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricPrecioVenta((HistoricPrecioVenta)o));
		}
		return result;
	}
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVenta(int idStore) {
		String queryStr = "SELECT hpv FROM HistoricPrecioVenta hpv join hpv.store s WHERE s.id = :idStore Order by hpv.fecha";
		Query query = em.createQuery(queryStr, HistoricPrecioVenta.class);
		query.setParameter("idStore", idStore);
		
		List<DataHistoricPrecioVenta> result = new LinkedList<DataHistoricPrecioVenta>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricPrecioVenta((HistoricPrecioVenta)o));
		}
		return result;
	}
	
	public List<DataBuyList> findBuyListsStore(int idStore) {
		Store store = em.find(Store.class, idStore);
		List<DataBuyList> result = new LinkedList<DataBuyList>();
		for (BuyList bl: store.getBuylists()) {
			result.add(new DataBuyList(bl));
		}
		return result;
	}
	
	public void editElementBuyList(DataElementBuyList element) {
		ElementBuyList e = em.find(ElementBuyList.class, element.getId());
		e.setQuantity(element.getQuantity());
		if (e.getProduct().getId() != element.getProduct().getId()) {
			Product p = em.find(Product.class, element.getProduct().getId());
			e.setProduct(p);
		}
	}

}
