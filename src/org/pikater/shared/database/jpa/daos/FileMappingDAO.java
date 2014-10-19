package org.pikater.shared.database.jpa.daos;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAUser;

public class FileMappingDAO extends AbstractDAO<JPAFilemapping> {

	public FileMappingDAO() {
		super(JPAFilemapping.class);
	}

	@Override
	public String getEntityName() {
		return JPAFilemapping.EntityName;
	}

	public String getSingleExternalFilename(JPADataSetLO dslo) {
		try {
			return DAOs.filemappingDAO.getByInternalFilename(dslo.getHash()).get(0).getExternalfilename();
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return null;
		}
	}

	public List<JPAFilemapping> getByInternalFilename(String internalFilename) {
		return getByTypedNamedQuery("FileMapping.getByInternalFileName", "internalFilename", internalFilename);
	}

	public boolean fileExists(String internalFilename) {
		return !getByInternalFilename(internalFilename).isEmpty();
	}

	public List<JPAFilemapping> getByExternalFilename(String externalFilename) {
		return getByTypedNamedQuery("FileMapping.getByExternalFileName", "externalFilename", externalFilename);
	}

	public List<JPAFilemapping> getByUser(JPAUser user) {
		return getByTypedNamedQuery("FileMapping.getByUser", "user", user);
	}

	public List<JPAFilemapping> getByUserID(int userID) {
		JPAUser user = DAOs.userDAO.getByID(userID);
		if (user != null) {
			return getByTypedNamedQuery("FileMapping.getByUser", "user", user);
		} else {
			return new ArrayList<JPAFilemapping>();
		}
	}

	public List<JPAFilemapping> getByUserIDandInternalFilename(int userID, String internalFilename) {
		JPAUser user = DAOs.userDAO.getByID(userID);
		if (user != null) {
			return getByTypedNamedQueryDouble("FileMapping.getByUserAndInternalFileName", "user", user, "internalFilename", internalFilename);
		} else {
			return new ArrayList<JPAFilemapping>();
		}
	}

	public List<JPAFilemapping> getByUserIDandExternalFilename(int userID, String externalFilename) {
		JPAUser user = DAOs.userDAO.getByID(userID);
		if (user != null) {
			return getByTypedNamedQueryDouble("FileMapping.getByUserAndExternalFileName", "user", user, "externalFilename", externalFilename);
		} else {
			return new ArrayList<JPAFilemapping>();
		}
	}
}
