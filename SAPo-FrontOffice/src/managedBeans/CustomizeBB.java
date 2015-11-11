package managedBeans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.sql.rowset.serial.SerialException;

import org.primefaces.model.UploadedFile;

import comunication.Comunicacion;

import datatypes.DataStore;


@ManagedBean
@ViewScoped
public class CustomizeBB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private UploadedFile file;
	private DataStore store;
	
	public CustomizeBB() {
		// TODO Auto-generated constructor stub
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
		this.file=null;
	}
	
	public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}
    
    
     
    public void upload() throws SerialException, SQLException, NamingException, IOException {
    	
            if(this.file != null) {
            	byte[] input = this.file.getContents();
            	Comunicacion.getInstance().getIStoreController().setCustomizeStore(store.getId(), input);
                FacesMessage message = new FacesMessage("Customizaci�n cargada con existo.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
    		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
    		configurableNavigationHandler.performNavigation("/pages/StoreDetail.xhtml?faces-redirect=true");
     
    	
    /*    if(this.file != null) {
        	String path = CustomizeBB.class.getProtectionDomain().getCodeSource().getLocation().toString();
        	int end = path.lastIndexOf("/");
        	path = path.substring(1, end );
        	end = path.lastIndexOf("/");
        	path=path.substring(4, end );
        	path = path.concat("/resources/styles/");
        	System.out.println(path);
        	File f = new File(path+store.getId()+".css");
        	FileOutputStream fop = new FileOutputStream(f);

			// if file doesnt exists, then create it
			if (!f.exists()) {
				f.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = file.getContents();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();

            FacesMessage message = new FacesMessage("Customizaci�n cargada con existo.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		configurableNavigationHandler.performNavigation("/pages/StoreDetail.xhtml?faces-redirect=true");*/
    }
	
}
