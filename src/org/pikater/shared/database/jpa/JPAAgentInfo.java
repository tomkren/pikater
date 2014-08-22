package org.pikater.shared.database.jpa;

import java.util.Date;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity	
@Table(name="AgentInfo")
@NamedQueries({
	@NamedQuery(name="AgentInfo.getAll",query="select ai from JPAAgentInfo ai"),
	@NamedQuery(name="AgentInfo.getByID",query="select ai from JPAAgentInfo ai where ai.id=:id"),
	@NamedQuery(name="AgentInfo.getByName",query="select ai from JPAAgentInfo ai where ai.name=:name"),
	@NamedQuery(name="AgentInfo.getByAgentClass",query="select ai from JPAAgentInfo ai where ai.agentClass=:agentClass"),
	@NamedQuery(name="AgentInfo.getByOntologyClass",query="select ai from JPAAgentInfo ai where ai.ontologyClass=:ontologyClass")
})
public class JPAAgentInfo extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@Lob
	private String informationXML;
	private String name;
	private String agentClass;
	private String ontologyClass;
	private String description;
	@Nullable
	private JPAUser owner;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	public int getId() {
        return id;
    }
	public String getInformationXML() {
		return informationXML;
	}
	public void setInformationXML(String informationXML) {
		this.informationXML = informationXML;
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
	public String getOntologyClass() {
		return ontologyClass;
	}
	public void setOntologyClass(String ontologyClass) {
		this.ontologyClass = ontologyClass;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public JPAUser getOwner() {
		return owner;
	}
	public void setOwner(JPAUser owner) {
		this.owner = owner;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	@Transient
	public static final String EntityName = "AgentInfo";

	@Override
	public void updateValues(JPAAbstractEntity newValues) {
		JPAAgentInfo updateValues=(JPAAgentInfo)newValues;
		this.informationXML=updateValues.getInformationXML();
		this.agentClass=updateValues.getAgentClass();
		this.ontologyClass=updateValues.getOntologyClass();
		this.description=updateValues.getDescription();
		this.name=updateValues.getName();
		this.creationTime=updateValues.getCreationTime();
		this.owner=updateValues.getOwner();
	}
	
}
