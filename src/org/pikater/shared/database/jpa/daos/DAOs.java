package org.pikater.shared.database.jpa.daos;

public class DAOs {
	public static final DataSetDAO dataSetDAO=new DataSetDAO();
	public static final UserDAO userDAO=new UserDAO();
	public static final UserPriviledgeDAO userPrivDAO=new UserPriviledgeDAO();
	public static final RoleDAO roleDAO=new RoleDAO();
	public static final FileMappingDAO filemappingDAO=new FileMappingDAO();
	public static final ResultDAO resultDAO=new ResultDAO();
	public static final ExperimentDAO experimentDAO=new ExperimentDAO();
	public static final BatchDAO batchDAO=new BatchDAO();
	public static final TaskTypeDAO taskTypeDAO=new TaskTypeDAO();
}
