package entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Chat")
public class Chat implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Version
    private int version;
    
    @OneToMany
    @JoinColumn(name="idChat", referencedColumnName="id")
    private List<Message> messages;
    
    @ManyToMany
    @JoinTable (name = "Chat_Registered", joinColumns = @JoinColumn(name = "idChat"), inverseJoinColumns = @JoinColumn(name = "idRegistered"))
    private List<Registered> participants;

	public Chat() {
		super();
		// TODO Auto-generated constructor stub
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

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<Registered> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Registered> participants) {
		this.participants = participants;
	}
    
}
