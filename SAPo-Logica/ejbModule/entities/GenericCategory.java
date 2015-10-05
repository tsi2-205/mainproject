package entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GenericCategory")
public class GenericCategory extends Category {
	
	private static final long serialVersionUID = 1L;
	@ManyToOne
    @JoinColumn(name="IdAdministrator")
    private Administrator administrator;

	public GenericCategory() {
		super();
	}

	public GenericCategory(String name, String description, int version) {
		super(name, description, version);
	}

	public Administrator getAdministrator() {
		return administrator;
	}

	public void setAdministrator(Administrator administrator) {
		this.administrator = administrator;
	}
	
}
