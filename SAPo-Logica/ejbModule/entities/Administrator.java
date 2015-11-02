package entities;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "administrator")
public class Administrator extends User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy="administrator")
    private List<GenericCategory> GenericCategories = new LinkedList<GenericCategory>();
    
    @OneToMany(mappedBy="administrator")
    private List<GenericProduct> genericProducts = new LinkedList<GenericProduct>();

	public Administrator() {
		super();
	}

	public Administrator(String nick, String password, String mail,
			String nombre) {
		super(nick, password, mail, nombre, null);
	}

	public List<GenericCategory> getGenericCategories() {
		return GenericCategories;
	}

	public void setGenericCategories(List<GenericCategory> genericCategories) {
		GenericCategories = genericCategories;
	}

	public List<GenericProduct> getGenericProducts() {
		return genericProducts;
	}

	public void setGenericProducts(List<GenericProduct> genericProducts) {
		this.genericProducts = genericProducts;
	}

}
