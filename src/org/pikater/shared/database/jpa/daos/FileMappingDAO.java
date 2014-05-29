package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.utils.ResultFormatter;

public class FileMappingDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return JPAFilemapping.EntityName;
	}

	@Override
	public List<JPAFilemapping> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("FileMapping.getAll", JPAFilemapping.class)
				.getResultList();
	}

	@Override
	public JPAFilemapping getByID(int ID) {
		return new ResultFormatter<JPAFilemapping>(
				getByTypedNamedQuery("FileMapping.getByID", "id", ID)
				).getSingleResultWithNull();
	}
	
	@Override
	public JPAFilemapping getByIDWithException(int ID)
			throws NoResultException {
		return new ResultFormatter<JPAFilemapping>(
				getByTypedNamedQuery("FileMapping.getByID", "id", ID)
				).getSingleResult();
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
		JPAUser user=DAOs.userDAO.getByID(userID);
		if(user!=null){
			return getByTypedNamedQuery("FileMapping.getByUser", "user", user);
		}else{
			return null;
		}
	}
	
	public List<JPAFilemapping> getByUserIDandInternalFilename(int userID,String internalFilename){
		JPAUser user=DAOs.userDAO.getByID(userID);
		if(user!=null){
			return getByTypedNamedQueryDouble(
				"FileMapping.getByUserAndInternalFileName",
				"user",
				user,
				"internalFilename",
				internalFilename);
		}else{
			return null;
		}
	}
	
	public List<JPAFilemapping> getByUserIDandExternalFilename(int userID,String externalFilename){
		JPAUser user=DAOs.userDAO.getByID(userID);
		if(user!=null){
			return getByTypedNamedQueryDouble(
				"FileMapping.getByUserAndExternalFileName",
				"user",
				user,
				"externalFilename",
				externalFilename);
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
	
	private List<JPAFilemapping> getByTypedNamedQueryDouble(String queryName,String paramName1,Object param1,String paramName2,Object param2){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAFilemapping.class)
				.setParameter(paramName1, param1)
				.setParameter(paramName2, param2)
				.getResultList();
		}finally{
			em.close();
		}
	}	
}
