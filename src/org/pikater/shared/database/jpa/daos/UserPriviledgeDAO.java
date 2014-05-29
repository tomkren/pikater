package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAUserPriviledge;
import org.pikater.shared.database.utils.ResultFormatter;

public class UserPriviledgeDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return JPAUserPriviledge.EntityName;
	}

	@Override
	public List<JPAUserPriviledge> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("UserPriviledge.getAll", JPAUserPriviledge.class)
				.getResultList();
	}

	@Override
	public JPAUserPriviledge getByID(int ID) {
		return new ResultFormatter<JPAUserPriviledge>(
				getByTypedNamedQuery("UserPriviledge.getByID", "id", ID)
				).getSingleResultWithNull();
	}
	
	@Override
	public JPAUserPriviledge getByIDWithException(int ID)
			throws NoResultException {
		return new ResultFormatter<JPAUserPriviledge>(
				getByTypedNamedQuery("UserPriviledge.getByID", "id", ID)
				).getSingleResult();
	}
	
	public JPAUserPriviledge getByName(String name) {
		return new ResultFormatter<JPAUserPriviledge>(
				getByTypedNamedQuery("UserPriviledge.getByName", "name", name)
			   ).getSingleResultWithNull();
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
