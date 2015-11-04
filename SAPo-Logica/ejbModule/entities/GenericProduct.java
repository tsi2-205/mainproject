package entities;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "genericproduct")
public class GenericProduct extends Product {
	
	private static final long serialVersionUID = 1L;
//	@ManyToOne
//    @JoinColumn(name="IdAdministrator")
//    private Administrator administrator;
	
//	@ManyToMany
//	@JoinTable (name = "strore_genericproduct", joinColumns = @JoinColumn(name = "idGenericProduct"), inverseJoinColumns = @JoinColumn(name = "idStore"))
//    private List<Store> stores = new LinkedList<Store>();

	public GenericProduct() {
		super();
	}

	public GenericProduct(String name, String description) {
		super(name, description);
	}

//	public Administrator getAdministrator() {
//		return administrator;
//	}
//
//	public void setAdministrator(Administrator administrator) {
//		this.administrator = administrator;
//	}
//
//	public List<Store> getStores() {
//		return stores;
//	}
//
//	public void setStores(List<Store> stores) {
//		this.stores = stores;
//	}
	
}
