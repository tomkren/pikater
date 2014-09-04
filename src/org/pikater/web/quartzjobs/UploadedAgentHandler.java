package org.pikater.web.quartzjobs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.pikater.core.agents.gateway.WebToCoreEntryPoint;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.logging.web.PikaterLogger;
import org.pikater.shared.quartz.jobs.base.ImmediateOneTimeJob;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;

public class UploadedAgentHandler extends ImmediateOneTimeJob
{
	public UploadedAgentHandler()
	{
		super(5);
	}

	@Override
	public boolean argumentCorrect(int index, Object arg)
	{
		switch(index)
		{
			case 0:
				return arg instanceof JPAUser;
			case 1:
			case 2:
			case 3:
				return arg instanceof String;
			case 4:
				return arg instanceof File;
				
			default:
				return false;
		}
	}

	@Override
	public void buildJob(JobBuilder builder)
	{
		// builder.withIdentity("RemoveExpiredTrainedModels", "Jobs");
	}

	@Override
	protected void execute() throws JobExecutionException
	{
		// information from GUI
		JPAUser owner = getArg(0);
		String fileName = getArg(1);
		String agentClass = getArg(2);
		String agentDescription = getArg(3);
		File uploadedFile = getArg(4);

		// transport to database
		try
		{
			JPAExternalAgent agent = new JPAExternalAgent();
			agent.setAgentClass(agentClass);
			agent.setName(fileName);
			agent.setDescription(agentDescription);
			agent.setOwner(owner);
			agent.setCreated(new Date());
			byte[] content;
			try
			{
				content = Files.readAllBytes(Paths.get(uploadedFile.getAbsolutePath()));
			}
			catch (IOException e)
			{
				throw new JobExecutionException("Unable to open input jar", e);
			}
			agent.setJar(content);
			DAOs.externalAgentDAO.storeEntity(agent);
			
			try
			{
				WebToCoreEntryPoint.notify_newAgent(agent.getId());
			}
			catch (Throwable t)
			{
				PikaterLogger.logThrowable("Could not send notification about a new external agent to core.", t);
			}
		}
		finally
		{
			uploadedFile.delete();
		}
	}
}