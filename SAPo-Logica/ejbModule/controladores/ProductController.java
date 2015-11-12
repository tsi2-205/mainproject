package controladores;

import interfaces.IProductController;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DataProductAdditionalAttribute;
import datatypes.DataStock;
import entities.Category;
import entities.GenericProduct;
import entities.HistoricPrecioCompra;
import entities.HistoricPrecioVenta;
import entities.HistoricStock;
import entities.Product;
import entities.ProductAdditionalAttribute;
import entities.SpecificProduct;
import entities.Stock;
import entities.Store;
import exceptions.ExistCategoryException;

@Stateless
public class ProductController implements IProductController {
	
	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;
	
	public void createSpecificProduct(String name, String description, Integer stockMin, Integer stockMax, int idStore, List<DataProductAdditionalAttribute> additionalAttributes, int idCategory) {
		Store s = em.find(Store.class, idStore);
		Category cat = em.find(Category.class, idCategory);
		SpecificProduct p = new SpecificProduct(name, description, s);
		p.setCategory(cat);
		for (DataProductAdditionalAttribute dAdAt: additionalAttributes) {
			ProductAdditionalAttribute newAttribute = new ProductAdditionalAttribute(dAdAt.getNameAttribute(), dAdAt.getValueAttribute());
			em.persist(newAttribute);
			p.getAdditionalAttributes().add(newAttribute);
		}
		Stock stock = new Stock(0, stockMin, stockMax, s, p);
		em.persist(s);
		em.persist(p);
		em.persist(stock);
		
//		// Si ya existen mas de 5 productos con las mismas caracteristicas en el
//		// sistema se le debe enviar una notificacion a los administradores para
//		// que promuevan dicho producto como generico
//		String queryStr = "SELECT sp FROM SpecificProduct sp WHERE sp.name = :name";
//		Query query = em.createQuery(queryStr, SpecificProduct.class);
//		query.setParameter("name", name);
//		if (query.getResultList().size() > 5) {
//			// Aca hacer la notificacion
//		}
		
	}
	
	public void editProductStore(DataStock stock, int idStore, Integer idCategory) throws ExistCategoryException {
		Store store = em.find(Store.class, idStore);
		Product p = em.find(Product.class, stock.getProduct().getId());
		String queryStr = "SELECT sp FROM Store s join s.specificsProducts sp WHERE sp.name = :name AND sp.id <> :id";
		Query query = em.createQuery(queryStr);
		query.setParameter("name", stock.getProduct().getName());
		query.setParameter("id", p.getId());
		if (!query.getResultList().isEmpty()) {
			throw new ExistCategoryException("Ya existe un producto con nombre " + stock.getProduct().getName());
		}
		
		if (p instanceof GenericProduct) {
			//esta agregando un producto generico
			SpecificProduct sp = new SpecificProduct(stock.getProduct().getName(), stock.getProduct().getDescription(), store);
			if ((idCategory != null) && (p.getCategory().getId() != idCategory)) {
				Category cat = em.find(Category.class, idCategory);
				sp.setCategory(cat);
			}
			for (DataProductAdditionalAttribute dAdAt: stock.getProduct().getAdditionalAttributes()) {
				ProductAdditionalAttribute newAttribute = new ProductAdditionalAttribute(dAdAt.getNameAttribute(), dAdAt.getValueAttribute());
				em.persist(newAttribute);
				sp.getAdditionalAttributes().add(newAttribute);
			}
			Stock stk = new Stock(0, stock.getCantidadMin(), stock.getCantidadMax(), store, sp);
			sp.setGenericProduct((GenericProduct)p);
			em.persist(sp);
			em.persist(stk);
			
		} else {
			//Esta editando un producto especifico
			if ((idCategory != null) && (p.getCategory().getId() != idCategory)) {
				Category category = em.find(Category.class, idCategory);
				p.setCategory(category);
			}
			p.setName(stock.getProduct().getName());
			p.setDescription(stock.getProduct().getDescription());
			ProductAdditionalAttribute a = null;
			List<ProductAdditionalAttribute> listAux = new LinkedList<ProductAdditionalAttribute>();
			for (DataProductAdditionalAttribute data: stock.getProduct().getAdditionalAttributes()) {
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
					listAux.add(new ProductAdditionalAttribute(data.getNameAttribute(), data.getValueAttribute()));
				}
			}
			for (ProductAdditionalAttribute paa: listAux) {
				p.getAdditionalAttributes().add(paa);
			}
			em.merge(p);
			Stock stk = em.find(Stock.class, stock.getId());
			if (stk.getCantidadMax() != stock.getCantidadMax() || stk.getCantidadMin() != stock.getCantidadMin()) {
				stk.setCantidadMax(stock.getCantidadMax());
				stk.setCantidadMin(stock.getCantidadMin());
				em.merge(stk);
			}
		}
		

	}
	
	public int changeStockProduct(int idStore, int idProduct, int movCant, int movPrecio, int tipo) {
		Store store = em.find(Store.class, idStore);
		SpecificProduct p = em.find(SpecificProduct.class, idProduct);
		Stock stk = p.getStock();
		Calendar fechaActual = new GregorianCalendar();
		if (tipo == 0) {
			HistoricStock hs = new HistoricStock(fechaActual, (stk.getCantidad() - movCant), movCant, movPrecio, tipo, p, store);
			HistoricPrecioVenta hpv = new HistoricPrecioVenta(fechaActual, movPrecio/movCant, tipo, p, store);
			em.persist(hs);
			em.persist(hpv);
			stk.setCantidad((stk.getCantidad() - movCant));
			stk.setPrecioVenta(movPrecio/movCant);
			em.merge(stk);
		} else {
			HistoricStock hs = new HistoricStock(fechaActual, (stk.getCantidad() + movCant), movCant, movPrecio, tipo, p, store);
			HistoricPrecioCompra hpc = new HistoricPrecioCompra(fechaActual, movPrecio/movCant, tipo, p, store);
			em.persist(hs);
			em.persist(hpc);
			stk.setCantidad((stk.getCantidad() + movCant));
			stk.setPrecioVenta(movPrecio/movCant);
			em.merge(stk);
		}
		if (stk.getCantidad() < stk.getCantidadMin()) {
			// ENVIAR NOTIFICACION A LOS USUARIOS DEL ALMACEN YA QUE PASARON EL STOCK MINIMO
			return 1;
		}
		if (stk.getCantidad() > stk.getCantidadMax()) {
			// ENVIAR NOTIFICACION A LOS USUARIOS DEL ALMACEN YA QUE PASARON EL STOCK MAXIMO
			return 2;
		}
		return 0;
	}
	
	

}
