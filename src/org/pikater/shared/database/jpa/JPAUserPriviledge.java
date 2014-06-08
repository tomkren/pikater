package org.pikater.shared.database.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.pikater.shared.database.jpa.security.PikaterPriviledge;

@Entity
@Table(name="UserPriviledge")
@NamedQueries({
	@NamedQuery(name="UserPriviledge.getAll",query="select up from JPAUserPriviledge up"),
	@NamedQuery(name="UserPriviledge.getByID",query="select up from JPAUserPriviledge up where up.id=:id"),
	@NamedQuery(name="UserPriviledge.getByName",query="select up from JPAUserPriviledge up where up.name=:name")
})
public class JPAUserPriviledge extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@Column(unique=true)
	@Enumerated(EnumType.STRING)
	private PikaterPriviledge priviledge;
	@Column(unique=true)
	private String name;
	
	protected JPAUserPriviledge() {}
	
	public JPAUserPriviledge(String name,PikaterPriviledge priviledge){
		this.name=name;
		this.priviledge=priviledge;
	}
	
	public int getId() {
        return id;
    }	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PikaterPriviledge getPriviledge() {
		return priviledge;
	}
	public void setPriviledge(PikaterPriviledge priviledge) {
		this.priviledge = priviledge;
	}

	@Transient
	public static final String EntityName = "UserPriviledge";

	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPAUserPriviledge updateValues=(JPAUserPriviledge)newValues;
		this.name=updateValues.getName();
	}
	

}
