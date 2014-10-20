package org.pikater.shared.database.jpa.daos;

import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.PikaterRole;
import org.pikater.shared.database.util.CustomActionResultFormatter;

public class RoleDAO extends AbstractDAO<JPARole> {

	public RoleDAO() {
		super(JPARole.class);
	}

	@Override
	public String getEntityName() {
		return JPARole.ENTITYNAME;
	}

	public JPARole getByPikaterRole(PikaterRole role) {
		return new CustomActionResultFormatter<JPARole>(getByTypedNamedQuery("Role.getByPikaterRole", "pRole", role)).getSingleResultWithNull();
	}

	public JPARole getByName(String name) {
		return new CustomActionResultFormatter<JPARole>(getByTypedNamedQuery("Role.getByName", "name", name)).getSingleResultWithNull();
	}

}
