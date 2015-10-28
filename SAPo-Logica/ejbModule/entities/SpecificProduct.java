package entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "specificproduct")
public class SpecificProduct extends Product {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
    @JoinColumn(name = "idStore", referencedColumnName = "id", nullable = false)
    private Store store;

	public SpecificProduct() {
		super();
	}

	public SpecificProduct(String name, String description, Store store) {
		super(name, description);
		this.store = store;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
	
}
