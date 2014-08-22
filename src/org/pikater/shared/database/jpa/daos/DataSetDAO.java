package org.pikater.shared.database.jpa.daos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.status.JPADatasetSource;
import org.pikater.shared.database.postgre.largeobject.PGLargeObjectAction;
import org.pikater.shared.database.util.CustomActionResultFormatter;
import org.pikater.shared.database.util.Hash;
import org.pikater.shared.database.views.base.SortOrder;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBView;

public class DataSetDAO extends AbstractDAO{

	@Override
	public String getEntityName() {
		return JPADataSetLO.EntityName;
	}

	@Override
	public List<JPADataSetLO> getAll() {
		return EntityManagerInstancesCreator
		.getEntityManagerInstance()
		.createNamedQuery("DataSetLO.getAll", JPADataSetLO.class)
		.getResultList();
	}
	
	public List<JPADataSetLO> getAll(int offset,int maxResultCount) {
		return EntityManagerInstancesCreator
		.getEntityManagerInstance()
		.createNamedQuery("DataSetLO.getAll", JPADataSetLO.class)
		.setFirstResult(offset)
		.setMaxResults(maxResultCount)
		.getResultList();
	}
	
	private Path<Object> convertColumnToJPAParam(Root<JPADataSetLO> root,ITableColumn column){
		switch((DataSetTableDBView.Column)column){
		case CREATED:
		case DESCRIPTION:
		case SIZE:
			return root.get(column.toString().toLowerCase());
		case NUMBER_OF_INSTANCES:
			return root.get("globalMetaData").get("numberofInstances");
		case DEFAULT_TASK_TYPE:
			return root.get("globalMetaData").get("defaultTaskType").get("name");
		case OWNER:
			return root.get("owner").get("login");
		default:
			return root.get("created");
		}
	}
	
	public List<JPADataSetLO> getAll(int offset, int maxResults,ITableColumn sortColumn, SortOrder sortOrder) {
		return this.getAll(offset, maxResults, sortColumn, sortOrder, true, false);
	}
	
	public List<JPADataSetLO> getAllVisible(int offset, int maxResults,ITableColumn sortColumn, SortOrder sortOrder) {
		return this.getAll(offset, maxResults, sortColumn, sortOrder, false, false);
	}
	
	public List<JPADataSetLO> getAllVisibleApproved(int offset, int maxResults,ITableColumn sortColumn, SortOrder sortOrder) {
		return this.getAll(offset, maxResults, sortColumn, sortOrder, false, true);
	}
	
	/**
	 * Returns a list of all datasets available in the database. The result include invisible datasets if the last parameter is set to true. 
	 * @param offset the position from which the elements are returned
	 * @param maxResults maximum number of retrieved elements
	 * @param sortColumn column upon which the result is sorted
	 * @param sortOrder ascending or descending order of sorting
	 * @param includeDeleted false to retrieve only visible datasets, true to retrieve all items
	 * @return the list of all visible datasets
	 */
	public List<JPADataSetLO> getAll(int offset, int maxResults,ITableColumn sortColumn, SortOrder sortOrder,boolean includeDeleted,boolean justApproved) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPADataSetLO> q=cb.createQuery(JPADataSetLO.class);
		Root<JPADataSetLO> c=q.from(JPADataSetLO.class);
		q.select(c);
		if(!includeDeleted){
			q.where(cb.equal(c.get("visible"), true));
		}
		if(justApproved){
			q.where(cb.equal(c.get("approved"), true));
		}
		switch (sortOrder) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPADataSetLO> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	public List<JPADataSetLO> getAllUserUploaded(){
		return this.getAllBySource(JPADatasetSource.USER_UPLOAD);
	}
	
	public List<JPADataSetLO> getAllExperimentUploaded(){
		return this.getAllBySource(JPADatasetSource.EXPERIMENT);
	}
	
	public List<JPADataSetLO> getAllBySource(JPADatasetSource source){
		return this.getByTypedNamedQuery("DataSetLO.getAllBySoruce", "source", source);
	}
	
	public List<JPADataSetLO> getByOwner(JPAUser owner, int offset, int maxResults, ITableColumn sortColumn, SortOrder sortOrder) {
		return this.getByOwner(owner, offset, maxResults, sortColumn, sortOrder,true);
	}
	
