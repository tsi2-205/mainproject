package managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import comunication.Comunicacion;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class ShareStoreBB {
	
	private DataStore store;
	
	private List<DataUser> users;
	
	private List<DataUser> selectedUsers;
	
	public ShareStoreBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		this.store = session.getStoreSelected();
		try {
			this.users = Comunicacion.getInstance().getIStoreController().getShareUsersFromStore(this.store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public List<DataUser> getUsers() {
		return users;
	}

	public void setUsers(List<DataUser> users) {
		this.users = users;
	}

	public List<DataUser> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<DataUser> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
}
