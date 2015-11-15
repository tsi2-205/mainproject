package controladores;

import interfaces.IProductController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DataProduct;
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
import exceptions.CategoryNotExistException;
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
			
			List<ProductAdditionalAttribute> listAux = new LinkedList<ProductAdditionalAttribute>();
			for (ProductAdditionalAttribute atr: p.getAdditionalAttributes()) {
				boolean exist = false;
				for (DataProductAdditionalAttribute data: stock.getProduct().getAdditionalAttributes()) {
					if (atr.getId() == data.getId()) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					listAux.add(atr);
				}
			}
			p.getAdditionalAttributes().removeAll(listAux);
			
			ProductAdditionalAttribute a = null;
			listAux = new LinkedList<ProductAdditionalAttribute>();
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
	
	
	public void guardarImagenProducto(InputStream in) {
    	try {
            // write the inputStream to a FileOutputStream
    		File f = new File("C:\\Users\\Fernando\\Desktop\\Imagenes\\nombreTemporal");
    		OutputStream out = new FileOutputStream(f);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public File asociarImagen(String nombreProducto) {
    	File imagen = new File("C:\\Users\\Fernando\\Desktop\\Imagenes\\nombreTemporal");
    	File file = new File("C:\\Users\\Fernando\\Desktop\\Imagenes\\" + nombreProducto);
    	imagen.renameTo(file);
    	return file;
    }
    
    public File obtenerImagen(int codProduct) {
    	File imagen = null;
    	Product p = em.find(Product.class, codProduct);
    	if (p != null) {
    		imagen = new File("images/" + p.getName());
    	}
    	return imagen;
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
	

}
