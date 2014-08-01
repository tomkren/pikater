package org.pikater.shared.database.pglargeobject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.pikater.shared.database.PostgreSQLConnectionProvider;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.util.IOUtils;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

public class PostgreLobAccess
{
	private static final int DEFAULT_BUFFER_SIZE = 2048;
	
	static PGConnection con = null;

	static
	{
		try
		{

			con = (PGConnection) (new PostgreSQLConnectionProvider(
					"jdbc:postgresql://nassoftwerak.ms.mff.cuni.cz:5432/pikater",
					"pikater", "SrapRoPy").getConnection());
		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isDatabaseConnected()
	{
		return con != null;
	}

	public static PostgreLargeObjectReader getPostgreLargeObjectReader(long oid)
	{
		return new PostgreLargeObjectReader(con, oid);
	}

	public static long saveFileToDB(File file) throws SQLException, IOException
	{
		((java.sql.Connection) con).setAutoCommit(false);
		LargeObjectManager lobj = con.getLargeObjectAPI();
		long oid = lobj.createLO(LargeObjectManager.READ
				| LargeObjectManager.WRITE);
		LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
		FileInputStream fis = new FileInputStream(file);

		byte buf[] = new byte[2048];
		int s;
		while ((s = fis.read(buf, 0, 2048)) > 0)
		{
			obj.write(buf, 0, s);
		}
		obj.close();
		fis.close();

		((java.sql.Connection) con).setAutoCommit(true);
		return oid;
	}

	public static File downloadFileFromDB(long oid) throws Throwable
	{
		// create the result file
		File file = IOUtils.createTemporaryFile("LO");
		
		// download database file into the temporary file
		PostgreLargeObjectReader dbInput = null;
		FileOutputStream hddOutput = null;
        try
        {
        	dbInput = new PostgreLargeObjectReader(con, oid);
    		hddOutput = new FileOutputStream(file);
        	
    		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        	int length;
            while ((length = dbInput.read(buffer, 0, DEFAULT_BUFFER_SIZE)) >= 0)
            {
                hddOutput.write(buffer, 0, length);
            }
            // TODO: verify hashes?
            return file;
        }
		catch (FileNotFoundException e)
		{
			PikaterLogger.logThrowable("A file created just now can not be found? Weird...", e);
			throw e;
		}
		catch (IOException e)
		{
			PikaterLogger.logThrowable(String.format("Problems encountered while reading large object with id '%d':", oid), e);
			throw e;
		}
        finally
        {
        	try
        	{
        		if(dbInput != null)
        		{
        			dbInput.close();
        		}
        		if(hddOutput != null)
        		{
        			hddOutput.close();
        		}
        	}
        	catch (IOException t)
        	{
        		PikaterLogger.logThrowable("There is a problem closing output stream to the downloaded file. Weird...", t);
        		// seems to me there is no reason to throw this
        	}
        }
	}
}
