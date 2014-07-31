package org.pikater.web.ssh;

import java.io.IOException;

import com.jcraft.jsch.JSchException;

public interface ISSHAsyncCommandExec
{
	void execAsync(String command) throws JSchException, IOException;
}