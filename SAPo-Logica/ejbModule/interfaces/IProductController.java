package interfaces;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.ejb.Local;

import datatypes.DataProduct;
import datatypes.DataProductAdditionalAttribute;
import datatypes.DataStock;
import exceptions.CategoryNotExistException;
import exceptions.ExistCategoryException;

@Local
public interface IProductController {
	
	public void createSpecificProduct(String name, String description, Integer stockMin, Integer stockMax, int idStore, List<DataProductAdditionalAttribute> additionalAttributes, int idCategory, String imagenProducto);
	
	public void editProductStore(DataStock stock, int idStore, Integer idCategory, String laImagen) throws ExistCategoryException;
	
	public int changeStockProduct(int idStore, int idProduct, int idUser, int movCant, int movPrecio, int tipo);
	
//	public void editProductBasic(DataStock stock, int idStore);
	
	public List<DataProduct> findGenericsProducts(Integer idCategory) throws CategoryNotExistException;
	
	public void createGenericProduct(String name, String description, List<DataProductAdditionalAttribute> additionalAttributes, int idCategory) throws CategoryNotExistException;
	
	public List<DataStock> findStockProductsStore(int idStore, Integer idCategory);
	
	public List<DataProduct> findProductsStore(int idStore, Integer idCategory);
	
	public void editGenericProduct(DataProduct product, int idCategory) throws ExistCategoryException;
	
	public void deleteAttributeProduct(int idProduct, int idAttribute);
	
	public void guardarImagenProducto(InputStream in);
	
	public File asociarImagen(String nombreProducto);
	
    public File obtenerImagen(int codProduct);
    
    public boolean shouldPromoteProduct(String name);

}
