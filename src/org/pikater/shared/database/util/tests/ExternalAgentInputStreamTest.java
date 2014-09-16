package org.pikater.shared.database.util.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.daos.DAOs;

public class ExternalAgentInputStreamTest
{
	public static void main(String[] args) throws IOException
	{
		JPAExternalAgent agent = DAOs.externalAgentDAO.getByID(33301);
		System.out.println("Found external agent: "+agent.getName());
		
		InputStream is=null;
		FileOutputStream fos=null;
		try
		{
			is=agent.getInputStream();
			fos=new FileOutputStream(new File("core/ext_agents/"+agent.getName()+"_from_DB.jar"));
			System.out.println("Retrieving agent content...");
			byte[] buffer=new byte[1000];
			int size=0;
			int c=0;
			while((c=is.read(buffer))>0){
				fos.write(buffer, 0, c);
				size+=c;
			}
			System.out.println("Copied "+size+" bytes");
		}
		catch(Exception e)
		{
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(is);
		}
	}
}