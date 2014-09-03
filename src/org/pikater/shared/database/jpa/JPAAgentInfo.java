package org.pikater.shared.database.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	@NamedQuery(name="AgentInfo.getByName",query="select ai from JPAAgentInfo ai where ai.name=:name"),
	@NamedQuery(name="AgentInfo.getByAgentClass",query="select ai from JPAAgentInfo ai where ai.agentClass=:agentClass"),
	@NamedQuery(name="AgentInfo.getByOntologyClass",query="select ai from JPAAgentInfo ai where ai.ontologyClass=:ontologyClass"),
	@NamedQuery(name="AgentInfo.getWithoutExternal",query="select ai from JPAAgentInfo ai where ai.externalAgent is null"),
	@NamedQuery(name="AgentInfo.getByExternalAgentOwner",query="select ai from JPAAgentInfo ai where ai.externalAgent.owner=:owner")
})
public class JPAAgentInfo extends JPAAbstractEntity{
	
	@Lob
	private String informationXML;
	private String name;
	private String agentClass;
	private String ontologyClass;
	private String description;
	@Column(nullable=true)
	private JPAExternalAgent externalAgent;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
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
	public JPAExternalAgent getExternalAgent() {
		return externalAgent;
	}
	public void setExternalAgent(JPAExternalAgent externalAgent) {
		this.externalAgent = externalAgent;
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
		this.externalAgent=updateValues.getExternalAgent();
	}
	
}
