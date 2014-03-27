package pikater.utility.pikaterDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.postgresql.PGConnection;

import pikater.data.PostgreSQLConnectionProvider;
import pikater.data.jpa.JPADataSetLO;
import pikater.data.jpa.JPAGeneralFile;
import pikater.data.jpa.JPARole;
import pikater.data.jpa.JPAUser;
import pikater.utility.pikaterDatabase.exceptions.UserNotFoundException;

public class DatabaseTest {
	
	PostgreSQLConnectionProvider pscp;
	
	public DatabaseTest(){
		pscp=new PostgreSQLConnectionProvider("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
	}
	
	public void test() throws ClassNotFoundException, SQLException, IOException, UserNotFoundException{
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("pikaterDataModel");
		
		Database db=new Database(factory,(PGConnection)pscp.getConnection());
		//db.setPostgreConnection((PGConnection)pscp.getConnection());
		
		db.init(true);
		
		List<JPAUser> userlist= db.getUsers();
		System.out.println("Users in database: ");
		for(JPAUser user:userlist){
			JPARole r=user.getRole();
			if(r!=null){
			System.out.println(user.getLogin()+": "+user.getEmail()+"-----"+r.getName());
			}else{
				System.out.println(user.getLogin()+": "+user.getEmail()+"----- null");
			}
		}
		System.out.println(userlist.size()+" items found.");
		System.out.println();
		
		
		/**
		
		String filepath="C:/java/test.txt";
		String filepath2="C:/java/test2.txt";
		
		File f=new File(filepath);
		File f2=new File(filepath2);
		
		
		
		db.saveDataSet(1, f);
		db.saveDataSet(1, f2);
		db.saveDataSet(2, f2);
		
		
		db.saveGeneralFile(1, "Just a testfile", f);
		db.saveGeneralFile(2, "Second test file for user no. 2", f2);
		
		*/
		//db.deleteDataSetLOById(29);
		
		List<JPADataSetLO> list= db.getAllDataSetLargeObjects();
		System.out.println("All DataSets: ");
		for(JPADataSetLO dslo:list){
			System.out.println(dslo.getID()+" "+dslo.getDataSetFileName()+" "+dslo.getOID());
		}
		System.out.println(list.size()+" items found.");
		System.out.println();
		
		//db.deleteGeneralFilesById(10);
		
		List<JPAGeneralFile> list2= db.getAllGeneralFiles();
		System.out.println("All GeneralFiles: ");
		for(JPAGeneralFile gf:list2){
			System.out.println(gf.getID()+" "+gf.getFileName()+" "+gf.getOID());
		}
		System.out.println(list2.size()+" items found.");
		System.out.println();
		
		
		
		/**
		List<JPADataSetLO> list= db.getDataSetLargeObjectsByUser(1);
		System.out.println("DataSets for user no. 1: ");
		for(JPADataSetLO dslo:list){
			System.out.println(dslo.getID()+" "+dslo.getDataSetFileName()+" "+dslo.getOID());
		}
		System.out.println(list.size()+" items found.");
		System.out.println();
		
		list= db.getDataSetLargeObjectsByUser(2);
		System.out.println("DataSets for user no. 2: ");
		for(JPADataSetLO dslo:list){
			System.out.println(dslo.getID()+" "+dslo.getDataSetFileName()+" "+dslo.getOID());
		}
		System.out.println(list.size()+" items found.");
		System.out.println();
		
		
		List<JPAGeneralFile> list2= db.getGeneralFilesByUser(1);
		System.out.println("GeneralFiles for user no. 1: ");
		for(JPAGeneralFile gf:list2){
			System.out.println(gf.getID()+" "+gf.getFileName()+" "+gf.getOID());
		}
		System.out.println(list2.size()+" items found.");
		System.out.println();
		
		
		list2= db.getGeneralFilesByUser(2);
		System.out.println("GeneralFiles for user no. 2: ");
		for(JPAGeneralFile gf:list2){
			System.out.println(gf.getID()+" "+gf.getFileName()+" "+gf.getOID());
		}
		System.out.println(list2.size()+" items found.");
		System.out.println();
		
		long del_count=db.deleteAllDataSetLargeObjectsByUser(1);
		System.out.println("Number of deleted DataSets for User no. 1: "+del_count);
		
		del_count=db.deleteAllDataSetLargeObjectsByUser(2);
		System.out.println("Number of deleted DataSets for User no. 2: "+del_count);
		**/
	}

	
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException, UserNotFoundException{
		DatabaseTest dt=new DatabaseTest();
		dt.test();
	}
	
	
}
