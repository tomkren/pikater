package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.util.CustomActionResultFormatter;
import org.pikater.shared.database.util.ResultFormatter;

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
	public JPAGlobalMetaData getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPAGlobalMetaData>(
				getByTypedNamedQuery("GlobalMetaData.getByID", "id", ID),
				era
				).getSingleResultWithNull();
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
