package entities;
import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Administrator")
public class Administrator extends User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy="administrator")
    private List<GenericCategory> GenericCategories;
    
    @OneToMany(mappedBy="administrator")
    private List<GenericProduct> genericProducts;

	public Administrator() {
		super();
	}

	public Administrator(String nick, String password, String mail,
			String nombre) {
		super(nick, password, mail, nombre);
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
