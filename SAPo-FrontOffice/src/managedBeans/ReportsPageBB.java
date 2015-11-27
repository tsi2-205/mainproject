package managedBeans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.NamingException;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import comunication.Comunicacion;
import datatypes.DataUserLogged;


@ManagedBean
@ViewScoped
public class ReportsPageBB implements Serializable {
	
	private static final long serialVersionUID = 1L; 
	private LineChartModel dateModel;
	 
    @PostConstruct
    public void init() {
        createDateModel();
    }
 
    public LineChartModel getDateModel() {
        return dateModel;
    }
	 
	 public ReportsPageBB() {
			// TODO Auto-generated constructor stub
		}
	 
	 
	 
	 public void setDateModel(LineChartModel dateModel) {
		this.dateModel = dateModel;
	}

	private void createDateModel() {
	        dateModel = new LineChartModel();
	        LineChartSeries series1 = new LineChartSeries();
	        series1.setLabel("Series 1");
	        String fec=null;
	        try {
				List<DataUserLogged> userLog = Comunicacion.getInstance().getIUserController().getLoggedUser();
				for (Object o : userLog) {
					DataUserLogged u = (DataUserLogged)o;
					fec= u.getFecha().toString();
					series1.set(fec,(Number) u.getCount());
				}
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	 
	        dateModel.addSeries(series1);
	         
	        dateModel.setTitle("Usuarios Logueados por Fecha");
	        dateModel.setZoom(true);
	        dateModel.getAxis(AxisType.Y).setLabel("Cantidad Usarios");
	        DateAxis axis = new DateAxis("Fechas");
	        axis.setTickAngle(-50);
			axis.setMax(fec);
			axis.setTickFormat("%b %#d, %y");
	         
	        dateModel.getAxes().put(AxisType.X, axis);
	    }
	     
	
	    
	

}
