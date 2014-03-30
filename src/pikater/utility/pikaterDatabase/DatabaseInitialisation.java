package pikater.utility.pikaterDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.postgresql.PGConnection;

import pikater.data.PostgreSQLConnectionProvider;
import pikater.data.jpa.JPAAttributeMetaData;
import pikater.data.jpa.JPAAttributeNumericalMetadata;
import pikater.data.jpa.JPADataSetLO;
import pikater.data.jpa.JPAGlobalMetaData;
import pikater.data.jpa.JPARole;
import pikater.data.jpa.JPAUser;
import pikater.data.jpa.JPAUserPriviledge;
import pikater.utility.pikaterDatabase.exceptions.UserNotFoundException;

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
	 */
	private void itialisationData() throws SQLException, IOException, UserNotFoundException{
				
		
		// Initialisation of Datasets
		this.addIrisDataset();
		
		this.addWeatherDataset();
		
		this.addLinearDataset();
		
		
		// Test of Datasets
		for(JPADataSetLO dslo:database.getAllDataSetLargeObjects()){
			System.out.println("OID: "+dslo.getOID()+"  Hash:  "+dslo.getHash()+"  "+dslo.getDescription()+" ---  "+dslo.getOwner().getLogin()+"  GM.noInst: "+dslo.getGlobalMetaData().getNumberofInstances()+"  GM.DefTT: "+dslo.getGlobalMetaData().getDefaultTaskType().getName() );
		}
		
		
/**
		JPAUserPriviledge priviledgeSaveData = new JPAUserPriviledge();
		priviledgeSaveData.setName("SaveDatates");

		JPAUserPriviledge priviledgeSaveBox = new JPAUserPriviledge();
		priviledgeSaveBox.setName("SaveBox");

		
		JPARole userRole = new JPARole();
		userRole.setName("user");
		userRole.setDescription("Standard user role");
		userRole.addPriviledge(priviledgeSaveData);

		JPARole adminRole = new JPARole();
		adminRole.setName("admin");
		adminRole.setDescription("Admin role");
		userRole.addPriviledge(priviledgeSaveData);
		userRole.addPriviledge(priviledgeSaveBox);

		
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
		
		
		
		JPADataSetLO dslo=database.saveDataSet(stepanUser, new File());
		**/
		
		
		
		
	}
	
	private void addIrisDataset() throws SQLException, IOException, UserNotFoundException{
		
		JPAUser user = database.getUserByLogin("stepan");
		System.out.println(user.getEmail());


		File irisFile = new File("data/files/25d7d5d689042a3816aa1598d5fd56ef");
		System.out.println("--------------------");
		System.out.println(irisFile.getAbsolutePath());
		System.out.println("MD5 hash: "+database.getMD5Hash(irisFile));
		System.out.println("--------------------");

		JPAGlobalMetaData irisGlobalMD = database.createGlobalMetaData(150, "Classification");



		JPAAttributeNumericalMetadata sepallengthAtr = new JPAAttributeNumericalMetadata();
		sepallengthAtr.setReal(true);

		JPAAttributeNumericalMetadata sepalwidthAtr = new JPAAttributeNumericalMetadata();
		sepalwidthAtr.setReal(true);

		JPAAttributeNumericalMetadata petallengthAtr = new JPAAttributeNumericalMetadata();
		petallengthAtr.setReal(true);

		JPAAttributeNumericalMetadata petalwidthAtr = new JPAAttributeNumericalMetadata();
		petalwidthAtr.setReal(true);

		JPAAttributeNumericalMetadata classAtr = new JPAAttributeNumericalMetadata();
		classAtr.setReal(false);

		JPAAttributeMetaData irisAttributeMD = new JPAAttributeMetaData();
		irisAttributeMD.addAttribute(sepallengthAtr);
		irisAttributeMD.addAttribute(sepalwidthAtr);
		irisAttributeMD.addAttribute(petallengthAtr);
		irisAttributeMD.addAttribute(petalwidthAtr);
		irisAttributeMD.addAttribute(classAtr);

		
		JPADataSetLO irisDataset = database.saveDataSet(user, irisFile, "Iris", irisGlobalMD);
		irisDataset.setIrisAttributeMD(irisAttributeMD);
		//database.persist(irisDataset);

	}

	private void addWeatherDataset() throws SQLException, IOException, UserNotFoundException{
		
		JPAUser user = database.getUserByLogin("stepan");
		System.out.println(user.getEmail());

		
		File weatherFile = new File("data/files/772c551b8486b932aed784a582b9c1b1");
		System.out.println(weatherFile.getAbsolutePath());
		System.out.println("MD5 hash: "+database.getMD5Hash(weatherFile));
		System.out.println("--------------------");		
		
		JPADataSetLO weatherDataset = database.saveDataSet(user, weatherFile, "Weather",database.createGlobalMetaData(14,"Multivariate"));
		//weatherDataset.setGlobalMetaData(database.createGlobalMetaData(14,"Multivariate"));
		//database.persist(weatherDataset);

		JPAAttributeMetaData weatherAttributeMD = new JPAAttributeMetaData();
	}

	private void addLinearDataset() throws SQLException, IOException, UserNotFoundException{
		
		JPAUser user = database.getUserByLogin("stepan");
		System.out.println(user.getEmail());

		
		File linearFile = new File("data/files/dc7ce6dea5a75110486760cfac1051a5");
		System.out.println(linearFile.getAbsolutePath());
		System.out.println("MD5 hash: "+database.getMD5Hash(linearFile));
		System.out.println("--------------------");

		JPADataSetLO linearDataset = database.saveDataSet(user, linearFile, "Linear Data",database.createGlobalMetaData(5000,"Regression"));
		//linearDataset.setGlobalMetaData(database.createGlobalMetaData(5000,"Regression"));//!!!!CHECK DEFAULT TASK TYPE
		//database.persist(linearDataset);

		JPAAttributeMetaData linearAttributeMD = new JPAAttributeMetaData();
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
		
		
		for(JPADataSetLO dslo:database.getAllDataSetLargeObjects()){
			System.out.println("OID: "+dslo.getOID()+"  Hash:  "+dslo.getHash()+"  "+dslo.getDescription()+" ---  "+dslo.getOwner().getLogin()+"  GM.noInst: "+dslo.getGlobalMetaData().getNumberofInstances()+"  GM.DefTT: "+dslo.getGlobalMetaData().getDefaultTaskType().getName() );
		}

	}
	
	public static void main(String[] args) throws SQLException, IOException, UserNotFoundException, ClassNotFoundException {
        
		EntityManagerFactory emf=Persistence.createEntityManagerFactory("pikaterDataModel");
		
		DatabaseInitialisation data = new DatabaseInitialisation(emf,(PGConnection)(new PostgreSQLConnectionProvider("jdbc:postgresql://nassoftwerak.ms.mff.cuni.cz:5432/pikater", "pikater", "a").getConnection()));
		data.itialisationData();
	}
}
