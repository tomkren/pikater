package org.pikater.shared.database.jpa.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.pikater.core.ontology.subtrees.experiment.Experiment;
import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAModel;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.SortOrder;
import org.pikater.shared.database.views.tableview.batches.BatchTableDBView;
import org.pikater.shared.logging.database.PikaterDBLogger;

public class BatchDAO extends AbstractDAO<JPABatch> {

	public BatchDAO() {
		super(JPABatch.class);
	}

	@Override
	public String getEntityName() {
		return JPABatch.EntityName;
	}

	protected Path<Object> convertColumnToJPAParam(ITableColumn column) {
		Root<JPABatch> root = this.getRoot();
		switch ((BatchTableDBView.Column) column) {
		case CREATED:
		case FINISHED:
		case NAME:
		case NOTE:
		case STATUS:
			return root.get(column.toString().toLowerCase());
		case TOTAL_PRIORITY:
			return root.get("totalPriority");
		case OWNER:
			return root.get("owner").get("login");
		default:
			return root.get("name");
		}
	}

	private Predicate createAllExcludeByStatusPredicate(JPABatchStatus status) {
		return getCriteriaBuilder().notEqual(getRoot().get("status"), status);
	}

	/**
	 * Creates a list of all batches, that doesn't have the specified status.
	 * @return the list of batches
	 */
	public List<JPABatch> getAllExcludeByStatus(int offset, int maxResultCount, ITableColumn sortColumn, SortOrder sortOrder, JPABatchStatus status) {
		return getByCriteriaQuery(createAllExcludeByStatusPredicate(status), sortColumn, sortOrder, offset, maxResultCount);
	}

	/**
	 * Computes the number of batches, that doesn't have the specified status
	 * @return the number of batches
	 */
	public int getAllExcludeByStatusCount(JPABatchStatus status) {
		return getByCriteriaQueryCount(createAllExcludeByStatusPredicate(status));
	}

	private Predicate createByOwnerPredicate(JPAUser owner) {
		return getCriteriaBuilder().equal(getRoot().get("owner"), owner);
	}

	/**
	 * Creates a list of batches, that are associated with the given user   
	 * @param owner {@link JPAUser} object of the user
	 * @param offset the position from which the elements are returned
	 * @param maxResultCount maximum number of retrieved elements
	 * @param sortColumn column upon which the result is sorted
	 * @param sortOrder ascending or descending order of sorting
	 * @return list the list of batches 
	 */
	public List<JPABatch> getByOwner(JPAUser owner, int offset, int maxResultCount, ITableColumn sortColumn, SortOrder sortOrder) {
		return getByCriteriaQuery(createByOwnerPredicate(owner), sortColumn, sortOrder, offset, maxResultCount);
	}

	public int getBatchResultCount(JPABatch batch) {
		return getByCountQuery("Batch.getByIDonlyResults.count", "batchID", batch.getId());
	}

	private Predicate createByOwnerAndStatusPredicate(JPAUser owner, JPABatchStatus status) {
		return getCriteriaBuilder().and(getCriteriaBuilder().equal(getRoot().get("owner"), owner), getCriteriaBuilder().equal(getRoot().get("status"), status));
	}

	/**
	 * Creates a list of batches with specified status for the given user.
	 * @return the list of batches
	 */
	public List<JPABatch> getByOwnerAndStatus(JPAUser owner, JPABatchStatus status, int offset, int maxResultCount, ITableColumn sortColumn, SortOrder sortOrder) {
		return getByCriteriaQuery(createByOwnerAndStatusPredicate(owner, status), sortColumn, sortOrder, offset, maxResultCount);
	}

	/**
	 * Computes the number of batches with the given status, for the specified user
	 * @return the number of batches
	 */
	public int getByOwnerAndStatusCount(JPAUser owner, JPABatchStatus status) {
		return getByCriteriaQueryCount(createByOwnerAndStatusPredicate(owner, status));
	}

	private Predicate createByOwnerAndNotStatusPredicate(JPAUser owner, JPABatchStatus status) {
		return getCriteriaBuilder().and(getCriteriaBuilder().equal(getRoot().get("owner"), owner), getCriteriaBuilder().notEqual(getRoot().get("status"), status));
	}

	/**
	 * Creates list of batches of given user, that don't have the specified status.
	 * @return list of batches
	 */
	public List<JPABatch> getByOwnerAndNotStatus(JPAUser owner, JPABatchStatus status, int offset, int maxResultCount, ITableColumn sortColumn, SortOrder sortOrder) {
		return getByCriteriaQuery(createByOwnerAndNotStatusPredicate(owner, status), sortColumn, sortOrder, offset, maxResultCount);
	}

