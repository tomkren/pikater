package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.JPAUserPriviledge;

public class UserPriviledgeDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return (new JPAUserPriviledge()).getEntityName();
	}

	@Override
	public List<JPAUserPriviledge> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("UserPriviledge.getAll", JPAUserPriviledge.class)
				.getResultList();
	}

	@Override
	public List<JPAUserPriviledge> getByID(int ID) {
		return getByTypedNamedQuery("UserPriviledge.getByID", "id", ID);
	}
	
	public List<JPAUserPriviledge> getByName(String name) {
		return getByTypedNamedQuery("UserPriviledge.getByName", "name", name);
	}
	
	private List<JPAUserPriviledge> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAUserPriviledge.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}

	
}
