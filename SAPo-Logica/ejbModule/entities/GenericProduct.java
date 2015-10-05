package entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GenericProduct")
public class GenericProduct extends Product {
	
	private static final long serialVersionUID = 1L;
	@ManyToOne
    @JoinColumn(name="IdAdministrator")
    private Administrator administrator;

	public GenericProduct() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Administrator getAdministrator() {
		return administrator;
	}

	public void setAdministrator(Administrator administrator) {
		this.administrator = administrator;
	}
	
	

}
