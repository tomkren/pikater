package org.pikater.shared;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("topology")
public class TopologyModel
{
	public enum ServerType
	{
		MASTER,
		SLAVE;
		
		@Override
		public String toString()
		{
			return this.name();
		}
	};
	
	/**
	 * The Set collection is critical for @Step3 class. Do not change it lightly.
	 */
	private final Set<RemoteServerInfo> masters;
	private final Set<RemoteServerInfo> slaves;
	
	public TopologyModel()
	{
		this.masters = new HashSet<RemoteServerInfo>();
		this.slaves = new HashSet<RemoteServerInfo>();
	}
	
	public boolean addServer(ServerType type, RemoteServerInfo server)
	{
		switch(type)
		{
			case MASTER:
				return this.masters.add(server);
			case SLAVE:
				return this.slaves.add(server);
			default:
				throw new IllegalStateException();
		}
	}
	
	public Set<RemoteServerInfo> getMasters()
	{
		return masters;
	}

	public Set<RemoteServerInfo> getSlaves()
	{
		return slaves;
	}

	public boolean isWellFormed()
	{
		return isServersCollectionValid(masters) && isServersCollectionValid(slaves);
	}
	
	private boolean isServersCollectionValid(Collection<RemoteServerInfo> coll)
	{
		return (coll != null) && (coll.size() > 0); 
	}
}
