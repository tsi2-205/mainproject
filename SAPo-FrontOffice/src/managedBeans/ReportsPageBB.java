package managedBeans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.NamingException;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import comunication.Comunicacion;
import datatypes.DataUserLogged;


@ManagedBean
@ViewScoped
public class ReportsPageBB implements Serializable {
	
	private static final long serialVersionUID = 1L; 
	private LineChartModel zoomModel;
	 
	 @PostConstruct
	 public void init() {
	        createZoomModel();
	 }
	 
	 public ReportsPageBB() {
			// TODO Auto-generated constructor stub
		}
	 
	 public LineChartModel getZoomModel() {
	        return zoomModel;
	    }
	 
	 private void createZoomModel() {
	        zoomModel = initLinearModel();
	        zoomModel.setTitle("Uusarios logueados por fecha");
	        zoomModel.setZoom(true);
	        zoomModel.setLegendPosition("e");
	       // Axis yAxis = zoomModel.getAxis(AxisType.Y);
	       // yAxis.setMin(0);
	       // yAxis.setMax(100);
	 }
	     
	    private LineChartModel initLinearModel() {
	        LineChartModel model = new LineChartModel();
	 
	        LineChartSeries series1 = new LineChartSeries();
	        series1.setLabel("Usuarios logueados");
	        
	        try {
				List<DataUserLogged> userLog = Comunicacion.getInstance().getIUserController().getLoggedUser();
				for (Object o : userLog) {
					DataUserLogged u = (DataUserLogged)o;
					series1.set(u.getFecha(), u.getCount());
				}
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	       
	        model.addSeries(series1);
	       
	        return model;
	    }

		public void setZoomModel(LineChartModel zoomModel) {
			this.zoomModel = zoomModel;
		}
	
	    
	

}
