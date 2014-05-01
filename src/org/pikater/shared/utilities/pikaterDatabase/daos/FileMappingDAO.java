package org.pikater.shared.utilities.pikaterDatabase.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.utilities.pikaterDatabase.newDB.AbstractDAO;
import org.pikater.shared.utilities.pikaterDatabase.newDB.EntityManagerInstancesCreator;

public class FileMappingDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return (new JPAFilemapping()).getEntityName();
	}

	@Override
	public List<JPAFilemapping> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("FileMapping.getAll", JPAFilemapping.class)
				.getResultList();
	}

	@Override
	public List<JPAFilemapping> getByID(int ID) {
		return getByTypedNamedQuery("FileMapping.getByID", "id", ID);
	}
	
	public List<JPAFilemapping> getByInternalFilename(String internalFilename) {
		return getByTypedNamedQuery("FileMapping.getByInternalFileName", "internalFilename", internalFilename);
	}
	
	public boolean fileExists(String internalFilename){
		return getByInternalFilename(internalFilename).size()>0;
	}
	
	public List<JPAFilemapping> getByExternalFilename(String externalFilename) {
		return getByTypedNamedQuery("FileMapping.getByExternalFileName", "externalFilename", externalFilename);
	}
	
	public List<JPAFilemapping> getByUser(JPAUser user) {
		return getByTypedNamedQuery("FileMapping.getByUser", "user", user);
	}
	
	public List<JPAFilemapping> getByUserID(int userID) {
		List<JPAUser> users=DAOs.userDAO.getByID(userID);
		if(users.size()>0){
			return getByTypedNamedQuery("FileMapping.getByUser", "user", users.get(0));
		}else{
			return null;
		}
	}
	
	private List<JPAFilemapping> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAFilemapping.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}

	
}
