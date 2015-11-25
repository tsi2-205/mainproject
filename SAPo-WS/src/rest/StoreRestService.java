package rest;

import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import response.GetBuyListsResponse;
import response.SaveBuyListResponse;
import request.SaveBuyListRequest;
import comunication.Comunicacion;
import datatypes.DataBuyList;
import datatypes.DataElementBuyList;

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
	
	@POST
	@Path("/buyList")
	@Consumes("application/json")
	@Produces("application/json")
	public SaveBuyListResponse saveBuyList(SaveBuyListRequest request) throws NamingException {
		for (DataElementBuyList e: request.getBuyList().getElements()) {
			if (e.isChecked()) {
				DataElementBuyList auxElement = Comunicacion.getInstance().getIBuyListController().getBuyListElement(request.getBuyList().getId(), e.getId());
				if (!auxElement.isChecked()) {
					Comunicacion.getInstance().getIBuyListController().checkElementBuyList(e.getId(), request.getStoreId(), e.getPrice());
				}
			}
		}
		SaveBuyListResponse response = new SaveBuyListResponse(0,"");
		return response;
	}

}
