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
import datatypes.DataProduct;
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
import exceptions.ExistStoreException;
import exceptions.NoDeleteCategoryException;
import exceptions.ProductNotExistException;

@Stateless
@WebService
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
		em.persist(s);
	}
	
	public void createSpecificProduct(String name, String description, int stockIni, int precioCompra, int precioVenta, DataStore store, List<DataProductAdditionalAttribute> additionalAttributes, int idCategory) {
		Store s = em.find(Store.class, store.getId());
		Category cat = em.find(Category.class, idCategory);
		SpecificProduct p = new SpecificProduct(name, description, s);
		p.setCategory(cat);
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
	
	public void createSpecificCategory(String name, String description, DataStore store, DataCategory fatherCat) throws ExistStoreException {
		String queryStr = "SELECT cat FROM Store s JOIN s.categories cat WHERE s.id = :idStore AND cat.name = :name";
		Query query = em.createQuery(queryStr, Category.class);
		query.setParameter("name", name);
		query.setParameter("idStore", store.getId());
		if (!query.getResultList().isEmpty()) {
			throw new ExistStoreException("Ya tiene una categoría asociada con nombre " + name);
		}
		Store s = em.find(Store.class, store.getId());
		if (fatherCat != null) {
			SpecificCategory father = em.find(SpecificCategory.class, fatherCat.getId());
			SpecificCategory cat = new SpecificCategory(name, description, father, null);
			em.persist(cat);
		} else {
			SpecificCategory cat = new SpecificCategory(name, description, null, s);
			em.persist(cat);
		}
	}
	
	public void editSpecificCategory(String name, String description, DataCategory category) throws ExistStoreException {
		String queryStr = "SELECT cat FROM Category cat WHERE cat.id <> :idCat AND cat.name = :name";
		Query query = em.createQuery(queryStr, Category.class);
		query.setParameter("name", name);
		query.setParameter("idCat", category.getId());
		if (!query.getResultList().isEmpty()) {
			throw new ExistStoreException("Ya tiene una categoría asociada con nombre " + name);
		}
		Category c = em.find(Category.class, category.getId());
		if (c != null) {
			c.setName(name);
			c.setDescription(description);
			em.merge(c);
		}
	}
	
	public void deleteSpecificCategory(DataCategory category) throws NoDeleteCategoryException {
		Category c = em.find(Category.class, category.getId());
		if (c != null) {
			if (!c.getSonsCategories().isEmpty()) {
				throw new NoDeleteCategoryException("Esta categoría posee subcategorías");
			}
			if (!c.getProducts().isEmpty()) {
				throw new NoDeleteCategoryException("Esta categoría posee productos");
			}
			c.setFatherCategory(null);
			em.remove(c);
		}
	}
	
	private List<Category> obtenerAncestros(Category cat) {
		List<Category> result = new LinkedList<Category>();
		result.add(cat);
		Category aux = cat;
		while (aux.getFatherCategory() != null) {
			aux = aux.getFatherCategory();
			result.add(aux);
		}
		return result;
	}
	
	public List<DataStock> findStockProductsStore(int idStore, Integer idCategory) {
		Store store = em.find(Store.class, idStore);
		List<DataStock> result = new LinkedList<DataStock>();
		if (idCategory == null) {
			for (Stock s: store.getStocks()) {
				result.add(new DataStock(s));
			}
		} else {
			Category cat = em.find(Category.class, idCategory);
			for (Stock s: store.getStocks()) {
				List<Category> cats = obtenerAncestros(s.getProduct().getCategory());
				if (cats.contains(cat)) {
					result.add(new DataStock(s));
				}
			}
		}
		return result;
	}
	
	public List<DataProduct> findProductsStore(int idStore, Integer idCategory) {
		Store store = em.find(Store.class, idStore);
		List<DataProduct> result = new LinkedList<DataProduct>();
		if (idCategory == null) {
			for (Product p: store.getSpecificsProducts()) {
				result.add(new DataProduct(p));
			}
			for (Product p: store.getGenericsProducts()) {
				result.add(new DataProduct(p));
			}
		} else {
			Category cat = em.find(Category.class, idCategory);
			for (Product p: store.getSpecificsProducts()) {
				List<Category> cats = obtenerAncestros(p.getCategory());
				if (cats.contains(cat)) {
					result.add(new DataProduct(p));
				}
			}
			for (Product p: store.getGenericsProducts()) {
				List<Category> cats = obtenerAncestros(p.getCategory());
				if (cats.contains(cat)) {
					result.add(new DataProduct(p));
				}
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
	
	public void createBuyListStore(int idStore, List<DataStock> listProducts, String name, String description) throws ProductNotExistException {
		
		Store store = em.find(Store.class, idStore);
		List<ElementBuyList> elements = new LinkedList<ElementBuyList>();
		for (DataStock ds: listProducts) {
			Product p = em.find(Product.class, ds.getProduct().getId());
			if (p == null) {
				throw new ProductNotExistException("No existe el producto " + ds.getProduct().getName());
			}
			ElementBuyList elem = new ElementBuyList(ds.getCantidad(), p);
			elements.add(elem);
//			em.persist(elem);
		}
		Calendar fechaActual = new GregorianCalendar();
		BuyList buyList = new BuyList(name, description, fechaActual, store, elements);
		em.persist(buyList);
	}
	
	public void deleteBuyListsStore(int idBuyList, int idStore) {
		BuyList buyList = em.find(BuyList.class, idBuyList);
		if (buyList == null) {
//			Excepcion
		}
		em.remove(buyList);
	}
	
	public void editBuyListStore(int idStore, List<DataElementBuyList> listProducts, String name, String description, DataBuyList dataBuyList) throws ProductNotExistException {
		BuyList buyList = em.find(BuyList.class, dataBuyList.getId());
		if (buyList == null) {
//			No existe buy list
		}
		if (!buyList.getName().equals(name)) {
			//Chequeo que no exista otra lista con igual nombre
			buyList.setName(name);
		}
		if (!buyList.getDescription().equals(description)) {
			buyList.setDescription(description);
		}
		
		for (DataElementBuyList elemNew: listProducts) {
			boolean add = true;
			for (ElementBuyList elemList: buyList.getElements()) {
				if (elemList.getProduct().getId() == elemNew.getProduct().getId()) {
					add = false;
					if (elemList.getQuantity() != elemNew.getQuantity()) {
						elemList.setQuantity(elemNew.getQuantity());
					}
					break;
				}
			}
			if (add) {
				Product prodAdd = em.find(Product.class, elemNew.getProduct().getId());
				if (prodAdd == null) {
					throw new ProductNotExistException("No existe el producto " + elemNew.getProduct().getName());
				}
				buyList.getElements().add(new ElementBuyList(elemNew.getQuantity(), prodAdd));
			}
		}
		List<ElementBuyList> listRemove = new LinkedList<ElementBuyList>();
		for (ElementBuyList elemList: buyList.getElements()) {
			boolean remove = true;
			for (DataElementBuyList elemNew: listProducts) {
				if (elemList.getProduct().getId() == elemNew.getProduct().getId()) {
					remove = false;
					break;
				}
			}
			if (remove) {
				listRemove.add(elemList);
			}
		}
		buyList.getElements().removeAll(listRemove);
		while (!listRemove.isEmpty()) {
			em.remove(listRemove.remove(0));
		}
		em.merge(buyList);
	}

}
