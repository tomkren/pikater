package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.SortOrder;
import org.pikater.shared.database.views.tableview.externalagents.ExternalAgentTableDBView;

public class ExternalAgentDAO extends AbstractDAO<JPAExternalAgent> {

	public ExternalAgentDAO() {
		super(JPAExternalAgent.class);
	}

	@Override
	public String getEntityName() {
		return JPAExternalAgent.ENTITYNAME;
	}

	protected Path<Object> convertColumnToJPAParam(ITableColumn column) {
		Root<JPAExternalAgent> root = getRoot();
		switch ((ExternalAgentTableDBView.Column) column) {
		case CREATED:
		case DESCRIPTION:
		case NAME:
			return root.get(column.toString().toLowerCase());
		case AGENT_CLASS:
			return root.get("agentClass");
		case OWNER:
			return root.get("owner").get("login");
		default:
			return root.get("created");
		}
	}

	public List<JPAExternalAgent> getAll(int offset, int maxResultCount, ITableColumn sortColumn, SortOrder sortOrder) {
		return getByCriteriaQuery(sortColumn, sortOrder, offset, maxResultCount);
	}

	private Predicate createByVisibilityPredicate(boolean agentVisibility) {
		return getCriteriaBuilder().equal(getRoot().get("visible"), agentVisibility);
	}

	public List<JPAExternalAgent> getByVisibility(int offset, int maxResultCount, ITableColumn sortColumn, SortOrder sortOrder, boolean agentVisibility) {
		return getByCriteriaQuery(createByVisibilityPredicate(agentVisibility), sortColumn, sortOrder, offset, maxResultCount);
	}

	public int getByVisibilityCount(boolean agentVisibility) {
		return getByCriteriaQueryCount(createByVisibilityPredicate(agentVisibility));
	}

	private Predicate createByOwnerAndVisibilityPredicate(JPAUser owner, boolean agentVisibility) {
		return getCriteriaBuilder().and(getCriteriaBuilder().equal(getRoot().get("owner"), owner), getCriteriaBuilder().equal(getRoot().get("visible"), agentVisibility));
	}

	public List<JPAExternalAgent> getByOwnerAndVisibility(JPAUser owner, int offset, int maxResultCount, ITableColumn sortColumn, SortOrder sortOrder, boolean agentVisibility) {
		return getByCriteriaQuery(createByOwnerAndVisibilityPredicate(owner, agentVisibility), sortColumn, sortOrder, offset, maxResultCount);
	}

	public int getByOwnerAndVisibilityCount(JPAUser owner, boolean agentVisibility) {
		return getByCriteriaQueryCount(createByOwnerAndVisibilityPredicate(owner, agentVisibility));
	}

	public int getAllCount() {
		return ((Long) EntityManagerInstancesCreator.getEntityManagerInstance().createNamedQuery("ExternalAgent.getAll.count").getSingleResult()).intValue();
	}

	public List<JPAExternalAgent> getAll(int offset, int maxResultSize) {
		TypedQuery<JPAExternalAgent> tq = EntityManagerInstancesCreator.getEntityManagerInstance().createNamedQuery("ExternalAgent.getAll", JPAExternalAgent.class);
		tq.setMaxResults(maxResultSize);
		tq.setFirstResult(offset);
		return tq.getResultList();
	}

	public List<JPAExternalAgent> getByOwner(JPAUser user) {
		return getByTypedNamedQuery("ExternalAgent.getByOwner", "owner", user);
	}

	public int getByOwnerCount(JPAUser user) {
		return ((Long) EntityManagerInstancesCreator.getEntityManagerInstance().createNamedQuery("ExternalAgent.getByOwner.count").setParameter("owner", user).getSingleResult()).intValue();
	}

	public JPAExternalAgent getByAgentClass(String agentClass) {
		return getSingleResultByTypedNamedQuery("ExternalAgent.getByAgentClass", "agentClass", agentClass);
	}

	public void deleteExternalAgentEntity(JPAExternalAgent externalAgent) {
		this.deleteExternalAgentByID(externalAgent.getId());
	}

	public void deleteExternalAgentByID(int id) {
		this.deleteEntityByID(id);
	}

}
