package org.pikater.web.ssh;

public interface ISSHChannel
{
	SSHSession getSession();
	void close();
}
