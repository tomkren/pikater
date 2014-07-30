package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.pikater.core.ontology.subtrees.experiment.Experiment;
import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAModel;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
import org.pikater.shared.database.utils.CustomActionResultFormatter;
import org.pikater.shared.database.utils.ResultFormatter;
import org.pikater.shared.database.views.base.SortOrder;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.batches.AbstractBatchTableDBView;

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
	
	public List<JPABatch> getAll(int offset, int maxResultCount) {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Batch.getAll", JPABatch.class)
				.setFirstResult(offset)
				.setMaxResults(maxResultCount)
				.getResultList();
	}
	
	private Path<Object> convertColumnToJPAParam(Root<JPABatch> root,ITableColumn column){
		switch((AbstractBatchTableDBView.Column)column){
		case CREATED: 
		case FINISHED: 
		case NAME:
		case NOTE:
		case PRIORITY:
		case STATUS:
			return root.get(column.toString().toLowerCase());
		case MAX_PRIORITY:
			return root.get("totalPriority");
		case OWNER:
			return root.get("owner").get("login");
		default:
			return root.get("name");
		}
	}
	
	public List<JPABatch> getAll(int offset, int maxResults, ITableColumn sortColumn,SortOrder order) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPABatch> q=cb.createQuery(JPABatch.class);
		Root<JPABatch> c=q.from(JPABatch.class);
		q.select(c);
		switch (order) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPABatch> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	public List<JPABatch> getAllExcludeByStatus(int offset, int maxResults, ITableColumn sortColumn,SortOrder order,JPABatchStatus status) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPABatch> q=cb.createQuery(JPABatch.class);
		Root<JPABatch> c=q.from(JPABatch.class);
		q.select(c);
		q.where(cb.notEqual(c.get("status"), status));
		switch (order) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPABatch> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	public List<JPABatch> getByOwner(JPAUser owner,int offset, int maxResults, ITableColumn sortColumn,SortOrder order) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPABatch> q=cb.createQuery(JPABatch.class);
		Root<JPABatch> c=q.from(JPABatch.class);
		q.select(c);
		q.where(cb.equal(c.get("owner"), owner));
		switch (order) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPABatch> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	
	public int getAllExcludedStatusCount(JPABatchStatus status) {
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Batch.getAllNotStatus.count")
				.setParameter("status", status)
				.getSingleResult())
				.intValue();
	}

	public int getByOwnerAndStatusCount(JPAUser owner,JPABatchStatus status) {
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Batch.getByOwnerAndStatus.count")
				.setParameter("owner", owner)
				.setParameter("status", status)
				.getSingleResult())
				.intValue();
	}	
	
	
	public List<JPABatch> getByOwnerAndStatus(JPAUser owner,JPABatchStatus status,int offset, int maxResults, ITableColumn sortColumn,SortOrder order) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPABatch> q=cb.createQuery(JPABatch.class);
		Root<JPABatch> c=q.from(JPABatch.class);
		q.select(c);
		q.where(cb.and(cb.equal(c.get("owner"), owner),cb.equal(c.get("status"), status)));
		switch (order) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPABatch> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	public int getByOwnerAndNotStatusCount(JPAUser owner,JPABatchStatus status) {
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Batch.getByOwnerAndNotStatus.count")
				.setParameter("owner", owner)
				.setParameter("status", status)
				.getSingleResult())
				.intValue();
	}	
	
	
	public List<JPABatch> getByOwnerAndNotStatus(JPAUser owner,JPABatchStatus status,int offset, int maxResults, ITableColumn sortColumn,SortOrder order) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPABatch> q=cb.createQuery(JPABatch.class);
		Root<JPABatch> c=q.from(JPABatch.class);
		q.select(c);
		q.where(cb.and(cb.equal(c.get("owner"), owner),cb.notEqual(c.get("status"), status)));
		switch (order) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPABatch> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	
	public int getAllCount() {
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Batch.getAll.count")
				.getSingleResult())
				.intValue();
	}

	@Override
	public JPABatch getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPABatch>(
				getByTypedNamedQuery("Batch.getByID", "id", ID),
				era
				).getSingleResultWithNull();
	}
	
	public List<JPABatch> getByStatus(JPABatchStatus status) {
		return getByTypedNamedQuery("Batch.getByStatus", "status", status);
	}
	
	public List<JPABatch> getByOwner(JPAUser owner) {
		return getByTypedNamedQuery("Batch.getByOwner", "owner", owner);
	}
	
	public List<JPABatch> getByOwner(JPAUser owner,int offset,int maxResultCount) {
		return getByTypedNamedQuery("Batch.getByOwner", "owner", owner,offset,maxResultCount);
	}
	
	public int getByOwnerCount(JPAUser owner) {
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Batch.getByOwner.count")
				.setParameter("owner", owner)
				.getSingleResult())
				.intValue();
	}
	
	/**
	 * 
	 * @param experiment
	 * @return the ID of the saved Experiment Entity, -1 when failed to save
	 */
	public int addExperimentToBatch(Experiment experiment){
		int batchID=experiment.getBatchID();
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPABatch batch=em.find(JPABatch.class, batchID);
			if(batch!=null){
				JPAExperiment jpaExperiment;
				
				if(experiment.getModel() == null){
					jpaExperiment=new JPAExperiment(batch);
				}else{
					int modelID=experiment.getModel();
					JPAModel model=em.find(JPAModel.class, modelID);
					jpaExperiment=new JPAExperiment(batch, model);
				}
				
				jpaExperiment.setStatus(experiment.getStatus());
				
				em.persist(jpaExperiment);
				batch.addExperiment(jpaExperiment);
				em.getTransaction().commit();
				return jpaExperiment.getId();
			}else{
				em.getTransaction().rollback();
				return -1;
			}
		}finally{
			em.close();
		}
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
	
	private List<JPABatch> getByTypedNamedQuery(String queryName,String paramName,Object param,int offset,int maxResultCount){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPABatch.class)
				.setParameter(paramName, param)
				.setFirstResult(offset)
				.setMaxResults(maxResultCount)
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
	
	public void deleteBatchEntity(JPABatch batch){
		this.deleteBatchByID(batch.getId());
	}
	
	public void deleteBatchByID(int id){
		this.deleteEntityByID(JPABatch.class, id);
	}
}
