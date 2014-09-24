package org.pikater.web.unused.ssh;

import java.io.ByteArrayOutputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;

public final class SSHInteractiveExecChannel implements ISSHChannel, ISSHSyncCommandExec
{
	private final SSHSession session;
	
	public SSHInteractiveExecChannel(SSHSession session)
	{
		this.session = session;
	}
	
	// ------------------------------------------
	// INHERITED INTERFACE
	
	@Override
	public SSHSession getSession()
	{
		return session;
	}
	
	@Override
	public void close()
	{
	}
	
	// ------------------------------------------
	// PUBLIC INTERFACE
	
	@Override
	public String execSync(String command) throws JSchException, InterruptedException
	{
		// initialize
	    ChannelExec channel = (ChannelExec) session.openChannel("exec");
	    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
	    channel.setOutputStream(responseStream);
	    
	    // set command
	    channel.setCommand(command.trim() + "\n"); // command will not execute on the remote machine if it doesn't end with newline

	    // connect channel and automatically execute the command
	    channel.connect();

	    // wait until the channel closes and the response is here
	    while (channel.isConnected()) 
	    {
	        Thread.sleep(100);
	    }
	    
	    return responseStream.toString();
	}
	
	/*
	 * SCP:
	 * - usage: scp [options and flags] [[user@]host1:]file1 ... [[user@]host2:]file2
	 * 		- soubory se kopírují do aktuálního pracovního adresáře
	 * 		- soubory lze specifikovat s {user@host} - slouží pro určení odkud kam
	 * 		- soubory je dobré specifikovat s absolutní/relativní cestou, aby se zamezilo chybě, když ve jménu souboru je ":"
	 * - automaticky vypisuje progress a vypadá to, že to ani nebude muset bejt interaktivní záležitost...ale uvidíme
	 * -2 - forces SSH2
	 * -B - batch mode (no repeated password or passphrases)
	 * -r - recursively copy all directories - copies symbolic links (potential insecurity?)
	 * -v - for debugging...
	 */
}