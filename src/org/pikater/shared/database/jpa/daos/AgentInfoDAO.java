package org.pikater.shared.database.jpa.daos;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAgentInfo;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.util.CustomActionResultFormatter;

public class AgentInfoDAO extends AbstractDAO{

	@Override
	public String getEntityName() {
		return JPAAgentInfo.EntityName;
	}

	@Override
	public List<JPAAgentInfo> getAll() {
		return EntityManagerInstancesCreator
		.getEntityManagerInstance()
		.createNamedQuery("AgentInfo.getAll", JPAAgentInfo.class)
		.getResultList();
	}
	
	@Override
	public JPAAgentInfo getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPAAgentInfo>(
				getByTypedNamedQuery(JPAAgentInfo.class,"AgentInfo.getByID", "id", ID),
				era
				).getSingleResultWithNull();
	}
	
	public List<JPAAgentInfo> getByName(String name) {
		return getByTypedNamedQuery(JPAAgentInfo.class,"AgentInfo.getByName", "name", name);
	}
	
	public List<JPAAgentInfo> getByAgentClass(String agentClass) {
		return getByTypedNamedQuery(JPAAgentInfo.class,"AgentInfo.getByAgentClass", "agentClass", agentClass);
	}
	
	public List<JPAAgentInfo> getByOntologyClass(String ontologyClass) {
		return getByTypedNamedQuery(JPAAgentInfo.class,"AgentInfo.getByOntologyClass", "ontologyClass", ontologyClass);
	}
	
	/**
	 * Retrieves the agent info records for agents, that were created by certain {@link JPAUser}
	 * If the owner is null, that agent infos with no associated external agents are returned 
	 * @param owner {@link JPAUser} for which we search agent infos
	 * @return the list of user's agetn infos or all internal agents
	 */
	public List<JPAAgentInfo> getByExternalAgentOwner(JPAUser owner){
		if(owner==null){
			return getByTypedNamedQuery(JPAAgentInfo.class, "AgentInfo.getWithoutExternal");
		}else{
			return getByTypedNamedQuery(JPAAgentInfo.class,"AgentInfo.getByExternalAgentOwner", "owner", owner);
		}
	}
	
	public void storeAgentInfoOntology(AgentInfo agentInfoOntology, JPAUser user){
		JPAAgentInfo nai=new JPAAgentInfo();
		nai.setAgentClass(agentInfoOntology.getAgentClassName());
		nai.setOntologyClass(agentInfoOntology.getOntologyClassName());
		nai.setDescription(agentInfoOntology.getDescription());
		nai.setInformationXML(agentInfoOntology.exportXML());
		nai.setName(agentInfoOntology.getName());
		nai.setCreationTime(new Date());
		nai.setOwner(user);
		DAOs.agentInfoDAO.storeEntity(nai);
	}
	
	public void updateEntity(JPAAgentInfo changedEntity){
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPAAgentInfo item=em.find(JPAAgentInfo.class, changedEntity.getId());
			item.updateValues(changedEntity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't update JPA AgentInfo object.", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
	public void deleteAgentInfoEntity(JPAAgentInfo agentinfo){
		this.deleteAgentInfoByID(agentinfo.getId());
	}
	
	public void deleteAgentInfoByID(int id){
		this.deleteEntityByID(JPAAgentInfo.class, id);
	}
}
