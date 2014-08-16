package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;
import org.pikater.shared.database.util.CustomActionResultFormatter;

public class ExperimentDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return JPAExperiment.EntityName;
	}

	@Override
	public List<JPAExperiment> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Experiment.getAll", JPAExperiment.class)
				.getResultList();
	}

	@Override
	public JPAExperiment getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPAExperiment>(
				getByTypedNamedQuery("Experiment.getByID", "id", ID),
				era
				).getSingleResultWithNull();
	}
	
	public List<JPAExperiment> getByBatch(JPABatch batch) {
		return getByTypedNamedQuery("Experiment.getByBatch", "batch", batch);
	}
	
	public List<JPAExperiment> getByStatus(JPAExperimentStatus status) {
		return getByTypedNamedQuery("Experiment.getByStatus", "status", status);
	}
	
	/**
	 * Persists experiment's result and binds it to the experiment 
	 * @param experimentID
	 * @param result
	 * @return The ID of the persisted result or -1 if error occured
	 */
	public int addResultToExperiment(int experimentID,JPAResult result){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPAExperiment exp=em.find(JPAExperiment.class, experimentID);
			if(exp!=null){
				result.setExperiment(exp);
				em.persist(result);
				exp.addResult(result);
				em.getTransaction().commit();
				return result.getId();
			}else{
				em.getTransaction().rollback();
				return -1;
			}
		}finally{
			em.close();
		}
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
	
	public void deleteExperimentEntity(JPAExperiment experiment){
		this.deleteExperimentByID(experiment.getId());
	}
	
	public void deleteExperimentByID(int id){
		this.deleteEntityByID(JPAExperiment.class, id);
	}
	
}