	public List<JPADataSetLO> getByOwnerVisible(JPAUser owner, int offset, int maxResults, ITableColumn sortColumn, SortOrder sortOrder) {
		return this.getByOwner(owner, offset, maxResults, sortColumn, sortOrder,false);
	}
	
	public List<JPADataSetLO> getByOwner(JPAUser owner, int offset, int maxResults, ITableColumn sortColumn, SortOrder sortOrder, boolean includeDeleted) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPADataSetLO> q=cb.createQuery(JPADataSetLO.class);
		Root<JPADataSetLO> c=q.from(JPADataSetLO.class);
		q.select(c);
		q.where(cb.equal(c.get("owner"), owner));
		if(!includeDeleted){
			q.where(cb.equal(c.get("visible"), true));
		}
		switch (sortOrder) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPADataSetLO> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	public int getAllCount(){
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("DataSetLO.getAll.count")
				.getSingleResult())
				.intValue();
	}
	
	public int getAllVisibleCount(){
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("DataSetLO.getAllVisible.count")
				.getSingleResult())
				.intValue();
	}
	
	public List<JPADataSetLO> getAllExcludingHashes(List<String> hashesToBeExcluded){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb= em.getCriteriaBuilder();
		CriteriaQuery<JPADataSetLO> cq=cb.createQuery(JPADataSetLO.class);
		Root<JPADataSetLO> r=cq.from(JPADataSetLO.class);
		
		Predicate p=cb.conjunction();
		for(String exHash:hashesToBeExcluded){
			p=cb.and(p,cb.equal(r.get("hash"),exHash).not());
		}
		cq=cq.where(p);
		List<JPADataSetLO> res=em.createQuery(cq).getResultList();
		
		return res;
	}

	@Override
	public JPADataSetLO getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPADataSetLO>(
				getByTypedNamedQuery("DataSetLO.getByID", "id", ID),
				era
				).getSingleResultWithNull();
	}
	
	public List<JPADataSetLO> getByOwner(JPAUser user) {
		return getByTypedNamedQuery("DataSetLO.getByOwner", "owner", user);
	}
	
	public List<JPADataSetLO> getByOwner(JPAUser user, int offset, int maxResultCount) {
		return getByTypedNamedQuery("DataSetLO.getByOwner", "owner", user,offset,maxResultCount);
	}
	
	public int getByOwnerCount(JPAUser user){
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("DataSetLO.getByOwner.count")
				.setParameter("owner", user)
				.getSingleResult())
				.intValue();
	}
	
	public int getByOwnerVisibleCount(JPAUser user){
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("DataSetLO.getByOwnerVisible.count")
				.setParameter("owner", user)
				.getSingleResult())
				.intValue();
	}
	
	public List<JPADataSetLO> getByOID(long oid) {
		return getByTypedNamedQuery("DataSetLO.getByOID", "oid", oid);
	}
	
	public List<JPADataSetLO> getByHash(String hash) {
		return getByTypedNamedQuery("DataSetLO.getByHash", "hash", hash);
	}
	
	public List<JPADataSetLO> getByDescription(String description) {
		return getByTypedNamedQuery("DataSetLO.getByDescription", "description", description);
	}
	
	public List<JPADataSetLO> getAllWithResults(){
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("DataSetLO.getAllWithResults", JPADataSetLO.class)
				.getResultList();
	}
	
	public List<JPADataSetLO> getAllWithResultsExcludingHashes(List<String> hashesToBeExcluded){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb= em.getCriteriaBuilder();
		
		CriteriaQuery<JPADataSetLO> cq=cb.createQuery(JPADataSetLO.class);
		Root<JPADataSetLO> r=cq.from(JPADataSetLO.class);
		
		Predicate p=cb.conjunction();
		for(String exHash:hashesToBeExcluded){
			p=cb.and(p,cb.equal(r.get("hash"),exHash).not());
		}
		
		Subquery<JPAResult> subquery=cq.subquery(JPAResult.class);
		Root<JPAResult> sqroot=subquery.from(JPAResult.class);
		
		subquery=subquery.where(cb.equal(sqroot.get("serializedFileName"), r.get("hash")));
		
		p=cb.and(p, cb.exists(subquery));
		
		
		cq=cq.where(p);
		
		List<JPADataSetLO> res=em.createQuery(cq).getResultList();
		
		return res;
	}
	
	/**
	 * TODO: make it transactional
	 * Stores a new dataset from file to the database and creates the corresponding {@link JPADataSetLO} and {@link JPAFilemapping} objects.
	 * @param sourceFile
	 * @param description
	 * @param userID
	 * @return ID of the new {@link JPADataSetLO} object
	 * @throws IOException
	 */
	public int storeNewDataSet(File sourceFile, String description, int userID,JPADatasetSource datasetSource) throws IOException{
			
		JPAUser owner = DAOs.userDAO.getByID(userID);
		
		JPADataSetLO newDSLO=new JPADataSetLO();
		newDSLO.setCreated(new Date());
		newDSLO.setDescription(description);
		newDSLO.setOwner(owner);
		newDSLO.setApproved(owner.isAdmin());
		
		long oid = -1;
		String hash = Hash.getMD5Hash(sourceFile);
		List<JPADataSetLO> sameHashDS = DAOs.dataSetDAO.getByHash(hash);
		if (sameHashDS.size() > 0) {
			oid = sameHashDS.get(0).getOID();
		} else {
			try {
				oid = new PGLargeObjectAction(null).uploadLOToDB(sourceFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JPAFilemapping fm=new JPAFilemapping();
		fm.setExternalfilename(sourceFile.getName());
		fm.setInternalfilename(hash);
		fm.setUser(owner);
		DAOs.filemappingDAO.storeEntity(fm);
		newDSLO.setHash(hash);
		newDSLO.setOID(oid);
		newDSLO.setSize(sourceFile.length());
		newDSLO.setSource(datasetSource);
		storeEntity(newDSLO);
		
		return newDSLO.getId();
	}
		
	private List<JPADataSetLO> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPADataSetLO.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
	private List<JPADataSetLO> getByTypedNamedQuery(String queryName,String paramName,Object param, int offset, int maxResultCount){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPADataSetLO.class)
				.setParameter(paramName, param)
				.setFirstResult(offset)
				.setMaxResults(maxResultCount)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
	
	public void updateEntity(JPADataSetLO changedEntity){
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPADataSetLO item=em.find(JPADataSetLO.class, changedEntity.getId());
			item.updateValues(changedEntity);
			if(changedEntity.getGlobalMetaData()!=null){
				JPAGlobalMetaData persistedGM=em.find(JPAGlobalMetaData.class, changedEntity.getGlobalMetaData().getId());
				if(persistedGM!=null){
					item.setGlobalMetaData(persistedGM);
				}
			}
			
			if(changedEntity.getAttributeMetaData()!=null){
				List<JPAAttributeMetaData> attrList=new ArrayList<JPAAttributeMetaData>();
				/**System.err.println("No. of attr metadatas : "+changedEntity.getAttributeMetaData().size());
				for(JPAAttributeMetaData amd : changedEntity.getAttributeMetaData()){
					System.err.println(amd+"   ID: "+amd.getId());
				}**/
				for(JPAAttributeMetaData amd : changedEntity.getAttributeMetaData()){
					if(amd instanceof JPAAttributeCategoricalMetaData){
						//System.err.println("Categorical: "+amd.getId());
						JPAAttributeCategoricalMetaData persistedcatMD=em.find(JPAAttributeCategoricalMetaData.class,amd.getId());
						if(persistedcatMD!=null){
							//System.err.println("PERSISTED");
							attrList.add(persistedcatMD);
						}else{
							//System.err.println("NON-PERSISTED");
							attrList.add((JPAAttributeCategoricalMetaData)amd);
						}
					}else if(amd instanceof JPAAttributeNumericalMetaData){
						//System.err.println("Numerical: "+amd.getId());
						JPAAttributeNumericalMetaData persistednumMD=em.find(JPAAttributeNumericalMetaData.class,amd.getId());
						if(persistednumMD!=null){
							//System.err.println("PERSISTED");
							attrList.add(persistednumMD);
						}else{
							//System.err.println("NON-PERSISTED");
							attrList.add((JPAAttributeNumericalMetaData)amd);
						}
					}
					
				}
				item.setAttributeMetaData(attrList);
			}
			
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't update JPA DataSetLO object.", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
	public void deleteDataSetEntity(JPADataSetLO datasetlo){
		this.deleteDatasetByID(datasetlo.getId());
	}
	
	public void deleteDatasetByID(int id){
		this.deleteEntityByID(JPADataSetLO.class, id);
	}

}
