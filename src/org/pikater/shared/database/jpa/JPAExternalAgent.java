package org.pikater.shared.database.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity	
@Table(name="ExternalAgent")
@NamedQueries({
	@NamedQuery(name="ExternalAgent.getByClass",query="select o from JPAExternalAgent o where o.agentClass=:class")
})
public class JPAExternalAgent {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@Column(nullable=false,unique=true)
	private String name;
	@Column(nullable=false,unique=true)
	private String agentClass;
	private String description;
	@Column(nullable=false)
	private byte[] jar;

	public int getId() {
		return id;
	}
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
}
