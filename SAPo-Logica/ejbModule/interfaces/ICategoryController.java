package interfaces;

import java.util.List;

import javax.ejb.Local;

import datatypes.DataCategory;
import datatypes.DataStore;
import exceptions.ExistCategoryException;
import exceptions.ExistStoreException;
import exceptions.NoDeleteCategoryException;

@Local
public interface ICategoryController {

	public void createSpecificCategory(String name, String description,
			DataStore store, DataCategory fatherCat) throws ExistStoreException;

	public void editSpecificCategory(String name, String description,
			DataCategory category) throws ExistStoreException;

	public void deleteSpecificCategory(DataCategory category)
			throws NoDeleteCategoryException;
	
	public List<DataCategory> findSpecificCategoriesStore(int idStore);
	
	public List<DataCategory> findGenericCategories();
	
	public void createGenericCategory(String name, String description, DataCategory fatherCat) throws ExistCategoryException;
	
	public void editGenericCategory(String name, String description, DataCategory category) throws ExistCategoryException;
	
	public void deleteGenericCategory(DataCategory category) throws NoDeleteCategoryException;

}
