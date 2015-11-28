package interfaces;

import java.util.List;

import javax.ejb.Local;

import datatypes.DataBuyList;
import datatypes.DataElementBuyList;
import datatypes.DataStock;
import exceptions.ProductNotExistException;

@Local
public interface IBuyListController {
	
	public List<DataBuyList> findBuyListsStore(int idStore);
	
	public void editElementBuyList(DataElementBuyList element);
	
	public void createBuyListStore(int idStore, List<DataStock> listProducts, String name, String description) throws ProductNotExistException;
	
	public void deleteBuyListsStore(int idBuyList, int idStore);
	
	public void editBuyListStore(int idStore, List<DataElementBuyList> listProducts, String name, String description, DataBuyList dataBuyList) throws ProductNotExistException;

	public DataBuyList findBuyList(int idBuyList);
	
	public void checkElementBuyList(int idElementBuyList, int idStore, int precio, int idUser);
	
	public List<DataStock> buyRecommendation(int id);
	
	public void addBuyListStore(int idStore, List<DataElementBuyList> listProducts, String name, String description, DataBuyList dataBuyList) throws ProductNotExistException;
	
	public DataElementBuyList getBuyListElement(int idBuyList, int idElementBuyLits);
}
