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

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.pglargeobject.PostgreLobAccess;
import org.pikater.shared.database.utils.CustomActionResultFormatter;
import org.pikater.shared.database.utils.Hash;
import org.pikater.shared.database.utils.ResultFormatter;
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
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPADataSetLO> q=cb.createQuery(JPADataSetLO.class);
		Root<JPADataSetLO> c=q.from(JPADataSetLO.class);
		q.select(c);
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
	
	public List<JPADataSetLO> getByOwner(JPAUser owner, int offset, int maxResults, ITableColumn sortColumn, SortOrder sortOrder) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPADataSetLO> q=cb.createQuery(JPADataSetLO.class);
		Root<JPADataSetLO> c=q.from(JPADataSetLO.class);
		q.select(c);
		q.where(cb.equal(c.get("owner"), owner));
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
	 * @param initialData
	 * @return ID of the new {@link JPADataSetLO} object
	 * @throws IOException
	 */
	public int storeNewDataSet(File sourceFile,String description,String userlogin) throws IOException{
		
		JPAUser user=new ResultFormatter<JPAUser>(DAOs.userDAO.getByLogin(userlogin)).getSingleResult();
		
		JPADataSetLO newDSLO=new JPADataSetLO();
		newDSLO.setCreated(new Date());
		newDSLO.setDescription(description);
		newDSLO.setOwner(user);
		
		long oid = -1;
		String hash = Hash.getMD5Hash(sourceFile);
		List<JPADataSetLO> sameHashDS = DAOs.dataSetDAO.getByHash(hash);
		if (sameHashDS.size() > 0) {
			oid = sameHashDS.get(0).getOID();
		} else {
			try {
				oid = PostgreLobAccess.saveFileToDB(sourceFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JPAFilemapping fm=new JPAFilemapping();
		fm.setExternalfilename(sourceFile.getName());
		fm.setInternalfilename(hash);
		fm.setUser(user);
		DAOs.filemappingDAO.storeEntity(fm);
		newDSLO.setHash(hash);
		newDSLO.setOID(oid);
		newDSLO.setSize(sourceFile.length());
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
