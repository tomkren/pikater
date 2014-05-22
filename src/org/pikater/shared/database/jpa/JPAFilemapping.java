package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="FileMapping")
@NamedQueries({
	@NamedQuery(name="FileMapping.getAll",query="select fm from JPAFilemapping fm"),
	@NamedQuery(name="FileMapping.getByID",query="select fm from JPAFilemapping fm where fm.id=:id"),
	@NamedQuery(name="FileMapping.getByInternalFileName",query="select fm from JPAFilemapping fm where fm.internalfilename=:internalFilename"),
	@NamedQuery(name="FileMapping.getByExternalFileName",query="select fm from JPAFilemapping fm where fm.externalfilename=:externalFilename"),
	@NamedQuery(name="FileMapping.getByUser",query="select fm from JPAFilemapping fm where fm.user=:user"),
	@NamedQuery(name="FileMapping.getByUserAndInternalFileName",query="select fm from JPAFilemapping fm where fm.user=:user and fm.internalfilename=:internalFilename"),
	@NamedQuery(name="FileMapping.getByUserAndExternalFileName",query="select fm from JPAFilemapping fm where fm.user=:user and fm.externalfilename=:externalFilename")
})
public class JPAFilemapping extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	private JPAUser user;
	private String externalfilename;
	private String internalfilename;
	
    public int getId() {
		return id;
	}  
	public JPAUser getUser() {
		return user;
	}
	public void setUser(JPAUser user) {
		this.user = user;
	}
	public String getExternalfilename() {
		return externalfilename;
	}
	public void setExternalfilename(String externalfilename) {
		this.externalfilename = externalfilename;
	}
	public String getInternalfilename() {
		return internalfilename;
	}
	public void setInternalfilename(String internalfilename) {
		this.internalfilename = internalfilename;
	}
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPAFilemapping updatedValues=(JPAFilemapping)newValues;
		this.externalfilename=updatedValues.getExternalfilename();
		this.internalfilename=updatedValues.getInternalfilename();
		this.user=updatedValues.getUser();
	}
}


