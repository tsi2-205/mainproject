package controladores;

import interfaces.IStoreController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
import entities.BuyList;
import entities.Category;
import entities.Customer;
import entities.ElementBuyList;
import entities.GenericCategory;
import entities.GenericProduct;
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
import exceptions.CategoryNotExistException;
import exceptions.ExistCategoryException;
import exceptions.ExistStoreException;
import exceptions.NoDeleteCategoryException;
import exceptions.ProductNotExistException;

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
		s.setCustomer(c);
		em.persist(s);
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
//			for (Product p: store.getGenericsProducts()) {
//				result.add(new DataProduct(p));
//			}
		} else {
			Category cat = em.find(Category.class, idCategory);
			for (Product p: store.getSpecificsProducts()) {
				List<Category> cats = obtenerAncestros(p.getCategory());
				if (cats.contains(cat)) {
					result.add(new DataProduct(p));
				}
			}
//			for (Product p: store.getGenericsProducts()) {
//				List<Category> cats = obtenerAncestros(p.getCategory());
//				if (cats.contains(cat)) {
//					result.add(new DataProduct(p));
//				}
//			}
		}
		return result;
	}
	
	public List<DataProduct> findProductsStoreName(int idStore, String name) {
		List<DataProduct> result = new LinkedList<DataProduct>();
		
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
	
	// Chequear que no exista otro producto co el mismo nombre en el almacen
	public void editProductBasic(DataStock stock, int idStore) {
		Stock stk = em.find(Stock.class, stock.getId());
		SpecificProduct p = stk.getProduct();
		Store s = stk.getStore();
		Calendar fechaActual = new GregorianCalendar();
		
		if (stk.getCantidad() != stock.getCantidad()) {
			int tipo;
			int cant;
			int precio;
			if (stk.getCantidad() < stock.getCantidad()) {
				tipo = 1;
				cant = stock.getCantidad() - stk.getCantidad();
				precio = cant * stock.getPrecioCompra();
			} else {
				tipo = 0;
				cant = stk.getCantidad() - stock.getCantidad();
				precio = cant * stock.getPrecioVenta();
			}
			HistoricStock hs = new HistoricStock(fechaActual, stock.getCantidad(), cant, precio, tipo, p, s);
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
	
	public List<DataHistoricStock> findHistoricStockProduct(int idProduct) {
//		String queryStr = "SELECT hs FROM HistoricStock hs join SpecificProduct p join Store s WHERE s.id = :idStore AND p.id = :idProduct Order by hs.fecha";
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
		String queryStr = "SELECT hs FROM HistoricStock hs join hs.store s WHERE s.id = :idStore Order by hs.fecha";
		Query query = em.createQuery(queryStr, HistoricStock.class);
		query.setParameter("idStore", idStore);
		
		List<DataHistoricStock> result = new LinkedList<DataHistoricStock>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricStock((HistoricStock)o));
		}
		return result;
	}
	
	public List<DataHistoricPrecioCompra> findHistoricPrecioCompraProducto(int idProduct) {
//		String queryStr = "SELECT hpc FROM HistoricPrecioCompra hpc join hpc.product p join hpc.store s WHERE s.id = :idStore AND p.id = :idProduct Order by hpc.fecha";
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
		String queryStr = "SELECT hpc FROM HistoricPrecioCompra hpc join hpc.store s WHERE s.id = :idStore Order by hpc.fecha";
		Query query = em.createQuery(queryStr, HistoricPrecioCompra.class);
		query.setParameter("idStore", idStore);
		
		List<DataHistoricPrecioCompra> result = new LinkedList<DataHistoricPrecioCompra>();
		for (Object o: query.getResultList()) {
			result.add(new DataHistoricPrecioCompra((HistoricPrecioCompra)o));
		}
		return result;
	}
	
	public List<DataHistoricPrecioVenta> findHistoricPrecioVentaProducto(int idProduct) {
//		String queryStr = "SELECT hpv FROM HistoricPrecioVenta hpv join Product p join Store s WHERE s.id = :idStore AND p.id = :idProduct Order by hpv.fecha";
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
	

	public DataBuyList findBuyList(int idBuyList) {
		BuyList bl = em.find(BuyList.class, idBuyList);
		return new DataBuyList(bl);
	}
	
	public void checkElementBuyList(int idElementBuyList, int idStore, int precio) {
		ElementBuyList elem = em.find(ElementBuyList.class, idElementBuyList);
		elem.setChecked(true);
		em.merge(elem);
		
		Store store = em.find(Store.class, idStore);
		SpecificProduct p = (SpecificProduct) elem.getProduct();
		Stock stk = p.getStock();
		Calendar fechaActual = new GregorianCalendar();
		HistoricStock hs = new HistoricStock(fechaActual, (stk.getCantidad() + elem.getQuantity()), elem.getQuantity(), precio, 1, p, store);
		HistoricPrecioCompra hpc = new HistoricPrecioCompra(fechaActual, precio/elem.getQuantity(), 1, p, store);
		em.persist(hs);
		em.persist(hpc);
		stk.setCantidad((stk.getCantidad() + elem.getQuantity()));
		stk.setPrecioVenta(precio/elem.getQuantity());
		em.merge(stk);
		
		if (stk.getCantidad() > stk.getCantidadMax()) {
			// EVIAR NOTIFICACION A LOS USUARIOS DEL ALMACEN YA QUE PASARON EL STOCK MAXIMO
		}
	}
	public String getCustomizeStore(int id) throws SQLException, IOException{
	/*	String queryStr = " SELECT c FROM Store c" + " WHERE c.id = :id";
>>>>>>> Stashed changes
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
		return ret;*/
		return "aa";
	}
	
	public void setCustomizeStore(int store, byte[] rutaCss) throws SerialException, SQLException{
		Store s= em.find(Store.class, store);
        Customer c= s.getCustomer();
        c.setCss(rutaCss);
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
	
	public List<DataProduct> findGenericsProducts(Integer idCategory) throws CategoryNotExistException {
		List<DataProduct> result = new LinkedList<DataProduct>();
		String queryStr = null;
		if (idCategory == null) {
			queryStr = "SELECT p FROM GenericProduct p Order by p.name";
			Query query = em.createQuery(queryStr, GenericProduct.class);
			for (Object o: query.getResultList()) {
				result.add(new DataProduct((GenericProduct)o));
			}
		} else {
			Category cat = em.find(Category.class, idCategory);
			if (cat == null) {
				throw new CategoryNotExistException("No existe la categoría con identificador " + idCategory);
			}
			queryStr = "SELECT p FROM GenericProduct p Order by p.name";
			Query query = em.createQuery(queryStr, GenericProduct.class);
			for (Object o: query.getResultList()) {
				GenericProduct p = (GenericProduct) o;
				List<Category> cats = obtenerAncestros(p.getCategory());
				if (cats.contains(cat)) {
					result.add(new DataProduct(p));
				}
			}
		}
		return result;
	}
	
	public void createGenericProduct(String name, String description, List<DataProductAdditionalAttribute> additionalAttributes, int idCategory) throws CategoryNotExistException {
		Category cat = em.find(Category.class, idCategory);
		if (cat == null) {
			throw new CategoryNotExistException("No existe la categoría con identificador " + idCategory);
		}
		GenericProduct p = new GenericProduct(name, description);
		p.setCategory(cat);
		for (DataProductAdditionalAttribute dAdAt: additionalAttributes) {
			ProductAdditionalAttribute newAttribute = new ProductAdditionalAttribute(dAdAt.getNameAttribute(), dAdAt.getValueAttribute());
			em.persist(newAttribute);
			p.getAdditionalAttributes().add(newAttribute);
		}
		em.persist(p);
	}
	
	public void createGenericCategory(String name, String description, DataCategory fatherCat) throws ExistCategoryException {
		String queryStr = "SELECT cat FROM Category cat WHERE cat.name = :name";
		Query query = em.createQuery(queryStr, Category.class);
		query.setParameter("name", name);
		if (!query.getResultList().isEmpty()) {
			throw new ExistCategoryException("Ya existe una categoría con nombre " + name);
		}
		if (fatherCat != null) {
			GenericCategory father = em.find(GenericCategory.class, fatherCat.getId());
			GenericCategory cat = new GenericCategory(name, description, father);
			em.persist(cat);
		} else {
			GenericCategory cat = new GenericCategory(name, description, null);
			em.persist(cat);
		}
	}
	
	public void editGenericCategory(String name, String description, DataCategory category) throws ExistCategoryException {
		String queryStr = "SELECT cat FROM Category cat WHERE cat.id <> :idCat AND cat.name = :name";
		Query query = em.createQuery(queryStr, Category.class);
		query.setParameter("name", name);
		query.setParameter("idCat", category.getId());
		if (!query.getResultList().isEmpty()) {
			throw new ExistCategoryException("Ya existe una categoría con nombre " + name);
		}
		Category c = em.find(Category.class, category.getId());
		if (c != null) {
			c.setName(name);
			c.setDescription(description);
			em.merge(c);
		}
	}
	
	public void deleteGenericCategory(DataCategory category) throws NoDeleteCategoryException {
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
	
	public void editGenericProduct(DataProduct product, int idCategory) throws ExistCategoryException {
		String queryStr = "SELECT prod FROM Product prod WHERE prod.id <> :idProd AND prod.name = :name";
		Query query = em.createQuery(queryStr, Product.class);
		query.setParameter("name", product.getName());
		query.setParameter("idProd", product.getId());
		if (!query.getResultList().isEmpty()) {
			throw new ExistCategoryException("Ya existe un producto con nombre " + product.getName());
		}
		Product p = em.find(Product.class, product.getId());
		if (p.getCategory().getId() != idCategory) {
			Category category = em.find(Category.class, idCategory);
			p.setCategory(category);
		}
		p.setName(product.getName());
		p.setDescription(product.getDescription());
		ProductAdditionalAttribute a = null;
		for (DataProductAdditionalAttribute data: product.getAdditionalAttributes()) {
			boolean exist = false;
			for (ProductAdditionalAttribute atr: p.getAdditionalAttributes()) {
				if (atr.getId() == data.getId()) {
					a = atr;
					exist = true;
					break;
				}
			}
			if (exist) {
				a.setNameAttribute(data.getNameAttribute());
				a.setValueAttribute(data.getValueAttribute());
				exist = false;
			} else {
				p.getAdditionalAttributes().add(new ProductAdditionalAttribute(data.getNameAttribute(), data.getValueAttribute()));
			}
		}
		em.merge(p);
	}
	
	public void deleteAttributeProduct(int idProduct, int idAttribute) {
		Product p = em.find(Product.class, idProduct);
		ProductAdditionalAttribute atr = em.find(ProductAdditionalAttribute.class, idAttribute);
		p.getAdditionalAttributes().remove(atr);
		em.remove(atr);
		em.merge(p);
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
	
	public List<DataCategory> findGenericCategories() {
		List<DataCategory> result = new LinkedList<DataCategory>();
		String queryStr = "SELECT c FROM GenericCategory c WHERE c.fatherCategory = null Order by c.name";
		Query query = em.createQuery(queryStr, GenericCategory.class);
		for (Object o: query.getResultList()) {
			result.add(new DataCategory((GenericCategory)o));
		}
		return result;
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

	public List<DataStock> buyRecommendation(int id, DataBuyList db) {
		Store store = em.find(Store.class, id);
		List<DataStock> result = new LinkedList<DataStock>();
		String queryStr = "SELECT p FROM Stock c, Product p WHERE c.store = :store and c.cantidad<5 and c.product=p";
		Query query = em.createQuery(queryStr, Product.class);
		query.setParameter("store", store);
		BuyList buy = em.find(BuyList.class, db.getId());
		for (Object o: query.getResultList()) {
			Product product =(Product)o;
			DataProduct dataP= new DataProduct (product);
			String queryStr1 = "SELECT(h.stock - c.cantidad) FROM Stock c, HistoricStock h WHERE c.store = :store and now()>h.fecha and c.cantidad<5 and c.product= :product and h.product= :product and h.store=c.store and h.tipo=1 and h.fecha > all(SELECT j.fecha FROM HistoricStock j where j.store=h.store and j.product=h.product and j.tipo=1 and h.fecha<>j.fecha)";
			Query query1 = em.createQuery(queryStr1);
			query1.setParameter("store", store);
			query1.setParameter("product", product);
			int i =(int)query1.getSingleResult();
			boolean noAdd=false;
			for (ElementBuyList eb: buy.getElements()){
					if (eb.getProduct()==product){
						noAdd=true;
					}
			}
			DataStock ds = new DataStock(i, dataP);
			if (!noAdd){
				result.add(ds);
			}
			
		}
		return result;
	}

}
