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
	
	
	/**
  public void saveExperimentXML(int userID, File xmlExperiment) {
        //:TODO
  }
  **/
	
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
  
  
  public List<JPAGeneralFile> getGeneralFilesByUser(long userId){
	  em = factory.createEntityManager();
	  Query q = em.createQuery("select gf from JPAGeneralFile gf where gf.userId=:userId");
	  q.setParameter("userId", userId);
	  List<JPAGeneralFile> generalFileList = q.getResultList();
	  em.close();
	  return generalFileList;
  }
  
  /**
   * Function stores the given file to the Database through JPA. It uses the file's name as one of the identificators in the database, that is returned by java.io.File.getName() function.
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
  
  public List<JPADataSetLO> getDataSetLargeObjectsByUser(long userId){
	  em = factory.createEntityManager();
	  Query q = em.createQuery("select dslo from JPADataSetLO dslo where dslo.userId=:userId");
	  q.setParameter("userId", userId);
	  List<JPADataSetLO> dataSetLOList = q.getResultList();
	  em.close();
	  return dataSetLOList;
  }
   

  public void insertExperiment(int userID, String xmlExperimetId, Long dataSetId, int priority) {
        //:TODO
  }
  
  
  public void setPostgreConnection(PGConnection connection){
	  this.connection=connection;
  }
  
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
  
  public byte[] getDataFromLargeObject(long oid) throws SQLException{
	  // All LargeObject API calls must be within a transaction block
	  ((java.sql.Connection) connection).setAutoCommit(false);

	  // Get the Large Object Manager to perform operations with
	  LargeObjectManager lobj = connection.getLargeObjectAPI();
	  LargeObject obj = lobj.open(oid, LargeObjectManager.READ);

	  // Read the data
	  byte buf[] = new byte[obj.size()];
	  obj.read(buf, 0, obj.size());
	  // Do something with the data read here

	  // Close the object
	  obj.close();
	    
	  ((java.sql.Connection) connection).setAutoCommit(true);
	  return buf;
  }
  

  private void persist(Object object){
	    em = factory.createEntityManager();
	  	em.getTransaction().begin();
	    em.persist(object);
	    em.getTransaction().commit();
	    em.close();
  }
}
