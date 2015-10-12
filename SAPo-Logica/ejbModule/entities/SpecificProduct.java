package entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SpecificProduct")
public class SpecificProduct extends Product {

	private static final long serialVersionUID = 1L;

	public SpecificProduct() {
		super();
	}

	public SpecificProduct(String name, String description) {
		super(name, description);
	}
	
	
	
}
