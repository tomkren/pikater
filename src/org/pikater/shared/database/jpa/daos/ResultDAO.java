package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.status.JPAUserStatus;
import org.pikater.shared.database.utils.ResultFormatter;

public class ResultDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return JPAResult.EntityName;
	}

	@Override
	public List<JPAResult> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Result.getAll", JPAResult.class)
				.getResultList();
	}

	@Override
	public JPAResult getByID(int ID) {
		return new ResultFormatter<JPAResult>(
				getByTypedNamedQuery("Result.getByID", "id", ID)
				).getSingleResultWithNull();
	}
	
	@Override
	public JPAResult getByIDWithException(int ID)
			throws NoResultException {
		return new ResultFormatter<JPAResult>(
				getByTypedNamedQuery("Result.getByID", "id", ID)
				).getSingleResult();
	}
	
	public List<JPAResult> getByAgentName(String agentName) {
		return getByTypedNamedQuery("Result.getByAgentName", "agentName", agentName);
	}
	
	public List<JPAResult> getByExperiment(JPAExperiment experiment) {
		return getByTypedNamedQuery("Result.getByExperiment", "experiment", experiment);
	}
	
	public List<JPAResult> getByRole(String dataSetHash) {
		return getByTypedNamedQuery("Result.getByDataSetHash", "hash", dataSetHash);
	}
	
	private List<JPAResult> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAResult.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}	
}
