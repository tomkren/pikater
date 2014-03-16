package pikater.utility.pikaterDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import pikater.data.jpa.JPADataSetLO;
import pikater.data.jpa.JPAGeneralFile;

public class Database {
	
	PGConnection connection;
	
	private EntityManagerFactory factory;
	EntityManager em = null;
	
	public Database(){
		factory = Persistence.createEntityManagerFactory("pikaterDataModel");
	}
	
    public void saveExperimentXML(int userID, File xmlExperiment) {
        //:TODO
    }
	public void insertExperiment(int userID, String xmlExperimetId, Long dataSetId, int priority) {
        //:TODO
    }
  
	/**
	 * The function stores a general purpose file for the given user. 
	 * @param userId The ID of the user, with whom the file'll be associated.
	 * @param description Description of the file.
	 * @param file File object of the given file.
	 * @return Persisted JPAGeneralFile object for the saved file.
	 * @throws SQLException
	 * @throws IOException
	 */
  public JPAGeneralFile saveGeneralFile(long userId,String description,File file) throws SQLException, IOException{
	  long oid=saveFileAsLargeObject(file);
	  
	  JPAGeneralFile gf=new JPAGeneralFile();
	  gf.setDescription(description);
	  gf.setFileName(file.getName());
	  gf.setUserID(userId);
	  gf.setOID(oid);
	  
	  persist(gf);
	  
	  return gf;
  }
  
  /**
   * Returns the list of all general purpose files stored in the database.
   * @return The List of general files.
   */
  public List<JPAGeneralFile> getAllGeneralFiles(){
	  em = factory.createEntityManager();
	  Query q = em.createQuery("select gf from JPAGeneralFile gf");
	  List<JPAGeneralFile> generalFileList = q.getResultList();
	  em.close();
	  return generalFileList;
  }
  
  /**
   * The function returns a List of JPA objects representing the given users' general files stored in the database.
   * @param userId The ID of the user.
   * @return List of JPAGeneralFile objects.
   */
  public List<JPAGeneralFile> getGeneralFilesByUser(long userId){
	  em = factory.createEntityManager();
	  Query q = em.createQuery("select gf from JPAGeneralFile gf where gf.userId=:userId");
	  q.setParameter("userId", userId);
	  List<JPAGeneralFile> generalFileList = q.getResultList();
	  em.close();
	  return generalFileList;
  }
  
  /**
   * The function deletes all stored general files for the given user.
   * @param userId ID of the user.
   * @return Number of deleted files.
   * @throws SQLException
   */
  public long deleteAllGeneralFilesByUser(long userId) throws SQLException{
	  long count=0;
	  List<JPAGeneralFile> files=this.getGeneralFilesByUser(userId);
	  for(JPAGeneralFile gf:files){
		  deleteLargeObject(gf.getOID());
		  unpersist(gf);
		  count++;
	  }
	  return count;
  }
  
  /**
   * This function returns the persisted JPAGeneralFile object for the given fileID. 
   * @param fileId The persisted file's ID.
   * @return JPAGeneralFile object for the stored file.
   */
  public JPAGeneralFile getGeneralFileByID(long fileId){
	  em = factory.createEntityManager();
	  Query q = em.createQuery("select gf from JPAGeneralFile gf where gf.id=:fileId");
	  q.setParameter("fileId", fileId);
	  JPAGeneralFile res=(JPAGeneralFile)q.getSingleResult();
	  em.close();
	  return res;
  }
  
  /**
   * This function deletes the stored file for the given fileId.
   * @param fileId The ID of the file to be deleted.
   * @throws SQLException
   */
  public void deleteGeneralFilesById(long fileId) throws SQLException{
	  JPAGeneralFile file=this.getGeneralFileByID(fileId);
	  deleteLargeObject(file.getOID());
	  unpersist(file);
  }
  
  
  
  
  
  
  /**
   * The function stores the given file to the Database through JPA. It uses the file's name as one of the identificators in the database, that is returned by java.io.File.getName() function.
   * @param userID 
   * @param dataSet
   * @throws SQLException
   * @throws IOException
   * @return The persisted JPADataSetLO object for the stored file
   */
  public JPADataSetLO saveDataSet(long userID, File dataSet) throws SQLException, IOException {
      long oid=saveFileAsLargeObject(dataSet);
      JPADataSetLO jpaDataSetLD=new JPADataSetLO();
      jpaDataSetLD.setOID(oid);
      jpaDataSetLD.setUserID(userID);
      jpaDataSetLD.setDataSetFileName(dataSet.getName());
      //persisting the object jpaDataSet
      persist(jpaDataSetLD);
      return jpaDataSetLD;
  }
  
