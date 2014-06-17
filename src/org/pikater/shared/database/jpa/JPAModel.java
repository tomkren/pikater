package org.pikater.shared.database.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	@NamedQuery(name="Model.getByAgentClassName",query="select m from JPAModel m where m.agentClassName=:agentClassName")
})
public class JPAModel extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@OneToOne
	private JPAResult creatorResult;
	private String agentClassName;
    @Lob
    private String serializedAgent;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
    public int getId() {
        return id;
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

	public String getSerializedAgent() {
		return serializedAgent;
	}

	public void setSerializedAgent(String serializedAgent) {
		this.serializedAgent = serializedAgent;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	@Transient
	public static final String EntityName = "Model";
	
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		throw new NotUpdatableEntityException();
	}
    
}
