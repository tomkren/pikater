package org.pikater.shared.utilities.pikaterDatabase.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.utilities.pikaterDatabase.newDB.AbstractDAO;
import org.pikater.shared.utilities.pikaterDatabase.newDB.EntityManagerInstancesCreator;

public class RoleDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return (new JPARole()).getEntityName();
	}

	@Override
	public List<JPARole> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Role.getAll", JPARole.class)
				.getResultList();
	}

	@Override
	public List<JPARole> getByID(int ID) {
		return getByTypedNamedQuery("Role.getByID", "id", ID);
	}
	
	public List<JPARole> getByName(String name) {
		return getByTypedNamedQuery("Role.getByName", "name", name);
	}
	
	private List<JPARole> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPARole.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}

	
}
