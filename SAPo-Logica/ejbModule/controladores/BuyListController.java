package controladores;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import interfaces.IBuyListController;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DataBuyList;
import datatypes.DataElementBuyList;
import datatypes.DataProduct;
import datatypes.DataStock;
import entities.BuyList;
import entities.ElementBuyList;
import entities.HistoricPrecioCompra;
import entities.HistoricStock;
import entities.Product;
import entities.SpecificProduct;
import entities.Stock;
import entities.Store;
import exceptions.ProductNotExistException;

@Stateless
public class BuyListController implements IBuyListController {
	
	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;
	
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
	
	public void addBuyListStore(int idStore, List<DataElementBuyList> listProducts, String name, String description, DataBuyList dataBuyList) throws ProductNotExistException {
		BuyList buyList = em.find(BuyList.class, dataBuyList.getId());
		for (DataElementBuyList elemNew: listProducts) {
			if (elemNew.getQuantity()>0){
				Product p = em.find(Product.class, elemNew.getProduct().getId());
				ElementBuyList de = new ElementBuyList(elemNew.getQuantity(), p );
				buyList.getElements().add(de);
			}
		}
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
