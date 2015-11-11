package utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import datatypes.DataProduct;
 
@FacesConverter("dataProductConverter")
public class DataProductConverter implements Converter {

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
    	DataProduct prod = new DataProduct(-1, "Un nombre", "Ninguna", false, null);
    	return prod;
        
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        return ((DataProduct)object).toString();
    }

	
}  