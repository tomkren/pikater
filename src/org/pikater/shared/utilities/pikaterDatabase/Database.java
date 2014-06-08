package org.pikater.shared.utilities.pikaterDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.DigestUtils;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.pikater.shared.database.exceptions.UserNotFoundException;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAGeneralFile;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPATaskType;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.JPAUserPriviledge;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.daos.AbstractDAO.EmptyResultAction;
import org.pikater.shared.database.pglargeobject.PostgreLargeObjectReader;


public class Database {

	public enum PasswordChangeResult {
		Success, Error
	};
	
	public enum ExperimentStatus{
		Waiting,Started,Finished
	};

	PGConnection connection;
	EntityManagerFactory emf = null;
	EntityManager em = null;

	public Database(EntityManagerFactory emf, PGConnection connection) {
		this.emf = emf;
		this.connection = connection;
	}

	
	public void saveBatch (int userId, JPABatch batch, String batchXml) {

		JPAUser user=DAOs.userDAO.getByID(userId, EmptyResultAction.NULL);

		int totalPriority = 10*user.getPriorityMax() + batch.getPriority();

		batch.setXML(batchXml);
		batch.setOwner(user);
		batch.setTotalPriority(totalPriority);

		//TODO:
		//this.persist(batch);

	}

	public void saveExperiment(int batchID, JPAExperiment experiment) {
		// :TODO
	}
	
