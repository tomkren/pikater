package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.SortOrder;
import org.pikater.shared.database.views.tableview.externalagents.ExternalAgentTableDBView;

public class ExternalAgentDAO extends AbstractDAO<JPAExternalAgent> {
	
	public ExternalAgentDAO(){
		super(JPAExternalAgent.class);
	}
	
	public JPAExternalAgent getByClass(String cls) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			JPAExternalAgent res = null;
			List<JPAExternalAgent> results = em
				.createNamedQuery("ExternalAgent.getByClass",JPAExternalAgent.class)
				.setParameter("class", cls)
				.getResultList();
			if (!results.isEmpty()) {
				res = results.get(0);
			}
			return res;
		}finally{
			em.close();
		}
	}

	@Override
	public String getEntityName() {
		return JPAExternalAgent.EntityName;
	}
	
	private Path<Object> convertColumnToJPAParam(Root<JPAExternalAgent> root,ITableColumn column){
		switch((ExternalAgentTableDBView.Column)column){
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
	
	public List<JPAExternalAgent> getAll(int offset, int maxResults, ITableColumn sortColumn, SortOrder sortOrder) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPAExternalAgent> q=cb.createQuery(JPAExternalAgent.class);
		Root<JPAExternalAgent> c=q.from(JPAExternalAgent.class);
		q.select(c);
		switch (sortOrder) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPAExternalAgent> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	public List<JPAExternalAgent> getByVisibility(int offset, int maxResults, ITableColumn sortColumn, SortOrder sortOrder, boolean  agentVisibility) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPAExternalAgent> q=cb.createQuery(JPAExternalAgent.class);
		Root<JPAExternalAgent> c=q.from(JPAExternalAgent.class);
		q.select(c);
		q.where(cb.equal(c.get("visible"), agentVisibility));
		switch (sortOrder) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPAExternalAgent> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	public int getByVisibilityCount(boolean agentVisibility){
		return ((Long)EntityManagerInstancesCreator
		.getEntityManagerInstance()
		.createNamedQuery("ExternalAgent.getByVisibility.count")
		.setParameter("visibility", agentVisibility)
		.getSingleResult())
		.intValue();
	}

	public JPAExternalAgent getByAgentClass(String agentClass){
		return getSingleResultByTypedNamedQuery("ExternalAgent.getByAgentClass", "agentClass", agentClass);
	}
	
	public List<JPAExternalAgent> getByOwner(JPAUser owner, int offset, int maxResults, ITableColumn sortColumn, SortOrder sortOrder,boolean agentVisibility) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPAExternalAgent> q=cb.createQuery(JPAExternalAgent.class);
		Root<JPAExternalAgent> c=q.from(JPAExternalAgent.class);
		q.select(c);
		q.where(
				cb.and(
						cb.equal(c.get("owner"), owner),
						cb.equal(c.get("visible"), agentVisibility)
						)
				);
		switch (sortOrder) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPAExternalAgent> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	public int getAllCount(){
		return ((Long)EntityManagerInstancesCreator
		.getEntityManagerInstance()
		.createNamedQuery("ExternalAgent.getAll.count")
		.getSingleResult())
		.intValue();
	}
	
	public List<JPAExternalAgent> getAll(int offset,int maxResultSize){
		TypedQuery<JPAExternalAgent> tq=EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("ExternalAgent.getAll", JPAExternalAgent.class);
		tq.setMaxResults(maxResultSize);
		tq.setFirstResult(offset);
		return tq.getResultList();
	}

	public List<JPAExternalAgent> getByOwner(JPAUser user) {
		return getByTypedNamedQuery("ExternalAgent.getByOwner", "owner", user);
	}
	
	public int getByOwnerCount(JPAUser user){
		return ((Long)EntityManagerInstancesCreator
		.getEntityManagerInstance()
		.createNamedQuery("ExternalAgent.getByOwner.count")
		.setParameter("owner", user)
		.getSingleResult())
		.intValue();
	}
	
	public int getByOwnerAndVisibilityCount(JPAUser user,boolean agentVisibility){
		return ((Long)EntityManagerInstancesCreator
		.getEntityManagerInstance()
		.createNamedQuery("ExternalAgent.getByOwnerAndVisibility.count")
		.setParameter("owner", user)
		.setParameter("visibility", agentVisibility)
		.getSingleResult())
		.intValue();
	}
	
	public List<JPAExternalAgent> getByOwner(JPAUser user,int offset,int maxResultSize){
		return getByTypedNamedQuery("ExternalAgent.getByOwner", "owner", user,offset,maxResultSize);
	}
	
	public void deleteExternalAgentEntity(JPAExternalAgent externalAgent){
		this.deleteExternalAgentByID(externalAgent.getId());
	}
	
	public void deleteExternalAgentByID(int id){
		this.deleteEntityByID(id);
	}
	
}
