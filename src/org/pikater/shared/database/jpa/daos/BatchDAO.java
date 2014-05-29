package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
import org.pikater.shared.database.utils.ResultFormatter;

public class BatchDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return JPABatch.EntityName;
	}

	@Override
	public List<JPABatch> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Batch.getAll", JPABatch.class)
				.getResultList();
	}

	@Override
	public JPABatch getByID(int ID) {
		return new ResultFormatter<JPABatch>(
				getByTypedNamedQuery("Batch.getByID", "id", ID)
				).getSingleResultWithNull();
	}
	
	@Override
	public JPABatch getByIDWithException(int ID) throws NoResultException{
		return new ResultFormatter<JPABatch>(
				getByTypedNamedQuery("Batch.getByID", "id", ID)
				).getSingleResult();
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

	public void updateEntity(JPABatch changedEntity){
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPABatch item=em.find(JPABatch.class, changedEntity.getId());
			item.updateValues(changedEntity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't update JPA Batch object.", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
}
