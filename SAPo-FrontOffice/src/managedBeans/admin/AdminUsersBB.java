package managedBeans.admin;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import managedBeans.SessionBB;
import comunication.Comunicacion;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class AdminUsersBB {
	
	private DataUser userSelected;
	private List<DataUser> users = new LinkedList<DataUser>();
	private List<DataUser> usersFiltered = new LinkedList<DataUser>();
	private String textFilter = "";
	
	public AdminUsersBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
		try {
			this.users = Comunicacion.getInstance().getIStoreController().findUsers();
			this.usersFiltered =this.users; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void filtrar() {
		this.usersFiltered = new LinkedList<DataUser>();
		for (DataUser du: this.users) {
			String name = du.getName();
			String email = du.getEmail();
			String fbId = du.getFbId();
			String t = this.textFilter;
			if (name.toLowerCase().contains(t.toLowerCase())) {
				this.usersFiltered.add(du);
			} else {
				if (email != null) {
					if (email.toLowerCase().contains(t.toLowerCase())) {
						this.usersFiltered.add(du);
					}
				} else {
					if (fbId.toLowerCase().contains(t.toLowerCase())) {
						this.usersFiltered.add(du);
					}
				}
			}
		}
	}
	
	public String showUser() {
		String ret = "/pages/AdminShowUser.xhtml?faces-redirect=true";
    	if(this.userSelected != null) {
    		FacesContext context = FacesContext.getCurrentInstance();
    		ELContext contextoEL = context.getELContext( );
    		Application apli  = context.getApplication( );	
    		ExpressionFactory ef = apli.getExpressionFactory( );
    		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
    		SessionBB session = (SessionBB) ve.getValue(contextoEL);
    		session.setUserSelected(this.userSelected);
        } else {
        	ret = "";
        }
		return ret;
	}
	
	public void filtered(AjaxBehaviorEvent event) {
		filtrar();
	}
	
	public String showReport() {
		return "/pages/ReportsPage.xhtml?faces-redirect=true";
	}

	public DataUser getUserSelected() {
		return userSelected;
	}

	public void setUserSelected(DataUser userSelected) {
		this.userSelected = userSelected;
	}

	public List<DataUser> getUsers() {
		return users;
	}

	public void setUsers(List<DataUser> users) {
		this.users = users;
	}

	public List<DataUser> getUsersFiltered() {
		return usersFiltered;
	}

	public void setUsersFiltered(List<DataUser> usersFiltered) {
		this.usersFiltered = usersFiltered;
	}

	public String getTextFilter() {
		return textFilter;
	}

	public void setTextFilter(String textFilter) {
		this.textFilter = textFilter;
	}
	
}
