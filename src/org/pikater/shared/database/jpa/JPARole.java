package org.pikater.shared.database.jpa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Role_20140430")
@NamedQueries({
	@NamedQuery(name="Role.getAll",query="select r from JPARole r"),
	@NamedQuery(name="Role.getByID",query="select r from JPARole r where r.id=:id"),
	@NamedQuery(name="Role.getByName",query="select r from JPARole r where r.name=:name")
})
public class JPARole extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int getId() {
        return id;
    }
	@OneToMany
	private final List<JPAUser> usersWithThisRole=new LinkedList<JPAUser>();
	@Column(unique=true)
	private String name;
	private String description;
	@OneToMany
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
	public List<JPAUser> getUsersWithThisRole() {
		return usersWithThisRole;
	}
	
	public void addPriviledge(JPAUserPriviledge priviledge) {
		this.priviledges.add(priviledge);
		
	}
	@Override
	public String getEntityName() {
		return "Role";
	}
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPARole updatedValues=(JPARole)newValues;
		this.description=updatedValues.getDescription();
		this.name=updatedValues.getName();
	}
}
