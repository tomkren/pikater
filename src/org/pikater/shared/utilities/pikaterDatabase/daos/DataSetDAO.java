package org.pikater.shared.utilities.pikaterDatabase.daos;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.utilities.pikaterDatabase.daos.utils.DBFiles;
import org.pikater.shared.utilities.pikaterDatabase.daos.utils.Hash;
import org.pikater.shared.utilities.pikaterDatabase.newDB.AbstractDAO;
import org.pikater.shared.utilities.pikaterDatabase.newDB.EntityManagerInstancesCreator;

public class DataSetDAO extends AbstractDAO{

	@Override
	public String getEntityName() {
		return (new JPADataSetLO()).getEntityName();
	}

	@Override
	public List<JPADataSetLO> getAll() {
		return EntityManagerInstancesCreator
		.getEntityManagerInstance()
		.createNamedQuery("DataSetLO.getAll", JPADataSetLO.class)
		.getResultList();
	}

	@Override
	public List<JPADataSetLO> getByID(int ID) {
		return getByTypedNamedQuery("DataSetLO.getByID", "id", ID);
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
	
	public void storeNewDataSet(File dataset,JPADataSetLO initialData) throws IOException{
		long oid = -1;
		String hash = Hash.getMD5Hash(dataset);
		List<JPADataSetLO> sameHashDS = DAOs.dataSetDAO.getByHash(hash);
		if (sameHashDS.size() > 0) {
			oid = sameHashDS.get(0).getOID();
		} else {
			try {
				oid = DBFiles.saveFileToDB(dataset);
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
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't update JPA DataSetLO object.", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}

}
