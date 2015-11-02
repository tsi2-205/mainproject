package managedBeans;

import java.io.File;
import java.io.FileOutputStream;
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
        	System.out.println (CustomizeBB.class.getProtectionDomain().getCodeSource().getLocation());
        	File f = new File("C:/Users/Nacho/Desktop/TSI-Proyectos/SAPo-FrontOffice/WebContent/resources/styles/"+store.getId()+".css");
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

            FacesMessage message = new FacesMessage("Customización cargada con existo.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		configurableNavigationHandler.performNavigation("/pages/StoreDetail.xhtml?faces-redirect=true");
    }
	
}
