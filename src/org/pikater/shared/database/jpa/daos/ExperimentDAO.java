package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;

public class ExperimentDAO extends AbstractDAO<JPAExperiment> {

	public ExperimentDAO(){
		super(JPAExperiment.class);
	}
	
	@Override
	public String getEntityName() {
		return JPAExperiment.EntityName;
	}
	
	public List<JPAExperiment> getByBatch(JPABatch batch) {
		return getByTypedNamedQuery("Experiment.getByBatch", "batch", batch);
	}
	
	public List<JPAExperiment> getByStatus(JPAExperimentStatus status) {
		return getByTypedNamedQuery("Experiment.getByStatus", "status", status);
	}
	
	/**
	 * Creates a list of all experiments, where the result has attached model
	 * @param batch {@link JPABatch} for which we wearch experiments
	 * @param offset
	 * @param maxResultCount
	 * @return the list of experiments with models
	 */
	public List<JPAExperiment> getByBatchWithModel(JPABatch batch, int offset, int maxResultCount){
		return getByTypedNamedQuery("Experiment.getByBatchWithModel", "batch", batch, offset, maxResultCount);
	}
	
	public int getByBatchWithModelCount(JPABatch batch){
		return getByCountQuery("Experiment.getByBatchWithModel.count", "batch", batch);
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
	
	public void deleteExperimentEntity(JPAExperiment experiment){
		this.deleteExperimentByID(experiment.getId());
	}
	
	public void deleteExperimentByID(int id){
		this.deleteEntityByID(id);
	}
	
}
