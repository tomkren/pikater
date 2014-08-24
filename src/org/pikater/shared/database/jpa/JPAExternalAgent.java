package org.pikater.shared.database.jpa;

import java.io.ByteArrayInputStream;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity	
@Table(name="ExternalAgent")
@NamedQueries({
	@NamedQuery(name="ExternalAgent.getAll",query="select o from JPAExternalAgent o"),
	@NamedQuery(name="ExternalAgent.getAll.count",query="select count(o) from JPAExternalAgent o"),
	//@NamedQuery(name="ExternalAgent.getByVisibility",query="select o from JPAExternalAgent o where o.visible=:visibility"),
	@NamedQuery(name="ExternalAgent.getByVisibility.count",query="select count(o) from JPAExternalAgent o where o.visible=:visibility"),
	@NamedQuery(name="ExternalAgent.getByID",query="select o from JPAExternalAgent o where o.id=:id"),
	@NamedQuery(name="ExternalAgent.getByAgentClass",query="select o from JPAExternalAgent o where o.agentClass=:agentClass"),
	@NamedQuery(name="ExternalAgent.getByOwner",query="select o from JPAExternalAgent o where o.owner=:owner"),
	@NamedQuery(name="ExternalAgent.getByOwner.count",query="select count(o) from JPAExternalAgent o where o.owner=:owner"),
	//@NamedQuery(name="ExternalAgent.getByOwnerAndVisibility",query="select o from JPAExternalAgent o where o.owner=:owner and o.visible=:visibility"),
	@NamedQuery(name="ExternalAgent.getByOwnerAndVisibility.count",query="select count(o) from JPAExternalAgent o where o.owner=:owner and o.visible=:visibility"),
	@NamedQuery(name="ExternalAgent.getByClass",query="select o from JPAExternalAgent o where o.agentClass=:class")
})
public class JPAExternalAgent extends JPAAbstractEntity{
	
	@Column(nullable=false,unique=true)
	private String name;
	@Column(nullable=false,unique=true)
	private String agentClass;
	private String description;
	@Column(nullable=false)
	private byte[] jar;
	private boolean approved;
	private boolean visible;
	private JPAUser owner;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAgentClass() {
		return agentClass;
	}
	public void setAgentClass(String agentClass) {
		this.agentClass = agentClass;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte[] getJar() {
		return jar;
	}
	public void setJar(byte[] jar) {
		this.jar = jar;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public JPAUser getOwner() {
		return owner;
	}
	public void setOwner(JPAUser owner) {
		this.owner = owner;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public ByteArrayInputStream getInputStream(){
		ByteArrayInputStream bis=new ByteArrayInputStream(jar);
		return bis;
	}
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPAExternalAgent updateValues=(JPAExternalAgent)newValues;
		this.setApproved(updateValues.isApproved());
		this.setVisible(updateValues.isVisible());
		this.setDescription(updateValues.getDescription());
		this.setAgentClass(updateValues.getAgentClass());
		this.setCreated(updateValues.getCreated());
		this.setName(updateValues.getName());
		this.setOwner(updateValues.getOwner());
	}
}
