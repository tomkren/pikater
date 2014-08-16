package org.pikater.shared.database.jpa.daos;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAgentInfo;
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
				getByTypedNamedQuery("AgentInfo.getByID", "id", ID),
				era
				).getSingleResultWithNull();
	}
	
	public List<JPAAgentInfo> getByName(String name) {
		return getByTypedNamedQuery("AgentInfo.getByName", "name", name);
	}
	
	public List<JPAAgentInfo> getByAgentClass(String agentClass) {
		return getByTypedNamedQuery("AgentInfo.getByAgentClass", "agentClass", agentClass);
	}
	
	public List<JPAAgentInfo> getByOntologyClass(String ontologyClass) {
		return getByTypedNamedQuery("AgentInfo.getByOntologyClass", "ontologyClass", ontologyClass);
	}
		
	private List<JPAAgentInfo> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAAgentInfo.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
	public void storeAgentInfoOntology(AgentInfo agentInfoOntology){
		JPAAgentInfo nai=new JPAAgentInfo();
		nai.setAgentClass(agentInfoOntology.getAgentClassName());
		nai.setOntologyClass(agentInfoOntology.getOntologyClassName());
		nai.setDescription(agentInfoOntology.getDescription());
		nai.setInformationXML(agentInfoOntology.exportXML());
		nai.setName(agentInfoOntology.getName());
		nai.setCreationTime(new Date());
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
