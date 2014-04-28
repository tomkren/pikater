package org.pikater.shared.ssh;

public interface ISSHChannel
{
	SSHSession getSession();
	void close();
}
