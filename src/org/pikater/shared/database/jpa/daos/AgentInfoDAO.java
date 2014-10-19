package org.pikater.shared.database.jpa.daos;

import java.util.Date;
import java.util.List;

import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.shared.database.jpa.JPAAgentInfo;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPAUser;

public class AgentInfoDAO extends AbstractDAO<JPAAgentInfo> {

	public AgentInfoDAO() {
		super(JPAAgentInfo.class);
	}

	@Override
	public String getEntityName() {
		return JPAAgentInfo.EntityName;
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

	/**
	 * Retrieves the agent info records for agents, that were created by certain {@link JPAUser}
	 * If the owner is null, that agent infos with no associated external agents are returned 
	 * @param owner {@link JPAUser} for which we search agent infos
	 * @return the list of user's agetn infos or all internal agents
	 */
	public List<JPAAgentInfo> getByExternalAgentOwner(JPAUser owner) {
		if (owner == null) {
			return getByTypedNamedQuery("AgentInfo.getWithoutExternal");
		} else {
			return getByTypedNamedQuery("AgentInfo.getByExternalAgentOwner", "owner", owner);
		}
	}

	public void storeAgentInfoOntology(AgentInfo agentInfoOntology, JPAExternalAgent externalAgent) {
		JPAAgentInfo nai = new JPAAgentInfo();
		nai.setAgentClass(agentInfoOntology.getAgentClassName());
		nai.setOntologyClass(agentInfoOntology.getOntologyClassName());
		nai.setDescription(agentInfoOntology.getDescription());
		nai.setInformationXML(agentInfoOntology.exportXML());
		nai.setName(agentInfoOntology.getName());
		nai.setCreationTime(new Date());
		nai.setExternalAgent(externalAgent);
		DAOs.agentInfoDAO.storeEntity(nai);
	}
}
