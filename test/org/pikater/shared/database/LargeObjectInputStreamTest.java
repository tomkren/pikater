package org.pikater.shared.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.pikater.shared.database.postgre.largeobject.PGLargeObjectReader;
import org.pikater.shared.logging.core.ConsoleLogger;

public class LargeObjectInputStreamTest {

	public static void main(String[] args) {

		long oid = 30783;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = PGLargeObjectReader.getForLargeObject(oid).getInputStream();
			fos = new FileOutputStream(new File("core/datasets/test_from_DB.arff"));

			System.out.println("Retrieving dataset content...");
			byte[] buffer = new byte[1000];
			int size = 0;
			int c = 0;
			while ((c = is.read(buffer)) > 0) {
				fos.write(buffer, 0, c);
				size += c;
			}
			System.out.println("Copied " + size + " bytes");
			fos.close();
			is.close();
		} catch (SQLException e) {
			ConsoleLogger.logThrowable(e.getMessage(), e);
		} catch (IOException e) {
			ConsoleLogger.logThrowable(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(is);
		}
	}
}