package org.pikater.shared.utilities.pikaterDatabase.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.pikater.shared.database.pglargeobject.PostgreLobAccess;

public class LargeObjectInputStreamTest {

	public static void main(String[] args) {
		
		long oid=30783;
		
		try {
			InputStream is=PostgreLobAccess.getPostgreLargeObjectReader(oid).getInputStream();
			FileOutputStream fos=new FileOutputStream(new File("core/datasets/test_from_DB.arff"));			
			System.out.println("Retrieving dataset content...");
			byte[] buffer=new byte[1000];
			int size=0;
			int c=0;
			while((c=is.read(buffer))>0){
				fos.write(buffer, 0, c);
				size+=c;
			}
			System.out.println("Copied "+size+" bytes");
			fos.close();
			is.close();
			
			
		} catch (SQLException  | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
