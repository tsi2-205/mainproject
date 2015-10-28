package entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class Comment implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    private String text;
	
	@Version
    private int version;
    
    @ManyToOne
    @JoinColumn(name="IdStore")
    private Store store;
    
    @ManyToOne
    @JoinColumn(name="IdRegistered")
    private Registered registered;

	public Comment() {
		super();
	}

	public Comment(int id, String text, Store store, Registered registered) {
		super();
		this.id = id;
		this.text = text;
		this.store = store;
		this.registered = registered;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Registered getRegistered() {
		return registered;
	}

	public void setRegistered(Registered registered) {
		this.registered = registered;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
}
