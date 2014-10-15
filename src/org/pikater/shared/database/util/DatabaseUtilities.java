package org.pikater.shared.database.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.logging.database.PikaterDBLogger;

/**
 * Various database-related interface.
 * 
 * @author SkyCrawl
 */
public class DatabaseUtilities
{
	/**
	 * Does the given list contain an entity with the given ID?
	 * 
	 * @param list the list we are searching in
	 * @param item the item we are searching for
	 * @return true if item is in the list or false 
	 */
	public static <T extends JPAAbstractEntity> boolean containsID(List<T> list, int entityID)
	{
		for(T i : list)
		{
			if(i.getId() == entityID)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Computes an MD5 hash of the given local file.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getMD5Hash(File file) throws IOException
	{
		if(file == null)
		{
			throw new NullPointerException("Who has ever heard of feeding nulls to a hashing function?");
		}
		else
		{
			String res = null;
			try (FileInputStream fis = new FileInputStream(file))
			{
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] buf = new byte[2048];
				int s;
				while ((s = fis.read(buf, 0, 2048)) > 0)
				{
					md.update(buf, 0, s);
				}
				byte[] dig = md.digest();
				res = DatatypeConverter.printHexBinary(dig).toLowerCase();
			}
			catch (NoSuchAlgorithmException nsae)
			{
				PikaterDBLogger.logThrowable("Unexpected error occured:", nsae);
			}
			return res;
		}
	}
}