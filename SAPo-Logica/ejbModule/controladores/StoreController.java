package controladores;

import java.util.LinkedList;
import java.util.List;

import interfaces.IStoreController;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import datatypes.DataBuyList;
import datatypes.DataCategory;
import datatypes.DataElementBuyList;
import datatypes.DataProduct;
import datatypes.DataStore;
import datatypes.DataUser;
import entities.BuyList;
import entities.Category;
import entities.ElementBuyList;
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
//		p.getStores().add(s);
		s.getProducts().add(p);
		em.persist(s);
		em.persist(p);
		em.persist(stock);
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
	
	public void editProductBasic(DataProduct product) {
		Product p = em.find(Product.class, product.getId());
		p.setName(product.getName());
		Stock s = p.getStock();
		s.setCantidad(product.getStock().getCantidad());
		s.setPrecioCompra(product.getStock().getPrecioCompra());
		s.setPrecioVenta(product.getStock().getPrecioVenta());
		em.merge(s);
		em.merge(p);
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
