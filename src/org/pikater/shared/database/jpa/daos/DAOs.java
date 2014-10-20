package org.pikater.shared.database.jpa.daos;

public class DAOs {
	public static final DataSetDAO DATASETDAO = new DataSetDAO();
	public static final UserDAO USERDAO = new UserDAO();
	public static final UserPriviledgeDAO USERPRIVDAO = new UserPriviledgeDAO();
	public static final RoleDAO ROLEDAO = new RoleDAO();
	public static final FileMappingDAO FILEMAPPINGDAO = new FileMappingDAO();
	public static final ResultDAO RESULTDAO = new ResultDAO();
	public static final ExperimentDAO EXPERIMENTDAO = new ExperimentDAO();
	public static final BatchDAO BATCHDAO = new BatchDAO();
	public static final TaskTypeDAO TASKTYPEDAO = new TaskTypeDAO();
	public static final AgentInfoDAO AGENTINFODAO = new AgentInfoDAO();
	public static final ModelDAO MODELDAO = new ModelDAO();
	public static final ExternalAgentDAO EXTERNALAGENTDAO = new ExternalAgentDAO();
}
