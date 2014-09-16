package org.pikater.shared.database.postgre.largeobject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.pikater.shared.database.postgre.MyPGConnection;
import org.pikater.shared.logging.database.PikaterDBLogger;
import org.pikater.shared.util.IOUtils;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

public class PGLargeObjectAction
{
	/**
	 * Default download/upload buffer size.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 2048;
	
	/**
	 * Object providing context that this action should respect.
	 */
	private final IPGLOActionContext actionContext;
	
	/**
	 * Constructor used to provide context telling the action to interrupt.
	 * @param actionContext
	 */
	public PGLargeObjectAction(IPGLOActionContext actionContext)
	{
		this.actionContext = actionContext;
	}
	
	private boolean isActionInterrupted()
	{
		return (actionContext != null) && actionContext.isInterrupted();
	}
	
	//--------------------------------------------------
	// MAIN DEFINED ACTIONS
	
	public long uploadLOToDB(File file) throws SQLException, IOException, InterruptedException
	{
		((java.sql.Connection) MyPGConnection.getConnectionToCurrentPGDB()).setAutoCommit(false);
		LargeObjectManager lobj = MyPGConnection.getConnectionToCurrentPGDB().getLargeObjectAPI();
		long oid = lobj.createLO(LargeObjectManager.READ
				| LargeObjectManager.WRITE);
		LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
		FileInputStream fis = new FileInputStream(file);

		try
		{
			byte buf[] = new byte[2048];
			int s;
			while ((s = fis.read(buf, 0, DEFAULT_BUFFER_SIZE)) > 0)
			{
				if(isActionInterrupted())
				{
					throw new InterruptedException("Interrupted by application."); 
				}
				else
				{
					obj.write(buf, 0, s);
				}
			}
			return oid;
		}
		finally
		{
			// TODO: delete from DB if interrupted?
			obj.close();
			fis.close();
			((java.sql.Connection) MyPGConnection.getConnectionToCurrentPGDB()).setAutoCommit(true);
		}
	}

	public File downloadLOFromDB(long oid) throws IOException, IOException, InterruptedException 
	{
		// create the result file
		File file = IOUtils.createTemporaryFile("LO");
		
		// download database file into the temporary file
		PGLargeObjectReader dbInput = null;
		FileOutputStream hddOutput = null;
        try
        {
        	dbInput = new PGLargeObjectReader(MyPGConnection.getConnectionToCurrentPGDB(), oid);
    		hddOutput = new FileOutputStream(file);
        	
    		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        	int length;
            while ((length = dbInput.read(buffer, 0, DEFAULT_BUFFER_SIZE)) > 0)
            {
            	if(isActionInterrupted())
            	{
            		throw new InterruptedException("Interrupted by application.");
            	}
            	else
            	{
            		hddOutput.write(buffer, 0, length);
            	}
            }
            // TODO: verify hashes?
            return file;
        }
		catch (FileNotFoundException e)
		{
			PikaterDBLogger.logThrowable("A file created just now can not be found? Weird...", e);
			throw e;
		}
		catch (IOException e)
		{
			PikaterDBLogger.logThrowable(String.format("Problems encountered while reading large object with id '%d':", oid), e);
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
        		PikaterDBLogger.logThrowable("There is a problem closing output stream to the downloaded file. Weird...", t);
        		// seems to me there is no reason to throw this
        	}
        	if(isActionInterrupted())
			{
				file.delete();
			}
        }
	}
}