package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "BuyList")
public class BuyList implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    private String name;
	
    private String description;
	
	@Version
    private int version;
    
    @ManyToOne
    @JoinColumn(name="IdStore")
    private Store store;
    
    @OneToMany
    @JoinColumn (name = "idBuyList", referencedColumnName="id")
    private List<ElementBuyList> elements = new LinkedList<ElementBuyList>();
    
    
    public BuyList() {
		super();
	}

	public BuyList(String name, String description, Store store,
			List<ElementBuyList> elements) {
		super();
		this.name = name;
		this.description = description;
		this.store = store;
		this.elements = elements;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public List<ElementBuyList> getElements() {
		return elements;
	}

	public void setElements(List<ElementBuyList> elements) {
		this.elements = elements;
	}

}