	public List<JPAExperiment> getExperimentsByStatus(JPAUser user,ExperimentStatus status){
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select exp from JPAExperiment exp where exp.status=:expStatus and exp.user=:expUser");
			q.setParameter("expStatus", status.toString());
			q.setParameter("expUser", user.getLogin());
			List<JPAExperiment> expList = q.getResultList();
			return expList;
		} finally {
			cleanUpEntityManager();
		}
	}
	
	public List<JPAExperiment> getExperiments(JPAUser user){
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select exp from JPAExperiment exp where exp.user=:expUser");
			q.setParameter("expUser", user.getLogin());
			List<JPAExperiment> expList = q.getResultList();
			return expList;
		} finally {
			cleanUpEntityManager();
		}
	}
	
	/**
	public List<JPAResult> getResults(){		
		try{
			TypedQuery<JPAResult> q=em.createNamedQuery("Result.getAll",JPAResult.class);
			return q.getResultList();
		}finally{
			cleanUpEntityManager();
		}
	}
	
	
	public List<JPAResult> getResultsForExperiment(int experimentID){
		try{
			TypedQuery<JPAResult> q=em.createNamedQuery("Result.getByExperiment",JPAResult.class);
			q.setParameter("experiment", experimentID);
			return q.getResultList();
		}finally{
			cleanUpEntityManager();
		}
	}
	
	public List<JPAResult> getResultsByDataSetHash(String hash){
		try{
			TypedQuery<JPAResult> q=em.createNamedQuery("Result.getByDataSetHash",JPAResult.class);
			q.setParameter("hash", hash);
			return q.getResultList();
		}finally{
			cleanUpEntityManager();
		}
	}
	**/
	
	/**
	 * Loads test data to the DataBase
	 * 
	 * @throws IOException
	 * @throws SQLException
	 * @throws UserNotFoundException
	 */
	private void loadTestData() throws SQLException, IOException, UserNotFoundException {

		/**
		this.addRole("user", "Standard user role");
		this.addRole("admin", "Standard administrator role");
**/
		try {
			this.deleteUserByLogin("bs");
		} catch (UserNotFoundException e) {
			System.err.println(e.getMessage());
		}
		try {
			this.deleteUserByLogin("kj");
		} catch (UserNotFoundException e) {
			System.err.println(e.getMessage());
		}
		try {
			this.deleteUserByLogin("sj");
		} catch (UserNotFoundException e) {
			System.err.println(e.getMessage());
		}
		try {
			this.deleteUserByLogin("sp");
		} catch (UserNotFoundException e) {
			System.err.println(e.getMessage());
		}
		try {
			this.deleteUserByLogin("johndoe");
		} catch (UserNotFoundException e) {
			System.err.println(e.getMessage());
		}
/**
		this.addUser("bs", "123", "nassoftwerak@gmail.com", 6);
		this.addUser("kj", "123", "nassoftwerak@gmail.com", 6);
		this.addUser("sj", "123", "nassoftwerak@gmail.com", 6);
		this.addUser("sp", "123", "nassoftwerak@gmail.com", 6);
		this.addUser("johndoe", "123", "nassoftwerak@gmail.com", 3);
**/
		this.setRoleForUser("bs", "admin");
		this.setRoleForUser("kj", "admin");
		this.setRoleForUser("sj", "admin");
		this.setRoleForUser("sp", "admin");
		this.setRoleForUser("johndoe", "user");

		/**
		 * JPAUser john=this.getUserByLogin("johndoe");
		 * this.saveGeneralFile(john.getId(), "First Data File",new File(
		 * "./data/files/25d7d5d689042a3816aa1598d5fd56ef"));
		 * this.saveGeneralFile(john.getId(), "Second Data File",new File(
		 * "./data/files/772c551b8486b932aed784a582b9c1b1"));
		 * this.saveGeneralFile(john.getId(), "Third Data File",new File(
		 * "./data/files/dc7ce6dea5a75110486760cfac1051a5"));
		 **/
	}

	/**
	 * Adds a JPAGlobalMetaData object to the database
	 * 
	 * @param jpaGlobalMetaData
	 *            The object to be stored.
	 */
	public void addGlobalMetaData(JPAGlobalMetaData jpaGlobalMetaData) {
		persist(jpaGlobalMetaData);
	}

	/**
	 * Creates a new instance of JPAGlobalMetaData class for the given
	 * numberOfInstances and defaultTaskTypeName If the defaultTaskTypeName
	 * references a new task type, then a new JPATaskType object will also be
	 * created.
	 * 
	 * @param numberOfInstances
	 *            The number of instances for the new object
	 * @param defaultTaskTypeName
	 *            The name of the dafault task type.
	 * @return
	 */
	public JPAGlobalMetaData createGlobalMetaData(int numberOfInstances, String defaultTaskTypeName) {
		JPATaskType storedTask = this.getTaskTypeByName(defaultTaskTypeName);
		if (storedTask == null) {
			storedTask = this.createTaskType(defaultTaskTypeName);
			this.addTaskType(storedTask);
			storedTask = this.getTaskTypeByName(defaultTaskTypeName);
		}
		JPAGlobalMetaData gMetaData = new JPAGlobalMetaData();
		gMetaData.setDefaultTaskType(storedTask);
		gMetaData.setNumberofInstances(numberOfInstances);
		return gMetaData;
	}

	/**
	 * Creates a new instance of JPAGlobalMetaData class
	 * 
	 * @param numberOfInstances
	 *            The number of instances of the new object
	 * @param defaultTaskType
	 *            The JPATaskType object of the default task type for the new
	 *            object
	 * @return The new object
	 */
	public JPAGlobalMetaData createGlobalMetaData(int numberOfInstances, JPATaskType defaultTaskType) {
		JPAGlobalMetaData res = new JPAGlobalMetaData();
		res.setDefaultTaskType(defaultTaskType);
		res.setNumberofInstances(numberOfInstances);
		return res;
	}

	/**
	 * Creates a new instance of JPATaskType class
	 * 
	 * @param name
	 *            The name of the new task.
	 * @return The new instance
	 */
	public JPATaskType createTaskType(String name) {
		JPATaskType result = new JPATaskType();
		result.setName(name);
		return result;
	}

	/**
	 * Adds a JPATaskType object to the database
	 * 
	 * @param taskType
	 *            The object to be stored.
	 */
	public void addTaskType(JPATaskType taskType) {
		persist(taskType);
	}

	public JPATaskType getTaskTypeByName(String name) {
		JPATaskType res = null;
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select tt from JPATaskType tt where tt.name=:taskTypeName");
			q.setParameter("taskTypeName", name);
			try {
				res = (JPATaskType) q.getSingleResult();
			} catch (javax.persistence.NoResultException nre) {
				res = null;
			}
			return res;
		} finally {
			cleanUpEntityManager();
		}
	}
	
	public JPATaskType getOrCreateTaskTypeByName(String name){
		JPATaskType tt=this.getTaskTypeByName(name);
		if(tt==null){
			return this.createTaskType(name);
		}else{
			return tt;
		}
		
	}

	
	public void addUserPriviledge(JPAUserPriviledge userPriviledge){
		persist(userPriviledge);
	}
	
	/**
	public void addUserPriviledge(String name){
		JPAUserPriviledge up=new JPAUserPriviledge();
		up.setName(name);
		this.addUserPriviledge(up);
	}
	**/
	
	public JPAUserPriviledge getUserPriviledge(String name){
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select up from JPAUserPriviledge up where up.name=:privName");
			q.setParameter("privName", name);
			JPAUserPriviledge res = (JPAUserPriviledge) q.getSingleResult();
			return res;
		} finally {
			cleanUpEntityManager();
		}
	}
	
	/**
	 * Adds an existing role object to the database
	 * 
	 * @param role
	 *            The role's object
	 */
	public void addRole(JPARole role) {
		persist(role);
	}

	/**
	 * Creates a new role with the given name and description. The created role
	 * is saved to the database. The name must be unique in the database, where
	 * to object is intended to be saved.
	 * 
	 * @param name
	 *            The name of the new role
	 * @param description
	 *            The description of the new role
	 */
	/**
	public void addRole(String name, String description) {
		JPARole newRole = new JPARole();
		newRole.setName(name);
		newRole.setRole(description);
		addRole(newRole);
	}
	**/
	
	
	public void addPriviledgeForRole(String roleName,String priviledgeName){		
		JPAUserPriviledge up=this.getUserPriviledge(priviledgeName);
		JPARole role=this.getRoleByName(roleName);
		role.addPriviledge(up);
		persist(role);
	}

	/**
	 * Returns the list of all roles stored in the database.
	 * 
	 * @return The List of roles.
	 */
	public List<JPARole> getRoles() {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select r from JPARole r");
			List<JPARole> roleList = q.getResultList();
			return roleList;
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * Returns a role identified by the given roleName
	 * 
	 * @param roleName
	 *            The role's name
	 * @return The JPA object of role found in the database
	 */
	public JPARole getRoleByName(String roleName) {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select r from JPARole r where r.name=:roleName");
			q.setParameter("roleName", roleName);
			JPARole res = (JPARole) q.getSingleResult();
			return res;
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * For the user with the given loginName sets the role specified by the name
	 * of the role
	 * 
	 * @param login
	 *            The user's login
	 * @param roleName
	 *            The role's name
	 * @throws UserNotFoundException
	 */
	public void setRoleForUser(String login, String roleName) throws UserNotFoundException {
		JPARole role = this.getRoleByName(roleName);
		JPAUser user = this.getUserByLogin(login);
		setRoleForUser(user, role);
	}

	/**
	 * For the user's JPA object sets a role, that is given by its JPA object.
	 * 
	 * @param user
	 *            The user's JPA object
	 * @param role
	 *            The role's JPA object
	 */
	public void setRoleForUser(JPAUser user, JPARole role) {
		role.getUsersWithThisRole().add(user);
		user.setRole(role);
		persist(user);
		persist(role);
	}

	/**
	 * Adds an existing user object to the database
	 * 
	 * @param user
	 *            The user's object.
	 */
	public void addUser(JPAUser user) {
		persist(user);
	}

	/**
	 * Based on the given parameters creates a new user object and stores it to
	 * the database.
	 * 
	 * @param login
	 *            The new user's login name
	 * @param password
	 *            The new user's password
	 * @param email
	 *            The new user's e-mail
	 */
	/**
	public void addUser(String login, String password, String email, String roleName) {
		JPAUser newUser = new JPAUser();
		newUser.setLogin(login);
		newUser.setPassword(password);
		newUser.setEmail(email);
		newUser.setRole(getRoleByName(roleName));
		this.addUser(newUser);
	}
	**/

	/**
	 * Based on the given parameters creates a new user object and stores it to
	 * the database.
	 * 
	 * @param login
	 *            The new user's login name
	 * @param password
	 *            The new user's password
	 * @param email
	 *            The new user's e-mail
	 * @param maxPriority
	 *            The new user's maximal priority
	 */
	/**
	public void addUser(String login, String password, String email, int maxPriority) {
		JPAUser newUser = new JPAUser();
		newUser.setLogin(login);
		newUser.setPassword(password);
		newUser.setEmail(email);
		newUser.setPriorityMax(maxPriority);
		this.addUser(newUser);
	}
**/
	/**
	 * Returns the list of all users stored in the database.
	 * 
	 * @return The List of users.
	 */
	public List<JPAUser> getUsers() {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select u from JPAUser u");
			List<JPAUser> userList = q.getResultList();
			return userList;
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * Returns an user identified by the given ID
	 * 
	 * @param id
	 *            The user id
	 * @return The JPA object of user found in the database
	 */
	public JPAUser getUserByID(long id) {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select u from JPAUser u where u.id=:userId");
			q.setParameter("userId", id);
			return (JPAUser) q.getSingleResult();
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * Returns an user identified by the given LoginName
	 * 
	 * @param login
	 *            The user's login name
	 * @return The JPA object of user found in the database
	 * @throws UserNotFoundException
	 */
	public JPAUser getUserByLogin(String login) throws UserNotFoundException {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select u from JPAUser u where u.login=:userLogin");
			q.setParameter("userLogin", login);
			JPAUser res;
			try {
				res = (JPAUser) q.getSingleResult();
			} catch (javax.persistence.NoResultException nre) {
				throw new UserNotFoundException(login);
			}
			return res;
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * Delete user with the given ID
	 * 
	 * @param id
	 *            The ID of the user to be deleted
	 * @throws UserNotFoundException
	 */
	public void deleteUserByID(int id) throws UserNotFoundException {
		JPAUser user = this.getUserByID(id);
		if (user == null)
			throw new UserNotFoundException(id);
		deleteUser(user);
	}

	/**
	 * Delete user with the given loginName
	 * 
	 * @param login
	 *            The loginName of the user to be deleted
	 * @throws UserNotFoundException
	 */
	public void deleteUserByLogin(String login) throws UserNotFoundException {
		JPAUser user = this.getUserByLogin(login);
		if (user == null)
			throw new UserNotFoundException(login);
		deleteUser(user);
	}

	/**
	 * Delete user represented with the given JPA object
	 * 
	 * @param user
	 *            The JPA object of the user to be deleted
	 */
	public void deleteUser(JPAUser user) {
		unpersist(user);
	}

	/**
	 * Changes the password for the user identified by the given ID stored in
	 * the database. The function returns PasswordChangeResult.Success for
	 * successful password change and PasswordChangeResult.Error otherwise.
	 * 
	 * @param id
	 *            The ID of the user for whom the password should be changed
	 * @param oldPassword
	 *            The old password of the user
	 * @param newPassword
	 *            The new password of the user
	 * @return Result of the password change
	 */
	public PasswordChangeResult changeUserPasswordForId(long id, String oldPassword, String newPassword) {
		JPAUser target = this.getUserByID(id);
		if (target.getPassword().equals(oldPassword)) {
			target.setPassword(newPassword);
			persist(target);
			return PasswordChangeResult.Success;
		} else {
			return PasswordChangeResult.Error;
		}
	}

	/**
	 * The function stores a general purpose file for the given user.
	 * 
	 * @param userId
	 *            The ID of the user, with whom the file'll be associated.
	 * @param description
	 *            Description of the file.
	 * @param file
	 *            File object of the given file.
	 * @return Persisted JPAGeneralFile object for the saved file.
	 * @throws SQLException
	 * @throws IOException
	 */
	public JPAGeneralFile saveGeneralFile(JPAUser user, String description, File file) throws SQLException, IOException {
		long oid = saveFileAsLargeObject(file);

		JPAGeneralFile gf = new JPAGeneralFile();
		gf.setDescription(description);
		gf.setFileName(file.getName());
		gf.setUser(user);
		gf.setOID(oid);

		persist(gf);

		return gf;
	}

	/**
	 * Returns the list of all general purpose files stored in the database.
	 * 
	 * @return The List of general files.
	 */
	public List<JPAGeneralFile> getAllGeneralFiles() {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select gf from JPAGeneralFile gf");
			List<JPAGeneralFile> generalFileList = q.getResultList();
			return generalFileList;
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * The function returns a List of JPA objects representing the given users'
	 * general files stored in the database.
	 * 
	 * @param userId
	 *            The ID of the user.
	 * @return List of JPAGeneralFile objects.
	 */
	public List<JPAGeneralFile> getGeneralFilesByUser(long userId) {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select gf from JPAGeneralFile gf where gf.userId=:userId");
			q.setParameter("userId", userId);
			List<JPAGeneralFile> generalFileList = q.getResultList();
			return generalFileList;
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * The function deletes all stored general files for the given user.
	 * 
	 * @param userId
	 *            ID of the user.
	 * @return Number of deleted files.
	 * @throws SQLException
	 */
	public long deleteAllGeneralFilesByUser(long userId) throws SQLException {
		long count = 0;
		List<JPAGeneralFile> files = this.getGeneralFilesByUser(userId);
		for (JPAGeneralFile gf : files) {
			deleteLargeObject(gf.getOID());
			unpersist(gf);
			count++;
		}
		return count;
	}

	/**
	 * This function returns the persisted JPAGeneralFile object for the given
	 * fileID.
	 * 
	 * @param fileId
	 *            The persisted file's ID.
	 * @return JPAGeneralFile object for the stored file.
	 */
	public JPAGeneralFile getGeneralFileByID(long fileId) {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select gf from JPAGeneralFile gf where gf.id=:fileId");
			q.setParameter("fileId", fileId);
			JPAGeneralFile res = (JPAGeneralFile) q.getSingleResult();
			return res;
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * This function deletes the stored file for the given fileId.
	 * 
	 * @param fileId
	 *            The ID of the file to be deleted.
	 * @throws SQLException
	 */
	public void deleteGeneralFilesById(long fileId) throws SQLException {
		JPAGeneralFile file = this.getGeneralFileByID(fileId);
		deleteLargeObject(file.getOID());
		unpersist(file);
	}

	/**
	 * Returns an MD5 hash for the given file. The hash is computed upon the
	 * whole content of the file.
	 * 
	 * @param file
	 *            The object referencing to the file.
	 * @return The computed MD5 hash
	 * @throws IOException
	 */
	public String getMD5Hash(File file) throws IOException {
		FileInputStream fis = null;
		String res = null;
		try {
			fis = new FileInputStream(file);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte buf[] = new byte[2048];
			int s;
			while ((s = fis.read(buf, 0, 2048)) > 0) {
				md.update(buf, 0, s);
			}
			byte[] dig = md.digest();
			res = DatatypeConverter.printHexBinary(dig).toLowerCase();
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} finally {
			fis.close();
		}
		return res;
	}

	private String klaraMD5(String path) {
		StringBuffer sb = null;
		try {
			FileInputStream fs = new FileInputStream(path);
			sb = new StringBuffer();

			int ch;
			while ((ch = fs.read()) != -1) {
				sb.append((char) ch);
			}
			fs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// logError("File not found: " + path + " -- " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			// logError("Error reading file: " + path + " -- " +
			// e.getMessage());
		}
		String md5 = DigestUtils.md5Hex(sb.toString());
		return md5;
	}

	/**
	 * The function returns all DataSet entries in the database, which has an
	 * associated file with the same MD5 hash as given.
	 * 
	 * @param hash
	 *            The Hash whose occurences are we searching for.
	 * @return The list of found DataSet entries.
	 */
	public List<JPADataSetLO> getDataSetByHash(String hash) {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select ds from JPADataSetLO ds where ds.hash=:hash");
			q.setParameter("hash", hash);
			List<JPADataSetLO> dataSetList = q.getResultList();
			return dataSetList;
		} finally {
			cleanUpEntityManager();
		}
	}
	
	/**
	 * Returns a DataSet object for the given file hash. 
	 * @param hash The hash for we want the DataSet
	 * @return The object of the found dataset.
	 */
	public JPADataSetLO getSingleDataSetByHash(String hash) {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select ds from JPADataSetLO ds where ds.hash=:hash");
			q.setParameter("hash", hash);
			JPADataSetLO res = (JPADataSetLO) q.getSingleResult();
			return res;
		} finally {
			cleanUpEntityManager();
		}
	}
	
	

	/**
	 * The function stores the given file to the Database as a DataSet through
	 * JPA. The dataset will be associated with the given user, description.
	 * Please note, that new dataset's MD5 hash will be compared to the already
	 * stored dataset's MD5 hashes and if there are matching entries in the
	 * database the LargeObject won't be created, but instead a link is being
	 * created.
	 * 
	 * @param user
	 *            The JPAUser object of the owner
	 * @param dataSet
	 *            The File object of the file to be stored.
	 * @param description
	 *            The description of the DataSet.
	 * @throws SQLException
	 * @throws IOException
	 * @return The persisted JPADataSetLO object for the stored file
	 */
	public JPADataSetLO saveDataSet(JPAUser user, File dataSet, String description) throws SQLException, IOException {
		long oid = -1;
		String hash = this.getMD5Hash(dataSet);
		List<JPADataSetLO> sameHashDS = this.getDataSetByHash(hash);
		if (sameHashDS.size() > 0) {
			oid = sameHashDS.get(0).getOID();
		} else {
			oid = saveFileAsLargeObject(dataSet);
		}
		JPADataSetLO jpaDataSetLD = new JPADataSetLO();
		jpaDataSetLD.setHash(hash);
		jpaDataSetLD.setOID(oid);
		jpaDataSetLD.setOwner(user);
		jpaDataSetLD.setDescription(description);
		persist(jpaDataSetLD);
		return jpaDataSetLD;
	}

	/**
	 * The function stores the given file to the Database as a DataSet through
	 * JPA. The dataset will be associated with the given user, description.
	 * Please note, that new dataset's MD5 hash will be compared to the already
	 * stored dataset's MD5 hashes and if there are matching entries in the
	 * database the LargeObject won't be created, but instead a link is being
	 * created.
	 * 
	 * @param user
	 *            The JPAUser object of the owner
	 * @param dataSet
	 *            The File object of the file to be stored.
	 * @param description
	 *            The description of the DataSet.
	 * @param globalMetaData
	 *            The GlobalMetaData object containing global metadata
	 *            information for the dataset
	 * @throws SQLException
	 * @throws IOException
	 * @return The persisted JPADataSetLO object for the stored file
	 */
	public JPADataSetLO saveDataSet(JPAUser user, File dataSet, String description, JPAGlobalMetaData globalMetaData) throws SQLException, IOException {
		long oid = -1;
		String hash = this.getMD5Hash(dataSet);
		List<JPADataSetLO> sameHashDS = this.getDataSetByHash(hash);
		if (sameHashDS.size() > 0) {
			oid = sameHashDS.get(0).getOID();
		} else {
			oid = saveFileAsLargeObject(dataSet);
		}
		JPADataSetLO jpaDataSetLD = new JPADataSetLO();
		jpaDataSetLD.setHash(hash);
		jpaDataSetLD.setOID(oid);
		jpaDataSetLD.setOwner(user);
		jpaDataSetLD.setDescription(description);
		jpaDataSetLD.setGlobalMetaData(globalMetaData);
		persist(jpaDataSetLD);
		return jpaDataSetLD;
	}

	/**
	 * The function stores the given file to the Database as a DataSet through
	 * JPA. The dataset will be associated with the given user, description.
	 * Please note, that new dataset's MD5 hash will be compared to the already
	 * stored dataset's MD5 hashes and if there are matching entries in the
	 * database the LargeObject won't be created, but instead a link is being
	 * created.
	 * 
	 * @param user
	 *            The JPAUser object of the owner
	 * @param dataSet
	 *            The File object of the file to be stored.
	 * @param description
	 *            The description of the DataSet.
	 * @param globalMetaData
	 *            The JPAGlobalMetaData object containing global metadata
	 *            information for the dataset
	 * @param attributeMetaData
	 *            The List<JPAAttributeMetaData> object containing the list of attribute metadatas for the dataset
	 *            for the dataset
	 * @throws SQLException
	 * @throws IOException
	 * @return The persisted JPADataSetLO object for the stored file
	 */
	public JPADataSetLO saveDataSet(JPAUser user, File dataSet, String description, JPAGlobalMetaData globalMetaData, List<JPAAttributeMetaData> attributeMetaData) throws SQLException, IOException {
		long oid = -1;
		String hash = this.getMD5Hash(dataSet);
		List<JPADataSetLO> sameHashDS = this.getDataSetByHash(hash);
		if (sameHashDS.size() > 0) {
			oid = sameHashDS.get(0).getOID();
		} else {
			oid = saveFileAsLargeObject(dataSet);
		}
		JPADataSetLO jpaDataSetLD = new JPADataSetLO();
		jpaDataSetLD.setHash(hash);
		jpaDataSetLD.setOID(oid);
		jpaDataSetLD.setOwner(user);
		jpaDataSetLD.setDescription(description);
		jpaDataSetLD.setGlobalMetaData(globalMetaData);
		jpaDataSetLD.setAttributeMetaData(attributeMetaData);
		persist(jpaDataSetLD);
		return jpaDataSetLD;
	}

	/**
	 * Returns all datasets stored in the database using LargeObjects.
	 * 
	 * @return The List of datasets.
	 */
	public List<JPADataSetLO> getAllDataSetLargeObjects() {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select dslo from JPADataSetLO dslo");
			List<JPADataSetLO> dataSetLOList = q.getResultList();
			return dataSetLOList;
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * The function returns a list of all stored DataSets for the given user.
	 * 
	 * @param userId
	 *            ID of the user.
	 * @return List of JPADataSetLO objects.
	 */
	public List<JPADataSetLO> getDataSetLargeObjectsByUser(JPAUser user) {
		try {
			Query q = em.createQuery("select dslo from JPADataSetLO dslo where dslo.owner=:user");
			q.setParameter("user", user);
			List<JPADataSetLO> dataSetLOList = q.getResultList();
			return dataSetLOList;
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * The function deletes all stored datasets for the given user, that were
	 * stored using LargeObjects.
	 * 
	 * @param userId
	 *            ID of the user.
	 * @return Number of deleted datasets.
	 * @throws SQLException
	 */
	public long deleteAllDataSetLargeObjectsByUser(JPAUser user) throws SQLException {
		long count = 0;
		List<JPADataSetLO> datasets = this.getDataSetLargeObjectsByUser(user);
		for (JPADataSetLO dslo : datasets) {
			deleteLargeObject(dslo.getOID());
			unpersist(dslo);
			count++;
		}
		return count;
	}

	/**
	 * Returns the JPADataSetLO object for the given dataSetID, that identifies
	 * the DataSet stored in Database using LargeObject.
	 * 
	 * @param dataSetId
	 *            The ID of the DataSet.
	 * @return The JPADataSetLO object.
	 */
	public JPADataSetLO getDataSetLOByID(int dataSetId) {
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select dslo from JPADataSetLO dslo where dslo.id=:dataSetId");
			q.setParameter("dataSetId", dataSetId);
			return (JPADataSetLO) q.getSingleResult();
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * The function deletes the DataSet stored using LargeObject from the
	 * database for the given dataSetID.
	 * 
	 * @param dataSetId
	 *            The ID of the DataSet.
	 * @throws SQLException
	 */
	public void deleteDataSetLOById(int dataSetId) throws SQLException {
		JPADataSetLO dslo = this.getDataSetLOByID(dataSetId);
		deleteLargeObject(dslo.getOID());
		unpersist(dslo);
	}

	/**
	 * This function stored the given File to the Postgre DB using Postgre's
	 * LargeObject method. It returns the ID of the created LargeObject, that
	 * can be used for referencing the stored file.
	 * 
	 * @param file
	 *            Object File representing the file to be stored.
	 * @return The Object ID of the LargeObject.
	 * @throws SQLException
	 * @throws IOException
	 */
	public long saveFileAsLargeObject(File file) throws SQLException, IOException {
		// All LargeObject API calls must be within a transaction block
		((java.sql.Connection) connection).setAutoCommit(false);

		// Get the Large Object Manager to perform operations with
		LargeObjectManager lobj = connection.getLargeObjectAPI();

		// Create a new large object
		long oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);

		// Open the large object for writing
		LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);

		// Now open the file
		// File file = new File("myimage.gif");
		FileInputStream fis = new FileInputStream(file);

		// Copy the data from the file to the large object
		byte buf[] = new byte[2048];
		int s;// , tl = 0;
		while ((s = fis.read(buf, 0, 2048)) > 0) {
			obj.write(buf, 0, s);
			// tl += s;
		}

		// Close the large object
		obj.close();
		fis.close();

		((java.sql.Connection) connection).setAutoCommit(true);
		return oid;
	}

	/**
	 * The function deletes the LargeObject with the given OID from the
	 * database.
	 * 
	 * @param oid
	 *            The Object ID of the LargeObject to be deleted.
	 * @throws SQLException
	 */
	public void deleteLargeObject(long oid) throws SQLException {
		((java.sql.Connection) connection).setAutoCommit(false);
		LargeObjectManager lobj = connection.getLargeObjectAPI();
		lobj.unlink(oid);
		((java.sql.Connection) connection).setAutoCommit(true);
	}

	/**
	 * The function returns the content of the stored LargeObject
	 * 
	 * @param oid
	 *            The Object ID of the stored LArgeObject
	 * @return The content of the stored file.
	 * @throws SQLException
	 */
	public byte[] getDataFromLargeObject(long oid) throws SQLException {
		// All LargeObject API calls must be within a transaction block
		((java.sql.Connection) connection).setAutoCommit(false);

		// Get the Large Object Manager to perform operations with
		LargeObjectManager lobj = connection.getLargeObjectAPI();
		LargeObject obj = lobj.open(oid, LargeObjectManager.READ);

		// Read the data
		byte buf[] = new byte[obj.size()];
		obj.read(buf, 0, obj.size());

		// Close the object
		obj.close();

		((java.sql.Connection) connection).setAutoCommit(true);
		return buf;
	}
	
	/**
	 * Returns a PostgreLargeObjectReader object for the DataSet stored in the database,
	 * if there exists a dataset in the DB for the given hash. 
	 * @param hash The hash of the dataset
	 * @return The Reader for the dataset in the database
	 */
	public PostgreLargeObjectReader getLargeObjectReader(String hash){
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("select dslo from JPADataSetLO dslo where dslo.hash=:hashString");
			q.setParameter("hashString", hash);
			JPADataSetLO dslo=(JPADataSetLO) q.getSingleResult();
			return new PostgreLargeObjectReader(connection, dslo.getOID());
		} finally {
			cleanUpEntityManager();
		}
	}
	

	/**
	 * The function persists the given object to the database using Java
	 * Persistence API.
	 * 
	 * @param object
	 *            The object to be persisted.
	 */
	protected void persist(Object object) {
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			Object o = em.merge(object);
			em.persist(o);
			em.getTransaction().commit();
		} finally {
			cleanUpEntityManager();
		}
	}

	/**
	 * The function removes an object from the database, which was persisted
	 * using Java Persistence API.
	 * 
	 * @param object
	 *            The object to be deleted.
	 */
	private void unpersist(Object object) {
		if (object != null) {
			try {
				em = emf.createEntityManager();
				em.getTransaction().begin();
				Object o = em.merge(object);
				em.remove(o);
				em.getTransaction().commit();
			} finally {
				cleanUpEntityManager();
			}
		}
	}

	private void cleanUpEntityManager() {
		if (em != null) {
			em.close();
		}
	}

}
