package controladores;

import java.util.LinkedList;
import java.util.List;

import interfaces.IStoreController;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import datatypes.DataCategory;
import datatypes.DataProduct;
import datatypes.DataStore;
import datatypes.DataUser;
import entities.Category;
import entities.GenericCategory;
import entities.GenericProduct;
import entities.Product;
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
	
	public void createProduct(String name, String description, int stockIni, int precioCompra, int precioVenta, DataStore store) {
		Store s = em.find(Store.class, store.getId());
		SpecificProduct p = new SpecificProduct(name, description);
		Stock stock = new Stock(stockIni, precioVenta, precioCompra, s, p);
		p.getStores().add(s);
		em.persist(s);
		em.persist(p);
	}
	
	public List<DataProduct> findSpecificProductsStore(int idStore) {
		Store store = em.find(Store.class, idStore);
		List<DataProduct> result = new LinkedList<DataProduct>();
		for (Product p: store.getProducts()) {
			if (p instanceof SpecificProduct) {
				result.add(new DataProduct(p));
			}
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
	
	public List<DataProduct> findGenericProductsStore(int idStore) {
		Store store = em.find(Store.class, idStore);
		List<DataProduct> result = new LinkedList<DataProduct>();
		for (Product p: store.getProducts()) {
			if (p instanceof GenericProduct) {
				result.add(new DataProduct(p));
			}
		}
		return result;
	}
	
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

}
