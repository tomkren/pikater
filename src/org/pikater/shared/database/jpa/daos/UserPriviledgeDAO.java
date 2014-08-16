package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAUserPriviledge;
import org.pikater.shared.database.util.CustomActionResultFormatter;
import org.pikater.shared.database.util.ResultFormatter;

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
	public JPAUserPriviledge getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPAUserPriviledge>(
				getByTypedNamedQuery("UserPriviledge.getByID", "id", ID),
				era
				).getSingleResultWithNull();
	}
	
	public JPAUserPriviledge getByName(String name) {
		return new CustomActionResultFormatter<JPAUserPriviledge>(
				getByTypedNamedQuery("UserPriviledge.getByName", "name", name),
				EmptyResultAction.getDefault()
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
