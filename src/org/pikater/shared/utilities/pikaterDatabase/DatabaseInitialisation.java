package org.pikater.shared.utilities.pikaterDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.postgresql.PGConnection;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.JPAUserPriviledge;
import org.pikater.shared.database.PostgreSQLConnectionProvider;

import org.pikater.shared.utilities.pikaterDatabase.exceptions.UserNotFoundException;
import org.pikater.shared.utilities.pikaterDatabase.initialisation.JPAMetaDataReader;

public class DatabaseInitialisation {

	public enum PasswordChangeResult{Success,Error};
	
	PGConnection connection;
	EntityManagerFactory emf=null;
	EntityManager em = null;
	Database database = null;
	
	public DatabaseInitialisation(EntityManagerFactory emf,PGConnection connection){
		this.emf=emf;
		this.connection=connection;
		this.database = new Database(emf, connection);
	}

	
	/**
	 * Initialisation of test-data in DataBase
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws UserNotFoundException 
	 * @throws ParseException 
	 */
	private void itialisationData() throws SQLException, IOException, UserNotFoundException, ParseException{		
/*		
		// Initialisation of Datasets
		this.addIrisDataset();
		
		this.addWeatherDataset();
		
		this.addLinearDataset();
		
		this.createRoles();

		this.insertFinishedBatch();
*/
		this.createFileMaping();

	}
	
	private void addIrisDataset() throws SQLException, IOException, UserNotFoundException{
		
		JPAUser user = database.getUserByLogin("stepan");
		System.out.println(user.getEmail());

		File file = new File("data/files/25d7d5d689042a3816aa1598d5fd56ef");
		
		JPAMetaDataReader mdr=new JPAMetaDataReader();
		mdr.readFile(file);
		
				
		System.out.println("--------------------");
		System.out.println(file.getAbsolutePath());
		System.out.println("MD5 hash: "+database.getMD5Hash(file));
		System.out.println("--------------------");

		JPAGlobalMetaData irisGlobalMD = database.createGlobalMetaData(150, "Classification");
		JPAAttributeMetaData attrMD=mdr.getJPAAttributeMetaData();
		
		
		JPADataSetLO dslo=database.saveDataSet(user, file, "Iris", irisGlobalMD, attrMD);

	}

	private void addWeatherDataset() throws SQLException, IOException, UserNotFoundException{
		
		JPAUser user = database.getUserByLogin("stepan");
		System.out.println(user.getEmail());
		
		File weatherFile = new File("data/files/772c551b8486b932aed784a582b9c1b1");
		
		JPAMetaDataReader mdr=new JPAMetaDataReader();
		mdr.readFile(weatherFile);


		System.out.println(weatherFile.getAbsolutePath());
		System.out.println("MD5 hash: "+database.getMD5Hash(weatherFile));
		System.out.println("--------------------");		
				
		JPAGlobalMetaData weatherGlobalMD = database.createGlobalMetaData(14,"Multivariate");
		JPAAttributeMetaData attrMD=mdr.getJPAAttributeMetaData();
		
		JPADataSetLO dslo=database.saveDataSet(user, weatherFile, "Weather", weatherGlobalMD, attrMD);
	}

	private void addLinearDataset() throws SQLException, IOException, UserNotFoundException{
		
		JPAUser user = database.getUserByLogin("stepan");
		System.out.println(user.getEmail());
		
		File linearFile = new File("data/files/dc7ce6dea5a75110486760cfac1051a5");
		
		JPAMetaDataReader mdr=new JPAMetaDataReader();
		mdr.readFile(linearFile);
		
		
		System.out.println(linearFile.getAbsolutePath());
		System.out.println("MD5 hash: "+database.getMD5Hash(linearFile));
		System.out.println("--------------------");		
		
		JPAGlobalMetaData weatherGlobalMD = database.createGlobalMetaData(5000,"Regression");
		JPAAttributeMetaData attrMD=mdr.getJPAAttributeMetaData();
		
		JPADataSetLO dslo=database.saveDataSet(user, linearFile, "Linear Data", weatherGlobalMD, attrMD);
		
	}

	private void createRoles() {

		JPAUserPriviledge priviledgeSaveData = new JPAUserPriviledge();
		priviledgeSaveData.setName("SaveDatates");

		JPAUserPriviledge priviledgeSaveBox = new JPAUserPriviledge();
		priviledgeSaveBox.setName("SaveBox");

		database.persist(priviledgeSaveData);
		
		JPARole userRole = new JPARole();
		userRole.setName("user");
		userRole.setDescription("Standard user role");
		userRole.addPriviledge(priviledgeSaveData);

		JPARole adminRole = new JPARole();
		adminRole.setName("admin");
		adminRole.setDescription("Admin role");
		userRole.addPriviledge(priviledgeSaveData);
		userRole.addPriviledge(priviledgeSaveBox);

		database.persist(userRole);
		database.persist(adminRole);

		
		JPAUser stepanUser = new JPAUser();
		stepanUser.setLogin("stepan");
		stepanUser.setPassword("123");
		stepanUser.setEmail("Bc.Stepan.Balcar@gmail.com");
		stepanUser.setPriorityMax(9);
		stepanUser.setRole(adminRole);

		JPAUser kjUser = new JPAUser();
		kjUser.setLogin("kj");
		kjUser.setPassword("123");
		kjUser.setEmail("kj@gmail.com");
		kjUser.setPriorityMax(9);
		kjUser.setRole(adminRole);

		JPAUser sjUser = new JPAUser();
		sjUser.setLogin("sj");
		sjUser.setPassword("123");
		sjUser.setEmail("sj@gmail.com");
		sjUser.setPriorityMax(9);
		sjUser.setRole(adminRole);

		JPAUser spUser = new JPAUser();
		spUser.setLogin("sp");
		spUser.setPassword("123");
		spUser.setEmail("sp@gmail.com");
		spUser.setPriorityMax(9);
		spUser.setRole(adminRole);

		JPAUser martinUser = new JPAUser();
		martinUser.setLogin("martin");
		martinUser.setPassword("123");
		martinUser.setEmail("Martin.Pilat@mff.cuni.cz");
		martinUser.setPriorityMax(9);
		martinUser.setRole(userRole);
		
		database.persist(stepanUser);
		database.persist(kjUser);
		database.persist(sjUser);
		database.persist(spUser);
		database.persist(martinUser);
		
		
	}
	
