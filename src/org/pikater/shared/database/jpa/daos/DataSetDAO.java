package org.pikater.shared.database.jpa.daos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
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
import org.pikater.shared.database.util.Hash;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.SortOrder;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBView;
import org.pikater.shared.logging.database.PikaterDBLogger;

public class DataSetDAO extends AbstractDAO<JPADataSetLO>{

	public DataSetDAO(){
		super(JPADataSetLO.class);
	}
	
	@Override
	public String getEntityName() {
		return JPADataSetLO.EntityName;
	}
	
	protected Path<Object> convertColumnToJPAParam(ITableColumn column){
		Root<JPADataSetLO> root=getRoot();
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
	
	private Predicate createAllUserUploadPredicate(){
		return getCriteriaBuilder()
				.equal(getRoot().get("source"), JPADatasetSource.USER_UPLOAD);
	}
	
	/**
	 * Creates of the list of all datasets, that were uploaded by users. The result doesn't depend on dataset visibility.
	 * @param offset the position from which the elements are returned
	 * @param maxResultCount maximum number of retrieved elements
	 * @param sortColumn column upon which the result is sorted
	 * @param sortOrder ascending or descending order of sorting
	 * @return the list of all user uploaded datasets
	 */
	public List<JPADataSetLO> getAllUserUpload(int offset, int maxResultCount,ITableColumn sortColumn, SortOrder sortOrder) {
		return getByCriteriaQuery(
				createAllUserUploadPredicate(),
				sortColumn,
				sortOrder,
				offset,
				maxResultCount);
	}
	
	/**
	 * Computes the number of all user uploaded datasets/
	 * @return the number of datasets
	 */
	public int getAllUserUploadCount(){
		return getByCriteriaQueryCount(createAllUserUploadPredicate());
	}
	
	
	private Predicate createUserUploadVisiblePredicate(){
		return getCriteriaBuilder()
				.and(
					getCriteriaBuilder().equal(getRoot().get("source"), JPADatasetSource.USER_UPLOAD),
					getCriteriaBuilder().equal(getRoot().get("visible"), true)
					);
	}
	
	/**
	 * Creates a list of All datasets, that were uploaded by user and are visible to users
	 * @param offset the position from which the elements are returned
	 * @param maxResultCount maximum number of retrieved elements
	 * @param sortColumn column upon which the result is sorted
	 * @param sortOrder ascending or descending order of sorting
	 * @return the list of all user uploaded visible datasets
	 */
	public List<JPADataSetLO> getUserUploadVisible(int offset, int maxResultCount,ITableColumn sortColumn, SortOrder sortOrder) {
		return this.getByCriteriaQuery(createUserUploadVisiblePredicate(), sortColumn, sortOrder, offset, maxResultCount);
	}
	
	/**
	 * Computes the number of all user uploaded visible datasets
	 * @return the number of datasets
	 */
	public int getUserUploadVisibleCount(){
		return getByCriteriaQueryCount(createUserUploadVisiblePredicate());
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
	
	private Predicate createByOwnerUserUploadVisiblePredicate(JPAUser owner){
		CriteriaBuilder cb=getCriteriaBuilder();
		Predicate pred=
				cb.equal(
					getRoot().get("source"),
					JPADatasetSource.USER_UPLOAD
					);
		pred=cb.and(
				pred,
				cb.equal(
					getRoot().get("owner"),
					owner)
				);
		pred=cb.and(
				pred,
				cb.equal(
					getRoot().get("visible"),
					true)
				);
		
		return pred;
	}
	
	/**
	 * Creates a list of datasets, which have the following properties:
	 * <p>
	 * - dataset's owner is equals to the {@link JPAUser} parameter
	 * <p>
	 * - dataset is visible
	 * <p>
	 * - dataset was uploaded by the user (not by an experiment, which has run under user's name)
	 * @param owner the owner of the dataset
	 * @param offset the position from which the elements are returned
	 * @param maxResultCount maximum number of retrieved elements
	 * @param sortColumn column upon which the result is sorted
	 * @param sortOrder ascending or descending order of sorting
	 * @return the list of user's datasets
	 */
	public List<JPADataSetLO> getByOwnerUserUploadVisible(JPAUser owner, int offset, int maxResultCount, ITableColumn sortColumn, SortOrder sortOrder) {
		return getByCriteriaQuery(createByOwnerUserUploadVisiblePredicate(owner), sortColumn, sortOrder, offset, maxResultCount);
	}
	
	/**
	 * Computes the number of datasets for the given user, that are visible and were uploaded by the user
	 * @param owner the owner of datasets
	 * @return the number of datasets
	 */
	public int getByOwnerUserUploadVisibleCount(JPAUser owner){
		return getByCriteriaQueryCount(createByOwnerUserUploadVisiblePredicate(owner));
	}
	
	/**
	 * Retrieves all DataSetLO entities from database, which have hash different from any entry
	 *  in hashesToBeExcluded list and were uploaded by user.
	 * @param hashesToBeExcluded list of dataset hashes, that have to be omitted
	 * @return list of {@link JPADataSetLO} objects
	 */
	public List<JPADataSetLO> getAllExcludingHashesWithMetadata(List<String> hashesToBeExcluded){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb= em.getCriteriaBuilder();
		CriteriaQuery<JPADataSetLO> cq=cb.createQuery(JPADataSetLO.class);
		Root<JPADataSetLO> r=cq.from(JPADataSetLO.class);
		
		Predicate p=cb.conjunction();
		for(String exHash:hashesToBeExcluded){
			p=cb.and(p,cb.equal(r.get("hash"),exHash).not());
		}
		p=cb.and(p,cb.isNotNull(r.get("globalMetaData")));
		
		//we want user uploaded datasets
		p=cb.and(p, cb.equal(r.get("source"), JPADatasetSource.USER_UPLOAD));
		
		cq=cq.where(p);
		List<JPADataSetLO> res=em.createQuery(cq).getResultList();
		
		return res;
	}

	public List<JPADataSetLO> getByOwner(JPAUser user) {
		return getByTypedNamedQuery("DataSetLO.getByOwner", "owner", user);
	}
	
	public int getByOwnerCount(JPAUser user){
		return getByCountQuery("DataSetLO.getByOwner.count", "owner", user);
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
		return getByTypedNamedQuery("DataSetLO.getAllWithResults");
	}
	
	/**
	 * Retrieves all DataSetLO entities from database, for which some results exist
	 * in the database, have hash different from any entry in hashesToBeExcluded list 
	 * and were uploaded by user.
	 * @param hashesToBeExcluded list of dataset hashes, that have to be omitted
	 * @return list of {@link JPADataSetLO} objects
	 */
	public List<JPADataSetLO> getAllWithResultsExcludingHashesWithMetadata(List<String> hashesToBeExcluded){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb= em.getCriteriaBuilder();
		
		CriteriaQuery<JPADataSetLO> cq=cb.createQuery(JPADataSetLO.class);
		Root<JPADataSetLO> r=cq.from(JPADataSetLO.class);
		
		Predicate p=cb.conjunction();
		for(String exHash:hashesToBeExcluded){
			p=cb.and(p,cb.equal(r.get("hash"),exHash).not());
		}
		p=cb.and(p,cb.isNotNull(r.get("globalMetaData")));
		
		//we want user uploaded datasets
		p=cb.and(p,cb.equal(r.get("source"), JPADatasetSource.USER_UPLOAD));
		
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
	 * @param realFilename file name that overrides the 'sourceFile' temporary name
	 * @param description
	 * @param userID
	 * @param datasetSource
	 * @return ID of the new {@link JPADataSetLO} object
	 * @throws IOException
	 */
	public int storeNewDataSet(File sourceFile, String realFilename, String description, int userID, JPADatasetSource datasetSource) throws IOException{
			
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
				PikaterDBLogger.logThrowable("Unexpected error occured:", e);
			}
		}
		JPAFilemapping fm=new JPAFilemapping();
		fm.setExternalfilename(realFilename);
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
				/*
				 System.err.println("No. of attr metadatas : "+changedEntity.getAttributeMetaData().size());
				 for(JPAAttributeMetaData amd : changedEntity.getAttributeMetaData())
				 {
					System.err.println(amd+"   ID: "+amd.getId());
				 }
				 */
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
			PikaterDBLogger.logThrowable("Can't update JPA DataSetLO object.", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
	public void deleteDataSetEntity(JPADataSetLO datasetlo){
		this.deleteDatasetByID(datasetlo.getId());
	}
	
	public void deleteDatasetByID(int id){
		this.deleteEntityByID(id);
	}

}