  /**
   * Returns all datasets stored in the database using LargeObjects.
   * @return The List of datasets.
   */
  public List<JPADataSetLO> getAllDataSetLargeObjects(){
	  em = factory.createEntityManager();
	  Query q = em.createQuery("select dslo from JPADataSetLO dslo");
	  List<JPADataSetLO> dataSetLOList = q.getResultList();
	  em.close();
	  return dataSetLOList;
  }
  
  
  /**
   * The function returns a list of all stored DataSets for the given user.
   * @param userId ID of the user.
   * @return List of JPADataSetLO objects.
   */
  public List<JPADataSetLO> getDataSetLargeObjectsByUser(long userId){
	  em = factory.createEntityManager();
	  Query q = em.createQuery("select dslo from JPADataSetLO dslo where dslo.userId=:userId");
	  q.setParameter("userId", userId);
	  List<JPADataSetLO> dataSetLOList = q.getResultList();
	  em.close();
	  return dataSetLOList;
  }
  
  
  /**
   * The function deletes all stored datasets for the given user, that were stored using LargeObjects.
   * @param userId ID of the user.
   * @return Number of deleted datasets.
   * @throws SQLException
   */
  public long deleteAllDataSetLargeObjectsByUser(long userId) throws SQLException{
	  long count=0;
	  List<JPADataSetLO> datasets=this.getDataSetLargeObjectsByUser(userId);
	  for(JPADataSetLO dslo:datasets){
		  deleteLargeObject(dslo.getOID());
		  unpersist(dslo);
		  count++;
	  }
	  return count;
  }
  
  /**
   * Returns the JPADataSetLO object for the given dataSetID, that identifies the DataSet stored in Database using LargeObject.
   * @param dataSetId The ID of the DataSet.
   * @return The JPADataSetLO object.
   */
  public JPADataSetLO getDataSetLOByID(long dataSetId){
	  em = factory.createEntityManager();
	  Query q = em.createQuery("select dslo from JPADataSetLO dslo where dslo.id=:dataSetId");
	  q.setParameter("dataSetId", dataSetId);
	  JPADataSetLO res=(JPADataSetLO)q.getSingleResult();
	  em.close();
	  return res;
  }
  
  /**
   * The function deletes the DataSet stored using LargeObject from the database for the given dataSetID.
   * @param dataSetId The ID of the DataSet.
   * @throws SQLException
   */
  public void deleteDataSetLOById(long dataSetId) throws SQLException{
	  JPADataSetLO dslo=this.getDataSetLOByID(dataSetId);
	  deleteLargeObject(dslo.getOID());
	  unpersist(dslo);
  }
  
  
  
  /**
   * Sets the Connection object used to communicate with the database. 
   * @param connection The PGConnection object representing the connection.
   */
  public void setPostgreConnection(PGConnection connection){
	  this.connection=connection;
  }
  
  
  
  /**
   * This function stored the given File to the Postgre DB using Postgre's LargeObject method. It returns the ID of the created LargeObject, that can be used for referencing the stored file.
   * @param file Object File representing the file to be stored.
   * @return The Object ID of the LargeObject.
   * @throws SQLException
   * @throws IOException
   */
  public long saveFileAsLargeObject(File file) throws SQLException, IOException{
	  // All LargeObject API calls must be within a transaction block
	  ((java.sql.Connection) connection).setAutoCommit(false);

	  // Get the Large Object Manager to perform operations with
	  LargeObjectManager lobj = connection.getLargeObjectAPI();

	  // Create a new large object
	  long oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);

	  // Open the large object for writing
	  LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);

	  // Now open the file
	  //File file = new File("myimage.gif");
	  FileInputStream fis = new FileInputStream(file);

	  // Copy the data from the file to the large object
	  byte buf[] = new byte[2048];
	  int s;//, tl = 0;
	  while ((s = fis.read(buf, 0, 2048)) > 0) {
	      obj.write(buf, 0, s);
	      //tl += s;
	  }
	  
	  
	  // Close the large object
	  obj.close();
	  fis.close();
	  
	  ((java.sql.Connection) connection).setAutoCommit(true);
	  return oid;
  }
  
  /**
   * The function deletes the LargeObject with the given OID from the database. 
   * @param oid The Object ID of the LargeObject to be deleted.
   * @throws SQLException
   */
  public void deleteLargeObject(long oid) throws SQLException{
	  ((java.sql.Connection) connection).setAutoCommit(false);
	  LargeObjectManager lobj = connection.getLargeObjectAPI();
	  lobj.unlink(oid);
	  ((java.sql.Connection) connection).setAutoCommit(true);
  }
  
  /**
   * The function returns the content of the stored LargeObject
   * @param oid The Object ID of the stored LArgeObject
   * @return The content of the stored file.
   * @throws SQLException
   */
  public byte[] getDataFromLargeObject(long oid) throws SQLException{
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
   * The function persists the given object to the database using Java Persistence API.
   * @param object The object to be persisted.
   */
  private void persist(Object object){
	    em = factory.createEntityManager();
	  	em.getTransaction().begin();
	    em.persist(object);
	    em.getTransaction().commit();
	    em.close();
  }
  
  /**
   * The function removes an object from the database, which was persisted using Java Persistence API.
   * @param object The object to be deleted.
   */
  private void unpersist(Object object){
	    em = factory.createEntityManager();
	  	em.getTransaction().begin();
	  	Object o=em.merge(object);
	    em.remove(o);
	    em.getTransaction().commit();
	    em.close();
  }
  
}