	private void insertFinishedBatch() throws ParseException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JPAResult result = new JPAResult();
		result.setAgentName("RBFNetwork");
		result.setAgentTypeId(0);
		result.setErrorRate(0.214285716414452);
		result.setFinish(dateFormat.parse("2014-03-29 11:06:57"));
		result.setKappaStatistic(0.511627912521362);
		result.setMeanAbsoluteError(0.264998614788055);
		result.setNote("Note of result :-)");
		result.setOptions("-S 0 -M 0.2 ");
		result.setRelativeAbsoluteError(55.6497116088867);
		result.setRootMeanSquaredError(0.462737262248993);
		result.setRootRelativeSquaredError(93.7923049926758);
		result.setSerializedFileName("");
		result.setStart(dateFormat.parse("2014-03-29 11:06:55"));
		
		JPAExperiment experiment = new JPAExperiment();
		experiment.setStatus("Finished");
		experiment.setXMLDescription("XML");
		experiment.setResult(result);

		
		JPABatch batch = new JPABatch();
		batch.setName("Stepan's batch of experiments - school project");
		batch.setPriority(99);
		batch.addExperiment(experiment);
		batch.setOwnerID(1);

		this.database.persist(batch);
	}
	
	private void createFileMaping() {
		
		JPAFilemapping f = new JPAFilemapping();
		f.setUserid(1);
		f.setExternalfilename("iris.arff");
		f.setInternalfilename("25d7d5d689042a3816aa1598d5fd56ef");
		database.persist(f);
		
		JPAFilemapping f2 = new JPAFilemapping();
		f2.setUserid(1);
		f2.setExternalfilename("weather.arff");
		f2.setInternalfilename("772c551b8486b932aed784a582b9c1b1");
		database.persist(f2);

	}

	private void testData() throws SQLException, IOException, UserNotFoundException{
		
		database.addRole("user", "Standard user role");
		database.addRole("admin","Standard administrator role");
		
		database.addUser("stepan", "123", "bc.stepan.balcar@gmail.com", 9); // + role
		database.addUser("kj", "123", "nassoftwerak@gmail.com", 6);
		database.addUser("sj", "123", "nassoftwerak@gmail.com", 6);
		database.addUser("sp", "123", "nassoftwerak@gmail.com", 6);
		database.addUser("martin", "123", "Martin.Pilat@mff.cuni.cz", 0);

		database.setRoleForUser("stepan", "admin");
		database.setRoleForUser("kj", "admin");
		database.setRoleForUser("sj", "admin");
		database.setRoleForUser("sp", "admin");
		database.setRoleForUser("martin", "user");
				
		JPAUserPriviledge priviledgeSaveData = new JPAUserPriviledge();
		priviledgeSaveData.setName("SaveDataSet");

		JPAUserPriviledge priviledgeSaveBox = new JPAUserPriviledge();
		priviledgeSaveBox.setName("SaveBox");

		JPARole roleAdmin = database.getRoleByName("admin");
		roleAdmin.addPriviledge(priviledgeSaveData);
		roleAdmin.addPriviledge(priviledgeSaveBox);
		
		JPARole roleUser = database.getRoleByName("user");
		roleUser.addPriviledge(priviledgeSaveData);
	
		JPAUser stepan = database.getUserByLogin("stepan");
		stepan.setRole(roleAdmin);

		database.persist(stepan);
		/**
		JPAUser john=this.getUserByLogin("johndoe");
		this.saveGeneralFile(john.getId(), "First Data File",new File( "./data/files/25d7d5d689042a3816aa1598d5fd56ef"));
		this.saveGeneralFile(john.getId(), "Second Data File",new File( "./data/files/772c551b8486b932aed784a582b9c1b1"));
		this.saveGeneralFile(john.getId(), "Third Data File",new File( "./data/files/dc7ce6dea5a75110486760cfac1051a5"));
		**/
		
		
		// Test of Datasets
		for(JPADataSetLO dslo:database.getAllDataSetLargeObjects()){
			System.out.println("OID: "+dslo.getOID()+"  Hash:  "+dslo.getHash()+"  "+dslo.getDescription()+" ---  "+dslo.getOwner().getLogin()+"  GM.noInst: "+dslo.getGlobalMetaData().getNumberofInstances()+"  GM.DefTT: "+dslo.getGlobalMetaData().getDefaultTaskType().getName() );
		}

	}


	public static void main(String[] args) throws SQLException, IOException, UserNotFoundException, ClassNotFoundException, ParseException {

		EntityManagerFactory emf=Persistence.createEntityManagerFactory("pikaterDataModel");

		DatabaseInitialisation data = new DatabaseInitialisation(emf,(PGConnection)(new PostgreSQLConnectionProvider("jdbc:postgresql://nassoftwerak.ms.mff.cuni.cz:5432/pikater", "pikater", "a").getConnection()));
		data.itialisationData();

		
		System.out.println("mm");
	}
}
