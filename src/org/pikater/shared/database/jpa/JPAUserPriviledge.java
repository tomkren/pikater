package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
	private String name;
	
	public int getId() {
        return id;
    }	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getEntityName() {
		return "UserPriviledge";
	}
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPAUserPriviledge updateValues=(JPAUserPriviledge)newValues;
		this.name=updateValues.getName();
	}
	

}
