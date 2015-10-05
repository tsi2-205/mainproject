package entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SpecificCategory")
public class SpecificCategory extends Category {

	private static final long serialVersionUID = 1L;

	public SpecificCategory() {
		super();
	}

	public SpecificCategory(String name, String description, int version) {
		super(name, description, version);
	}
	
}
