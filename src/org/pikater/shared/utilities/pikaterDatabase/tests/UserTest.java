package org.pikater.shared.utilities.pikaterDatabase.tests;

import java.util.Date;

import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.utils.ResultFormatter;

public class UserTest {

	/**
	 * For every run should update the last login time for user sp
	 */
	public void test(){
		try {
			JPAUser sp=new ResultFormatter<JPAUser>(DAOs.userDAO.getByLogin("sp")).getSingleResult();
			if(sp.getLastLogin()!=null){
				System.out.println("Last login time: "+sp.getLastLogin().toString());
			}else{
				System.out.println("User never logged in before.");
			}
			sp.setLastLogin(new Date());
			DAOs.userDAO.updateEntity(sp);
			System.out.println("Updated login time: "+sp.getLastLogin().toString());
		} catch (NoResultException e) {
			System.err.println("User not found...");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		UserTest t=new UserTest();
		t.test();
	}

}
