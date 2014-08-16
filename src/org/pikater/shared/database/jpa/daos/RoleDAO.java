package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.security.PikaterRole;
import org.pikater.shared.database.util.CustomActionResultFormatter;
import org.pikater.shared.database.util.ResultFormatter;

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
	public JPARole getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPARole>(
				getByTypedNamedQuery("Role.getByID", "id", ID),
				era
				).getSingleResultWithNull();
	}
	
	public JPARole getByPikaterRole(PikaterRole role){
		return new CustomActionResultFormatter<JPARole>(
				getByTypedNamedQuery("Role.getByPikaterRole", "pRole", role),
				EmptyResultAction.NULL
				).getSingleResultWithNull();
	}
	
	public JPARole getByName(String name) {
		return new CustomActionResultFormatter<JPARole>(
				getByTypedNamedQuery("Role.getByName", "name", name),
				EmptyResultAction.getDefault()
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
