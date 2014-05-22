package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExperimentStatus;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;

public class ExperimentDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return (new JPAExperiment()).getEntityName();
	}

	@Override
	public List<JPAExperiment> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Experiment.getAll", JPAExperiment.class)
				.getResultList();
	}

	@Override
	public List<JPAExperiment> getByID(int ID) {
		return getByTypedNamedQuery("Experiment.getByID", "id", ID);
	}
	
	public List<JPAExperiment> getByBatch(JPABatch batch) {
		return getByTypedNamedQuery("Experiment.getByBatch", "batch", batch);
	}
	
	public List<JPAExperiment> getByStatus(JPAExperimentStatus status) {
		return getByTypedNamedQuery("Experiment.getByStatus", "status", status);
	}
	
	private List<JPAExperiment> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAExperiment.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
	
	public void updateEntity(JPAExperiment changedEntity){
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPAExperiment item=em.find(JPAExperiment.class, changedEntity.getId());
			item.updateValues(changedEntity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't update JPA Experiment object.", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}

	
}
