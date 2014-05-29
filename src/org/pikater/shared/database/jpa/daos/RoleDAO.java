package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.security.PikaterRole;
import org.pikater.shared.database.utils.ResultFormatter;

public class RoleDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return JPARole.EntityName;
	}

	@Override
	public List<JPARole> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Role.getAll", JPARole.class)
				.getResultList();
	}

	@Override
	public JPARole getByID(int ID) {
		return new ResultFormatter<JPARole>(
				getByTypedNamedQuery("Role.getByID", "id", ID)
				).getSingleResultWithNull();
	}
	
	@Override
	public JPARole getByIDWithException(int ID)
			throws NoResultException {
		return new ResultFormatter<JPARole>(
				getByTypedNamedQuery("Role.getByID", "id", ID)
				).getSingleResult();
	}
	
	public JPARole getByPikaterRole(PikaterRole role){
		return new ResultFormatter<JPARole>(
				getByTypedNamedQuery("Role.getByPikaterRole", "pRole", role)
				).getSingleResultWithNull();
	}
	
	public JPARole getByName(String name) {
		return new ResultFormatter<JPARole>(
				getByTypedNamedQuery("Role.getByName", "name", name)
				).getSingleResultWithNull();
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