	/**
	 * Computes the number of batches of given user, that don't have the specified status
	 * @return the number of batches
	 */
	public int getByOwnerAndNotStatusCount(JPAUser owner, JPABatchStatus status) {
		return getByCriteriaQueryCount(createByOwnerAndNotStatusPredicate(owner, status));
	}

	public int getAllCount() {
		return ((Long) EntityManagerInstancesCreator.getEntityManagerInstance().createNamedQuery("Batch.getAll.count").getSingleResult()).intValue();
	}

	public List<JPABatch> getByStatus(JPABatchStatus status) {
		return getByTypedNamedQuery("Batch.getByStatus", "status", status);
	}

	public List<JPABatch> getByOwner(JPAUser owner) {
		return getByTypedNamedQuery("Batch.getByOwner", "owner", owner);
	}

	public List<JPABatch> getByOwner(JPAUser owner, int offset, int maxResultCount) {
		return getByTypedNamedQuery("Batch.getByOwner", "owner", owner, offset, maxResultCount);
	}

	public int getByOwnerCount(JPAUser owner) {
		return ((Long) EntityManagerInstancesCreator.getEntityManagerInstance().createNamedQuery("Batch.getByOwner.count").setParameter("owner", owner).getSingleResult()).intValue();
	}

	/**
	 * 
	 * @return the ID of the saved Experiment Entity, -1 when failed to save
	 */
	public int addExperimentToBatch(Experiment experiment) {
		int batchID = experiment.getBatchID();
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try {
			JPABatch batch = em.find(JPABatch.class, batchID);
			if (batch != null) {
				JPAExperiment jpaExperiment;

				if (experiment.getModel() == null) {
					jpaExperiment = new JPAExperiment(batch);
				} else {
					int modelID = experiment.getModel();
					JPAModel model = em.find(JPAModel.class, modelID);
					jpaExperiment = new JPAExperiment(batch, model);
				}

				jpaExperiment.setStatus(experiment.getStatus());

				em.persist(jpaExperiment);
				batch.addExperiment(jpaExperiment);
				em.getTransaction().commit();
				return jpaExperiment.getId();
			} else {
				em.getTransaction().rollback();
				return -1;
			}
		} finally {
			em.close();
		}
	}

	public List<Object[]> getByIDwithResults(int batchID) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try {
			return em.createNamedQuery("Batch.getByIDwithResult", Object[].class).setParameter("batchID", batchID).getResultList();
		} finally {
			em.close();
		}
	}

	/**
	 * Retrieves all results for the given batch
	 * @param batchID ID of {@link JPABatch} entity
	 * @param offset index of the first result
	 * @param maxResultCount maximal number of needed results
	 * @return list of {@link JPAResult}s for the batch
	 */
	public List<JPAResult> getByIDwithResults(int batchID, int offset, int maxResultCount) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try {
			return em.createNamedQuery("Batch.getByIDonlyResults", JPAResult.class).setParameter("batchID", batchID).setFirstResult(offset).setMaxResults(maxResultCount).getResultList();
		} finally {
			em.close();
		}
	}

	public void updateEntity(JPABatch changedEntity) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try {
			JPABatch item = em.find(JPABatch.class, changedEntity.getId());
			item.updateValues(changedEntity);
			em.getTransaction().commit();
		} catch (Exception e) {
			PikaterDBLogger.logThrowable("Can't update JPA Batch object.", e);
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}

	public void deleteBatchEntity(JPABatch batch) {
		this.deleteBatchByID(batch.getId());
	}

	public void deleteBatchByID(int id) {
		this.deleteEntityByID(id);
	}

	/**
	 * Sets the status of computed batches to failed, because just after startup, there shouldn't be
	 * running computations.
	 */
	public void cleanUp() {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try {
			PikaterDBLogger.log(Level.INFO, "Starting cleaning up batches");
			List<JPABatch> batches = em.createNamedQuery("Batch.getByStatus", JPABatch.class).setParameter("status", JPABatchStatus.COMPUTING).getResultList();
			batches.addAll(em.createNamedQuery("Batch.getByStatus", JPABatch.class).setParameter("status", JPABatchStatus.WAITING).getResultList());
			batches.addAll(em.createNamedQuery("Batch.getByStatus", JPABatch.class).setParameter("status", JPABatchStatus.STARTED).getResultList());
			for (JPABatch batch : batches) {
				batch.setStatus(JPABatchStatus.FAILED.name());
			}
			em.getTransaction().commit();
			PikaterDBLogger.log(Level.INFO, "Cleaning up batches finished");
		} catch (Exception e) {
			PikaterDBLogger.logThrowable("Error during cleanup...", e);
			em.getTransaction().rollback();
		} finally {
			em.close();
		}

	}
}
