package managedBeans;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.naming.NamingException;

import comunication.Comunicacion;
import datatypes.DataHistoricPrecioCompra;
import datatypes.DataHistoricPrecioVenta;
import datatypes.DataHistoricStock;
import datatypes.DataProduct;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class ProductMovesBB {
	
	private DataStore store;
	
	private DataProduct product = null;
	
	private Date fechaIni = null;
	
	private Date fechaFin = null;
	
	private List<DataHistoricStock> histStock = new LinkedList<DataHistoricStock>();
	
	private List<DataHistoricStock> filteredHistStock = new LinkedList<DataHistoricStock>();
	
	public ProductMovesBB() {
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
		this.product = session.getProductSelected();
		this.fechaFin = new Date();
		Calendar fechaAux = new GregorianCalendar();
		fechaAux.add(Calendar.MONTH, -1);
		this.fechaIni = fechaAux.getTime();
		
		try {
			Calendar fIni = new GregorianCalendar();
			Calendar fFin = new GregorianCalendar();
			
			fIni.setTime(fechaIni);
			fFin.setTime(fechaFin);
			this.histStock = Comunicacion.getInstance().getIStoreController().findHistoricStockProductDate(store.getId(), product.getId(), fIni, fFin);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	public String volver() {
		return "/pages/StoreMoves.xhtml?faces-redirect=true";
	}
	
	public void showPanel() {
		if (this.fechaIni != null && this.fechaFin != null) {
			try {
				Calendar fIni = new GregorianCalendar();
				Calendar fFin = new GregorianCalendar();
				fIni.setTime(fechaIni);
				fFin.setTime(fechaFin);
				fIni.add(Calendar.DAY_OF_MONTH, 1);
				fFin.add(Calendar.DAY_OF_MONTH, 1);
				this.histStock = Comunicacion.getInstance().getIStoreController().findHistoricStockProductDate(store.getId(), product.getId(), fIni, fFin);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public DataProduct getProduct() {
		return product;
	}

	public void setProduct(DataProduct product) {
		this.product = product;
	}

	public List<DataHistoricStock> getHistStock() {
		return histStock;
	}

	public void setHistStock(List<DataHistoricStock> histStock) {
		this.histStock = histStock;
	}

	public List<DataHistoricStock> getFilteredHistStock() {
		return filteredHistStock;
	}

	public void setFilteredHistStock(List<DataHistoricStock> filteredHistStock) {
		this.filteredHistStock = filteredHistStock;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

}
