package org.pikater.shared.database.jpa.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;
import org.pikater.shared.logging.database.PikaterDBLogger;

public class ExperimentDAO extends AbstractDAO<JPAExperiment> {

	public ExperimentDAO() {
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
	 * @return the list of experiments with models
	 */
	public List<JPAExperiment> getByBatchWithModel(JPABatch batch, int offset, int maxResultCount) {
		return getByTypedNamedQuery("Experiment.getByBatchWithModel", "batch", batch, offset, maxResultCount);
	}

	public int getByBatchWithModelCount(JPABatch batch) {
		return getByCountQuery("Experiment.getByBatchWithModel.count", "batch", batch);
	}

	/**
	 * Persists experiment's result and binds it to the experiment 
	 * @return The ID of the persisted result or -1 if error occured
	 */
	public int addResultToExperiment(int experimentID, JPAResult result) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try {
			JPAExperiment exp = em.find(JPAExperiment.class, experimentID);
			if (exp != null) {
				result.setExperiment(exp);
				em.persist(result);
				exp.addResult(result);
				em.getTransaction().commit();
				return result.getId();
			} else {
				em.getTransaction().rollback();
				return -1;
			}
		} finally {
			em.close();
		}
	}

	public void deleteExperimentEntity(JPAExperiment experiment) {
		this.deleteExperimentByID(experiment.getId());
	}

	public void deleteExperimentByID(int id) {
		this.deleteEntityByID(id);
	}

	/**
	 * Sets the status of computed experiments to failed, because just after startup, there shouldn't be
	 * running computations.
	 */
	public void cleanUp() {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try {
			PikaterDBLogger.log(Level.INFO, "Starting cleaning up experiments");
			List<JPAExperiment> experiments = em.createNamedQuery("Experiment.getByStatus", JPAExperiment.class).setParameter("status", JPAExperimentStatus.COMPUTING).getResultList();
			for (JPAExperiment experiment : experiments) {
				experiment.setStatus(JPAExperimentStatus.FAILED.name());
			}

			em.getTransaction().commit();
			PikaterDBLogger.log(Level.INFO, "Cleaning up experiments finished");
		} catch (Exception e) {
			PikaterDBLogger.logThrowable("Error during cleanup...", e);
			em.getTransaction().rollback();
		} finally {
			em.close();
		}

	}

}
