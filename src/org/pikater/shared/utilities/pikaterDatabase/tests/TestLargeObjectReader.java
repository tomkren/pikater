package org.pikater.shared.utilities.pikaterDatabase.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.pikater.shared.database.connection.PostgreSQLConnectionProvider;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.postgre.largeobject.PGLargeObjectReader;
import org.pikater.shared.utilities.pikaterDatabase.Database;
import org.postgresql.PGConnection;

public class TestLargeObjectReader {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException{
		EntityManagerFactory emf=Persistence.createEntityManagerFactory("pikaterDataModel");

		PGConnection con=
				(PGConnection)(new PostgreSQLConnectionProvider(
						"jdbc:postgresql://nassoftwerak.ms.mff.cuni.cz:5432/pikater",
						"pikater",
						"a").getConnection());
		
		Database db=new Database(emf, con);
		
		List<JPADataSetLO> dslos=db.getAllDataSetLargeObjects();
		
		for(JPADataSetLO dslo:dslos){
			System.out.println("Hash: "+dslo.getHash()+"   OID: "+dslo.getOID()+" Owner: "+dslo.getOwner().getLogin());
			
			
			PGLargeObjectReader plor=db.getLargeObjectReader(dslo.getHash());
			
			System.out.println("Size: "+plor.size());
			
			
		    BufferedReader br=new BufferedReader(plor);
		    
		    int count=0;
		    String line=null;
		    while((line=br.readLine())!=null){
		    	count++;
		    	if(count<=7) //For testing purposes we only print out only the first 6 rows, for the whole content just comment this condition
		    	System.out.println(count+". L(arge)O(object)L(ine): "+line);
		    }
		    System.out.println("Lines: "+count);
		    System.out.println();
		    
		    plor.close();
		    
		}
		
		//Testing the access via the byte array
		/**
		for(JPADataSetLO dslo:dslos){
			System.out.println("Hash: "+dslo.getHash()+"   OID: "+dslo.getOID()+" Owner: "+dslo.getOwner().getLogin());
			
			
			PostgreLargeObjectReader plor=db.getLargeObjectReader(dslo.getHash());
			
			System.out.println("Size: "+plor.size());
			
			byte[] content=new byte[plor.size()];
			plor.read(content, 0, plor.size());
			String s=new String(content);
		    System.out.println("Content: ");
		    System.out.println(s);
		    System.out.println();
		    
		    plor.close();
		    
		}
		
		**/
		
		
	}

}
