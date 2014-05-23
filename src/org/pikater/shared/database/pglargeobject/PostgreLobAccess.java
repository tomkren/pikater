package org.pikater.shared.database.pglargeobject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.pikater.shared.database.ConnectionProvider;
import org.pikater.shared.database.PostgreSQLConnectionProvider;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PostgreLobAccess {
	static PGConnection con=null;
	
	
	static {
		try {
			
			con=(PGConnection)(
					new PostgreSQLConnectionProvider(
							"jdbc:postgresql://nassoftwerak.ms.mff.cuni.cz:5432/pikater",
							"pikater",
							"SrapRoPy").getConnection());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static PostgreLargeObjectReader getPostgreLargeObjectReader(long oid){
		return new PostgreLargeObjectReader(con, oid);
	}
	
	public static long saveFileToDB(File file) throws SQLException, IOException{
		((java.sql.Connection) con).setAutoCommit(false);
		LargeObjectManager lobj = con.getLargeObjectAPI();
		long oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
		LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
		FileInputStream fis = new FileInputStream(file);

		byte buf[] = new byte[2048];
		int s;
		while ((s = fis.read(buf, 0, 2048)) > 0) {
			obj.write(buf, 0, s);
		}
		obj.close();
		fis.close();

		((java.sql.Connection) con).setAutoCommit(true);
		return oid;
	}
}
