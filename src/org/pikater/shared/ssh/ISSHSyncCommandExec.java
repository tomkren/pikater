package org.pikater.shared.ssh;

import com.jcraft.jsch.JSchException;

public interface ISSHSyncCommandExec
{
	/** 
	 * @param command
	 * Multiple commands may be specified in this string, e.g. "cd /etc; pwd"
	 * @return
	 * @throws JSchException
	 * @throws InterruptedException
	 */
	String execSync(String command) throws JSchException, InterruptedException;
}
