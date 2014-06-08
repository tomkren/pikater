package org.pikater.shared.database.jpa.daos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.jpa.JpaCache;
import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.pglargeobject.PostgreLobAccess;
import org.pikater.shared.database.utils.CustomActionResultFormatter;
import org.pikater.shared.database.utils.Hash;
import org.pikater.shared.database.utils.ResultFormatter;

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
		return null;
	}
	
	public List<JPADataSetLO> getAllWithResultsExcludingHashes(List<String> hashesToBeExcluded){
		return null;
	}
	
	public void storeNewDataSet(File dataset,JPADataSetLO initialData) throws IOException{
		long oid = -1;
		String hash = Hash.getMD5Hash(dataset);
		List<JPADataSetLO> sameHashDS = DAOs.dataSetDAO.getByHash(hash);
		if (sameHashDS.size() > 0) {
			oid = sameHashDS.get(0).getOID();
		} else {
			try {
				oid = PostgreLobAccess.saveFileToDB(dataset);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		initialData.setHash(hash);
		initialData.setOID(oid);
		initialData.setSize(dataset.length());
		storeEntity(initialData);
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

}
