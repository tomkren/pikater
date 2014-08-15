package org.pikater.web.ssh;

import com.jcraft.jsch.JSchException;

public interface ISSHChannel
{
	SSHSession getSession();
	void close() throws JSchException;
}