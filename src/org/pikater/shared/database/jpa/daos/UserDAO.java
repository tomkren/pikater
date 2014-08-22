package org.pikater.shared.database.jpa.daos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.status.JPAUserStatus;
import org.pikater.shared.database.security.bcrypt.BCrypt;
import org.pikater.shared.database.util.CustomActionResultFormatter;
import org.pikater.shared.database.views.base.SortOrder;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.users.UsersTableDBView;
import org.pikater.shared.logging.PikaterLogger;

public class UserDAO extends AbstractDAO
{
	@Override
	public String getEntityName() {
		return JPAUser.EntityName;
	}

	@Override
	public List<JPAUser> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("User.getAll", JPAUser.class)
				.getResultList();
	}
	
	public List<JPAUser> getAll(int offset, int maxQuerySize){
		TypedQuery<JPAUser> tq=EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("User.getAll", JPAUser.class);
		tq.setFirstResult(offset);
		tq.setMaxResults(maxQuerySize);
		return tq.getResultList();
	}
	
	private Path<Object> convertColumnToJPAParam(Root<JPAUser> root,ITableColumn column){
		switch((UsersTableDBView.Column)column){
		case LOGIN:
		case STATUS:
		case EMAIL:
			return root.get(column.toString().toLowerCase());
		case REGISTERED:
			return root.get("created");
		case MAX_PRIORITY:
			return root.get("priorityMax");
		case ADMIN:
			return root.get("role").get("role");
		default:
			return root.get("login");
		}
	}
	
	public List<JPAUser> getAll(int offset, int maxResults, ITableColumn sortColumn,SortOrder order) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<JPAUser> q=cb.createQuery(JPAUser.class);
		Root<JPAUser> c=q.from(JPAUser.class);
		q.select(c);
		switch (order) {
		case ASCENDING:
			q.orderBy(cb.asc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		case DESCENDING:
			q.orderBy(cb.desc(this.convertColumnToJPAParam(c, sortColumn)));
			break;
		default:
			break;
		}
		
		TypedQuery<JPAUser> query=
				em.createQuery(q)
				.setFirstResult(offset)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	
	public int getAllCount(){
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("User.getAll.count").getSingleResult()).intValue();
	}

	@Override
	public JPAUser getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPAUser>(
				getByTypedNamedQuery("User.getByID", "id", ID),
				era)
				.getSingleResultWithNull();
	}
	
	public List<JPAUser> getByStatus(JPAUserStatus status) {
		return getByTypedNamedQuery("User.getByStatus", "status", status);
	}
	
	public List<JPAUser> getByLogin(String login) {
		return getByTypedNamedQuery("User.getByLogin", "login", login);
	}
	
	public JPAUser getByLoginAndPassword(String login, String password)
	{
		List<JPAUser> usersByLogin = getByLogin(login);
		if((usersByLogin != null) && (!usersByLogin.isEmpty()))
		{
			List<JPAUser> resultUsers = new ArrayList<JPAUser>();
			for(JPAUser user : usersByLogin)
			{
				if(BCrypt.checkpw(password, user.getPassword()))
				{
					resultUsers.add(user);
				}
			}
			switch(resultUsers.size())
			{
				case 0:
					return null;
				case 1:
					return resultUsers.get(0);
				default:
					// TODO: trigger a cleanup cron or hunt bugs?
					StringBuilder sb = new StringBuilder();
					sb.append("Several user accounts with the same credentials were found:\n");
					for(JPAUser user : resultUsers)
					{
						sb.append(String.format("- ID: %d", user.getId()));
					}
					PikaterLogger.logThrowable(sb.toString(), new IllegalStateException());
					return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	public List<JPAUser> getByRole(JPARole role) {
		return getByTypedNamedQuery("User.getByRole", "role", role);
	}
	
	private List<JPAUser> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAUser.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}

	public void updateEntity(JPAUser changedEntity){
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPAUser item=em.find(JPAUser.class, changedEntity.getId());
			item.updateValues(changedEntity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't update JPA User object.", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
	public void deleteUserEntity(JPAUser user){
		this.deleteUserByID(user.getId());
	}
	
	public void deleteUserByID(int id){
		this.deleteEntityByID(JPAUser.class, id);
	}
	
	/**
	 * Sets new pasword to the given entity, doesn't update it in DB. 
	 * @param user
	 * @return the new password in plain text
	 */
	public String resetPasswordButDontUpdate(JPAUser user)
	{
		String newPassword = UUID.randomUUID().toString();
		user.setPassword(hashPw(newPassword));
		return newPassword;
	}
	
	@Override
	public void storeEntity(Object newEntity)
	{
		JPAUser user = (JPAUser) newEntity;
		user.setPassword(hashPw(user.getPassword()));
		super.storeEntity(user);
	}
	
	// ------------------------------------------------------------------------------------------------
	// PASSWORD HASHING INTERFACE

	private static int hashSalt = 13;

	public static String hashPw(String pwToHash)
	{
		return BCrypt.hashpw(pwToHash, BCrypt.gensalt(hashSalt));
	}
}