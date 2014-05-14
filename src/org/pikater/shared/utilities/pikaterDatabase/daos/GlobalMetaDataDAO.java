package org.pikater.shared.utilities.pikaterDatabase.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.utilities.pikaterDatabase.newDB.EntityManagerInstancesCreator;

public class GlobalMetaDataDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return (new JPAGlobalMetaData()).getEntityName();
	}

	@Override
	public List<JPAGlobalMetaData> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("GlobalMetaData.getAll", JPAGlobalMetaData.class)
				.getResultList();
	}

	@Override
	public List<JPAGlobalMetaData> getByID(int ID) {
		return getByTypedNamedQuery("GlobalMetaData.getByID", "id", ID);
	}
	
	private List<JPAGlobalMetaData> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAGlobalMetaData.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}

	
}
