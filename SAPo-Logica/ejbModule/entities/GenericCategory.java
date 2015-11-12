package entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "genericcategory")
public class GenericCategory extends Category {
	
	private static final long serialVersionUID = 1L;
//	@ManyToOne
//    @JoinColumn(name="IdAdministrator")
//    private Administrator administrator;

	public GenericCategory() {
		super();
	}

	public GenericCategory(String name, String description, Category father) {
		super(name, description, father, null);
	}

//	public Administrator getAdministrator() {
//		return administrator;
//	}
//
//	public void setAdministrator(Administrator administrator) {
//		this.administrator = administrator;
//	}
	
}
