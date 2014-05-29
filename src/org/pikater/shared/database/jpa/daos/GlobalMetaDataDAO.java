package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.utils.ResultFormatter;

public class GlobalMetaDataDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return JPAGlobalMetaData.EntityName;
	}

	@Override
	public List<JPAGlobalMetaData> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("GlobalMetaData.getAll", JPAGlobalMetaData.class)
				.getResultList();
	}

	@Override
	public JPAGlobalMetaData getByID(int ID) {
		return new ResultFormatter<JPAGlobalMetaData>(
				getByTypedNamedQuery("GlobalMetaData.getByID", "id", ID)
				).getSingleResultWithNull();
	}
	
	@Override
	public JPAGlobalMetaData getByIDWithException(int ID)
			throws NoResultException {
		return new ResultFormatter<JPAGlobalMetaData>(
				getByTypedNamedQuery("GlobalMetaData.getByID", "id", ID)
				).getSingleResult();
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
