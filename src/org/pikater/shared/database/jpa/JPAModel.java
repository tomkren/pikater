package org.pikater.shared.database.jpa;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.pikater.shared.database.exceptions.NotUpdatableEntityException;

@Entity
@Table(name="Model")
@NamedQueries({
	@NamedQuery(name="Model.getAll",query="select m from JPAModel m"),
	@NamedQuery(name="Model.getByID",query="select m from JPAModel m where m.id=:id"),
	@NamedQuery(name="Model.getByAgentClassName",query="select m from JPAModel m where m.agentClassName=:agentClassName"),
	@NamedQuery(name="Model.getOlderThan",query="select m, r from JPAModel m, JPAResult r where r.createdModel=m and m.created < :paramDate")
})
public class JPAModel extends JPAAbstractEntity{
	
	@OneToOne
	private JPAResult creatorResult;
	private String agentClassName;
    @Lob
    private byte[] serializedAgent;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
    public JPAModel(){
    	this.created=new Date();
    }
    
    public JPAResult getCreatorResult() {
		return creatorResult;
	}

	public void setCreatorResult(JPAResult creatorResult) {
		this.creatorResult = creatorResult;
	}

	public String getAgentClassName() {
		return agentClassName;
	}

	public void setAgentClassName(String agentClassName) {
		this.agentClassName = agentClassName;
	}

	public byte[] getSerializedAgent() {
		return serializedAgent;
	}

	public void setSerializedAgent(byte[] serializedAgent) {
		this.serializedAgent = serializedAgent;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public InputStream getInputStream()
	{
		return new ByteArrayInputStream(serializedAgent);
	}
	
	public String getFileName()
	{
		return String.format("%d-%s.agent", creatorResult.getExperiment().getId(), creatorResult.getCreatedModel().getAgentClassName());
	}
	
	@Transient
	public static final String EntityName = "Model";
	
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		throw new NotUpdatableEntityException();
	}
    
}
