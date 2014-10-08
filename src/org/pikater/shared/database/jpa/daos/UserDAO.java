package org.pikater.shared.database.jpa.daos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.status.JPAUserStatus;
import org.pikater.shared.database.security.bcrypt.BCrypt;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.SortOrder;
import org.pikater.shared.database.views.tableview.users.UsersTableDBView;
import org.pikater.shared.logging.database.PikaterDBLogger;

public class UserDAO extends AbstractDAO<JPAUser>{
	
	public UserDAO(){
		super(JPAUser.class);
	}
	
	@Override
	public String getEntityName() {
		return JPAUser.EntityName;
	}
	
	public List<JPAUser> getAll(int offset, int maxQuerySize){
		TypedQuery<JPAUser> tq=EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("User.getAll", JPAUser.class);
		tq.setFirstResult(offset);
		tq.setMaxResults(maxQuerySize);
		return tq.getResultList();
	}
	
	protected Path<Object> convertColumnToJPAParam(ITableColumn column){
		Root<JPAUser> root = getRoot();
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
	
	public List<JPAUser> getAll(int offset, int maxResultCount, ITableColumn sortColumn,SortOrder sortOrder) {
		return getByCriteriaQuery(sortColumn, sortOrder, offset, maxResultCount);
	}
	
	public int getAllCount(){
		return getByCountQuery("User.getAll.count");
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
					PikaterDBLogger.logThrowable(sb.toString(), new IllegalStateException());
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
	
	public void deleteUserEntity(JPAUser user){
		this.deleteUserByID(user.getId());
	}
	
	public void deleteUserByID(int id){
		this.deleteEntityByID(id);
	}
	
	/**
	 * Sets new pasword to the given entity, doesn't update it in DB. 
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

	private static String hashPw(String pwToHash)
	{
		return BCrypt.hashpw(pwToHash, BCrypt.gensalt(hashSalt));
	}
}
