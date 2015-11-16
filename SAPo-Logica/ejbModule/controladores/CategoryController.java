package controladores;

import java.util.LinkedList;
import java.util.List;

import interfaces.ICategoryController;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DataCategory;
import datatypes.DataStore;
import entities.Category;
import entities.GenericCategory;
import entities.SpecificCategory;
import entities.Store;
import exceptions.ExistCategoryException;
import exceptions.ExistStoreException;
import exceptions.NoDeleteCategoryException;

@Stateless
public class CategoryController implements ICategoryController {
	
	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;
	
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
	
	public List<DataCategory> findGenericCategories() {
		List<DataCategory> result = new LinkedList<DataCategory>();
		String queryStr = "SELECT c FROM GenericCategory c WHERE c.fatherCategory = null Order by c.name";
		Query query = em.createQuery(queryStr, GenericCategory.class);
		for (Object o: query.getResultList()) {
			result.add(new DataCategory((GenericCategory)o));
		}
		return result;
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

}
