package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
    private String description;
	
	@Version
    private int version;
	
	@ManyToOne
	@JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "idProduct"), inverseJoinColumns = @JoinColumn(name = "idCategory"))
    private Category category;
    
    @OneToMany
    @JoinColumn (name = "idProduct", referencedColumnName="id")
    private List<ProductAdditionalAttribute> additionalAttributes = new LinkedList<ProductAdditionalAttribute>();

    
	public Product() {
		super();
	}
	
	public Product(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ProductAdditionalAttribute> getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void setAdditionalAttributes(
			List<ProductAdditionalAttribute> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}
	
}
