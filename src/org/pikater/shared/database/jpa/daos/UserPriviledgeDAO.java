package org.pikater.shared.database.jpa.daos;

import org.pikater.shared.database.jpa.JPAUserPriviledge;
import org.pikater.shared.database.util.CustomActionResultFormatter;

public class UserPriviledgeDAO extends AbstractDAO<JPAUserPriviledge> {

	public UserPriviledgeDAO() {
		super(JPAUserPriviledge.class);
	}

	@Override
	public String getEntityName() {
		return JPAUserPriviledge.ENTITYNAME;
	}

	public JPAUserPriviledge getByName(String name) {
		return new CustomActionResultFormatter<JPAUserPriviledge>(getByTypedNamedQuery("UserPriviledge.getByName", "name", name)).getSingleResultWithNull();
	}

}
