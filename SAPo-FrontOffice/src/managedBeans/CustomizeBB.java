package managedBeans;


import java.io.InputStream;
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

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import comunication.Comunicacion;
import datatypes.DataStore;


@ManagedBean
@ViewScoped
public class CustomizeBB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private UploadedFile file;
	private DataStore store;
	private StreamedContent fileDown;
	
	
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
		InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/styles/layout2.css");
		this.fileDown = new DefaultStreamedContent(stream,"text/css", "descargaCSS.css");
		System.out.print(fileDown.getName());
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
	
	public StreamedContent getFileDown() {
		return fileDown;
	}

	public void setFileDown(StreamedContent fileDown) {
		this.fileDown = fileDown;
	}

	public void upload() throws SerialException, SQLException, NamingException {
        if(this.file != null) {
        	byte[] input = this.file.getContents();
        	Comunicacion.getInstance().getIStoreController().setCustomizeStore(store.getId(), input);
            FacesMessage message = new FacesMessage("Customización cargada con existo.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		configurableNavigationHandler.performNavigation("/pages/StoreMoves.xhtml?faces-redirect=true");
    }
	
	
     
   /* public void upload() throws SerialException, SQLException, NamingException, IOException {
    	
            if(this.file != null) {
            	InputStream in = file.getInputstream();
            	OutputStream out = new FileOutputStream(store.getFile());
            	
            	int read=0;
            	byte[] bytes=new byte[1024];
            	
            	while ((read = in.read(bytes))!= -1){
            		out.write(bytes, 0, read);
            	}
            	in.close();
            	out.flush();
            	out.close();
            	Comunicacion.getInstance().getIStoreController().setCustomizeStore(store.getId(), store.getFile());
                FacesMessage message = new FacesMessage("Customización cargada con existo.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
    		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
    		configurableNavigationHandler.performNavigation("/pages/StoreDetail.xhtml?faces-redirect=true");
     */
    	
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

            FacesMessage message = new FacesMessage("Customización cargada con existo.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		configurableNavigationHandler.performNavigation("/pages/StoreDetail.xhtml?faces-redirect=true");*/
    //}
	
}
