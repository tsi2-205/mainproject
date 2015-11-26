package managedBeans.admin;

import java.io.Serializable;
import java.util.Calendar;
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

import managedBeans.SessionBB;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import comunication.Comunicacion;

import datatypes.DataStore;

@ManagedBean
@ViewScoped
public class ChartValorizationStoreBB implements Serializable {

	private DataStore store;
	private LineChartModel chartVal;
	private List<Integer> valores = new LinkedList<Integer>();

	public ChartValorizationStoreBB() {
		super();
	}

	@PostConstruct
	private void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext();
		Application apli = context.getApplication();
		ExpressionFactory ef = apli.getExpressionFactory();
		ValueExpression ve = ef.createValueExpression(contextoEL,
				"#{sessionBB}", SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		this.store = session.getStoreSelected();
		try {
			this.valores = Comunicacion.getInstance().getIStoreController().getValorizationHistoricStore(this.store.getId());
			createLineModels();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createLineModels() {
		
		chartVal = initCategoryModel();
//		chartVal.setTitle("Category Chart");
		chartVal.setLegendPosition("e");
		chartVal.setShowPointLabels(true);
		chartVal.getAxes().put(AxisType.X, new CategoryAxis("Mes"));
		Axis yAxis = chartVal.getAxis(AxisType.Y);
		yAxis.setLabel("Valor en $");
		yAxis.setMin(0);
//		yAxis.setMax(200);
	}

	private LineChartModel initCategoryModel() {
		LineChartModel model = new LineChartModel();
		ChartSeries val = new ChartSeries();
		val.setLabel("$");
		Calendar fechaActual = new GregorianCalendar();
		int year = fechaActual.get(Calendar.YEAR);
		int month = fechaActual.get(Calendar.MONTH);
		Calendar fecha = new GregorianCalendar(year, month, 1);
		for (int i = 11; i >= 1; i--) {
			Calendar fechaAux = (Calendar) fecha.clone();
			fechaAux.add(Calendar.MONTH, (-1*i));
			year = fechaAux.get(Calendar.YEAR);
			month = fechaAux.get(Calendar.MONTH);
			val.set((month + 1) + "/" + year, valores.get(i));
		}
		val.set("Hoy", valores.get(0));
		model.addSeries(val);

		return model;
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public LineChartModel getChartVal() {
		return chartVal;
	}

	public void setChartVal(LineChartModel chartVal) {
		this.chartVal = chartVal;
	}
	
	

}
