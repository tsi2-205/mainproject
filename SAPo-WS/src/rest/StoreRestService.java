package rest;

import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import response.GetBuyListsResponse;

import comunication.Comunicacion;

import datatypes.DataBuyList;

@Path("/store")
public class StoreRestService {
	
	@GET
	@Path("/{storeId}/buyLists")
	@Produces("application/json")
	public GetBuyListsResponse getBuyLists(@PathParam("storeId") int storeId) throws NamingException {
		List<DataBuyList> buyLists = Comunicacion.getInstance().getIBuyListController().findBuyListsStore(storeId);
		GetBuyListsResponse response = new GetBuyListsResponse(0,"",buyLists);
		return response;
	}

}
