package org.pikater.web.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHSession
{
	public static final int defaultSSHport = 22;
	// public static final int sshSessionErrorStatusCode = 255;
	// protected static final String sshKnownHostsPath = "$HOME/.ssh/known_hosts";
	
	private Session session;
	private boolean sessionInitializedProperly;
	private final ISSHSessionNotificationHandler notificationHandler;

	public SSHSession(String hostname, String fingerprint, String username, String password, ISSHSessionNotificationHandler notificationHandler)
	{
		/*
	      // Set environment variable "LANG" as "ja_JP.eucJP".
	      ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
	    */
		
		// TODO: use jsch.setConfig() to globally alter the default config before creating any sessions. Will
		// result in more secure sessions :).
		JSch.setConfig("StrictHostKeyChecking", "no"); // TODO: this is really insecure... change later
		/*
		try
		{
			jsch.setKnownHosts(sshKnownHostsPath);
		}
		catch (JSchException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(HostKey hk : jsch.getHostKeyRepository().getHostKey())
		{
			System.out.println(hk.getHost());
			System.out.println(hk.getKey());
			System.out.println(hk.getType());
			System.out.println(hk.getFingerPrint(jsch));
		}
		if(jsch.getHostKeyRepository().check(hostname, fingerprint.getBytes()) == HostKeyRepository.NOT_INCLUDED)
		{
			try
			{
				jsch.getHostKeyRepository().add(new HostKey(hostname, HostKey.SSHRSA, fingerprint.getBytes()), null);
			}
			catch (JSchException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		
		JSch jsch = new JSch();
		this.notificationHandler = notificationHandler;
		try
		{
			this.session = jsch.getSession(username, hostname, defaultSSHport);
		}
		catch (JSchException e)
		{
			// don't log this, it is most likely a user related error
			notificationHandler.handleError("Hostname or username has invalid syntax.", e);
			sessionInitializedProperly = false;
			return;
		}
		this.session.setPassword(password);
		
		try
		{
			this.session.connect();
		}
		catch (JSchException e)
		{
			// don't log this, it is most likely a user related error
			notificationHandler.handleError("Could not connect to the specified hostname, with the specified username and password.", e);
			sessionInitializedProperly = false;
			return;
		}
		
		sessionInitializedProperly = true;
	}
	
	public ISSHSessionNotificationHandler getNotificationHandler()
	{
		return notificationHandler;
	}
	
	public boolean isAliveAndWell()
	{
		return sessionInitializedProperly && session.isConnected();
	}
	
	public synchronized void closeSession()
	{
		if((session != null) && session.isConnected())
		{
			session.disconnect();
			notificationHandler.notifySessionClosed();
		}
		session = null;
	}
	
	public synchronized Channel openChannel(String channelType) throws JSchException
	{
		if(!sessionInitializedProperly)
		{
			throw new JSchException("Session has not been initialized properly due to an error. Resolve it and try again.");
		}
		else if (!session.isConnected())
		{
			notificationHandler.notifySessionClosed();
			throw new JSchException("Session has been closed by the remote machine.");
		}
		else
		{
			return session.openChannel(channelType);
		}
	}
	
	//------------------------------------------------------------------------------
	// PUBLIC TYPES:
	
	public interface ISSHSessionNotificationHandler
	{
		void notifySessionClosed();
		void notifyChannelClosed(int exitStatus);
		void handleError(String description, Throwable t);
	}
}