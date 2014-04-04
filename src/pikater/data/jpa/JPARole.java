package pikater.data.jpa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class JPARole {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@OneToMany(mappedBy="role")
	private final List<JPAUser> usersWithThisRole=new LinkedList<JPAUser>();
	@Column(unique=true)
	private String name;
	private String description;
	
	private final List<JPAUserPriviledge> priviledges = new ArrayList<JPAUserPriviledge>(); 
	
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
	public int getId() {
		return id;
	}
	public List<JPAUser> getUsersWithThisRole() {
		return usersWithThisRole;
	}
	
	public void addPriviledge(JPAUserPriviledge priviledge) {
		this.priviledges.add(priviledge);
		
	}
}
