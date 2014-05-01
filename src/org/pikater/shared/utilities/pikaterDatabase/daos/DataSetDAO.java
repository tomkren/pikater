package org.pikater.shared.utilities.pikaterDatabase.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAUser;
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

}
