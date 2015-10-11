package managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import comunication.Comunicacion;

@ManagedBean
public class MongoBB {

	private String storeName;
	private List<String> AdditionalData;
	
	public MongoBB() {
		super();
	}

	@PostConstruct
	private void init() {
		AdditionalData = new ArrayList<String>();
		
	}
	
	public String getStoreName(){
		return new String ("Almacen lo de cacho");
	}
	
	public void setStoreName(String sn) {
		this.storeName = sn;
	}
	
	public List<String> getAdditionalData(){
		AdditionalData.add("Hello");
		AdditionalData.add("World");
		return this.AdditionalData;
	}
	
	public void setAdditionalData(List<String> ad){
		this.AdditionalData = ad;
	}
	
}
