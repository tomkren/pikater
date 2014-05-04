package org.pikater.shared.utilities.pikaterDatabase.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.JPABatchStatus;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.UserStatus;
import org.pikater.shared.utilities.pikaterDatabase.newDB.AbstractDAO;
import org.pikater.shared.utilities.pikaterDatabase.newDB.EntityManagerInstancesCreator;

public class BatchDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return (new JPABatch()).getEntityName();
	}

	@Override
	public List<JPABatch> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Batch.getAll", JPABatch.class)
				.getResultList();
	}

	@Override
	public List<JPABatch> getByID(int ID) {
		return getByTypedNamedQuery("Batch.getByID", "id", ID);
	}
	
	public List<JPABatch> getByStatus(JPABatchStatus status) {
		return getByTypedNamedQuery("Batch.getByStatus", "status", status);
	}
	
	private List<JPABatch> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPABatch.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}

	
}
