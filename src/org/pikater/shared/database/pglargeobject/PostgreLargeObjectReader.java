package org.pikater.shared.database.pglargeobject;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

public class PostgreLargeObjectReader extends Reader {
	
	long oid=-1;
	LargeObjectManager lobj=null;
	LargeObject obj = null;
	PGConnection con=null;
	
	/**
	 * Creates a new instance of PostgreLargeObjectReader for the
	 * defined connection and LargeObject ID
	 * @param con The PGConnection, that is used to retrieve the data
	 * @param oid The ID of the LargeObject for which the reader should be created
	 */
	public PostgreLargeObjectReader(PGConnection con, long oid){
		this.con=con;
		this.oid=oid;
		init();
	}

	private void init(){
		try {
			((java.sql.Connection) con).setAutoCommit(false);
			lobj = con.getLargeObjectAPI();
			obj = lobj.open(oid, LargeObjectManager.READ);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close(){
		try{
			if(obj!=null) obj.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		try{
			if(con!=null) ((java.sql.Connection) con).setAutoCommit(true);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * Function that reads data into a char array. Pleade note, that the Postgre DB
	 * internally returns an array of bytes and this method uses type conversion.
	 * The main purpose of this function is to provide compatibility for Java classes that
	 * rely on inheritance from java.io.Reader
	 */
	public int read(char[] arg0, int arg1, int arg2) throws IOException {
		byte buf[] = new byte[arg2];
		int res=-1;
		try {
			res=obj.read(buf, arg1, arg2);
			if(res==0) return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		for(int i=0;i<arg2;i++){
			arg0[i]=(char)buf[i];
		}
		return res;
	}
	
	/**
	 * Function to retrieve data from the Large Object.
	 * It uses the native byte[] data byte to retrieve the data, which is the preferred way
	 * to get the data from the Datasase.
	 * In order to retrieve the whole file from the database you can use the size() method to 
	 * create a sufficient byte array.
	 * @param buf
	 * @param offset
	 * @param count
	 * @return Number of bytes read or -1 if nothing was read
	 */
	public int read(byte[] buf, int offset, int count) {
		try {
			return obj.read(buf, offset, count);
		} catch (SQLException e) {
			return -1;
		}
	}

	/**
	 * Returns the size of the opened Large Object. It return -1 for a closed Large Object. 
	 * @return Size of the LargeObject.
	 */
	public int size(){
		try {
			return obj.size();
		} catch (SQLException e) {
			return -1;
		}
	}
	
}
