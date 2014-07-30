package org.pikater.web.vaadin.gui.server.welcometour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.pikater.shared.TopologyModel;
import org.pikater.shared.TopologyModel.ServerType;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.ssh.PikaterSSHLauncher;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.RefreshableWizardStep;
import org.pikater.web.vaadin.gui.server.welcometour.Step3TableContainer;
import org.pikater.web.vaadin.gui.server.welcometour.RemoteServerInfoItem.Header;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class Step3 extends RefreshableWizardStep<WelcomeTourCommons, WelcomeTourWizard> 
{
	/**
	 * Internal variable storing the top level GUI element of this wizard step.
	 */
	private final Component content;
	
	/**
	 * Individual low-level GUI components.
	 */
	private final TabSheet tabSheet;
	private final Table connectionTable;
	private Component lastConsoleComponentUsed;
	private final Label defaultComponent;
	
	/**
	 * Various other variables.
	 */
	private final Step3TableContainer dataSource;
	private final Map<Object, PikaterSSHLauncher> serverIDToLauncherMapping;
	
	public Step3(WelcomeTourWizard parentWizard)
	{
		super(parentWizard);
		
		this.dataSource = new Step3TableContainer(getOutput().getWrappedModels());
		this.serverIDToLauncherMapping = new HashMap<Object, PikaterSSHLauncher>();
		for(Object rowID : this.dataSource.getItemIds())
		{
			this.serverIDToLauncherMapping.put(rowID, new PikaterSSHLauncher(dataSource.getInfoInstanceByID(rowID)));
		}
		
		/*
		 * Create the button interface.
		 */
		final Button btn_launchPikaterOnConnectedServers = new Button("Launch Pikater on connected masters", new Button.ClickListener()
		{
			private static final long serialVersionUID = -4815413200621613910L;

			@Override
			public void buttonClick(ClickEvent event)
			{ 
				Collection<Object> connectedNotLaunchedMasterServerIDs = getConnectedNotLaunchedMasterServerIDs();
				Collection<Object> connectedNotLaunchedSlaveServerIDs = getConnectedNotLaunchedSlaveServerIDs();
				if(!connectedNotLaunchedMasterServerIDs.isEmpty() || !connectedNotLaunchedSlaveServerIDs.isEmpty())
				{
					if(!isPikaterLaunchedWithNoErrors(connectedNotLaunchedMasterServerIDs) 
							|| !isPikaterLaunchedWithNoErrors(connectedNotLaunchedSlaveServerIDs)) // updates GUI accordingly 
					{
						GeneralDialogs.error(null, "Pikater could not be launched on some of the servers. Use the console to find out what went wrong.");
					}
					tabSheet.getTab(defaultComponent).setEnabled(true);
				}
				else
				{
					MyNotifications.showInfo("All servers already launched", "Connect to other servers and launch piktaer on them.");
				}
			}
		});
		Button btn_checkAvailabilityOfIncludedServers = new Button("Connect to designated machines if possible", new Button.ClickListener()
		{
			private static final long serialVersionUID = -6541999846400127008L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				Collection<Object> includedNotConnectedServerIDs = getIncludedNotConnectedServerIDs();
				if(!includedNotConnectedServerIDs.isEmpty())
				{
					// first check that all the included servers are reachable
					if(!areServersReachable(includedNotConnectedServerIDs, false)) // updates GUI accordingly
					{
						GeneralDialogs.error(null, "Pikater could not be launched on some of the servers. Use the console to find out what went wrong.");
					}
					if(!getConnectedServerIDs().isEmpty())
					{
						btn_launchPikaterOnConnectedServers.setEnabled(true);
					}
				}
				else
				{
					MyNotifications.showInfo("All selected servers are connected", "Select some others and try again.");
				}
			}
		});
		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponent(btn_checkAvailabilityOfIncludedServers);
		hLayout.addComponent(btn_launchPikaterOnConnectedServers);
		btn_launchPikaterOnConnectedServers.setEnabled(false);
		
		/*
		 * Setup the GUI.
		 * Let's stick with a single table for now but eventually:
		 * TODO - Accordion by topology - content would be all the servers in the topology, with the same table headers as now 
		 */
		
		this.connectionTable = new Table();
		this.connectionTable.setColumnReorderingAllowed(false);
		this.connectionTable.setEditable(true);
		this.connectionTable.setSelectable(true);
		this.connectionTable.setSortEnabled(true);
		this.connectionTable.setImmediate(true);
		this.connectionTable.setContainerDataSource(dataSource);
		this.connectionTable.setDescription("Connect to some servers and then select a table line to display its corresponding server's connection console.");
		this.connectionTable.setHeight("100%");
		this.connectionTable.addValueChangeListener(new Table.ValueChangeListener()
		{
			private static final long serialVersionUID = -6262657918609094172L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				if(connectionTable.getValue() != null) // something is selected
				{
					Component newComponent = serverIDToLauncherMapping.get(connectionTable.getValue()).getOutputConsoleComponent();
					if(newComponent == null)
					{
						newComponent = defaultComponent;
					}
					
					tabSheet.replaceComponent(lastConsoleComponentUsed, newComponent);
					lastConsoleComponentUsed = newComponent;
					tabSheet.setSelectedTab(1);
				}
			}
		});
		
		/*
		 * Set this wizard step's content. 
		 */
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponent(connectionTable);
		vLayout.addComponent(new Label());
		vLayout.addComponent(hLayout);
		vLayout.setHeight("100%");
		vLayout.setComponentAlignment(hLayout, Alignment.BOTTOM_LEFT);
		vLayout.setExpandRatio(connectionTable, 1);
		
		this.defaultComponent = new Label();
		this.lastConsoleComponentUsed = this.defaultComponent;
		
		this.tabSheet = new TabSheet();
		this.tabSheet.addTab(vLayout, "Overview of connections");
		this.tabSheet.addTab(defaultComponent, "Console for selected connection");
		this.tabSheet.getTab(defaultComponent).setEnabled(false);
		this.tabSheet.setHeight("550px");
		
		this.content = tabSheet;
	}
	
	@Override
	public void refresh()
	{
		connectionTable.refreshRowCache();
	}

	@Override
	public String getCaption()
	{
		return "Launch Pikater on remote masters and establish connection";
	}

	@Override
	public Component getContent()
	{
		return content;
	}

	@Override
	public boolean onAdvance()
	{
		Map<String, TopologyModel> topologyView = getTopologyView();
		Collection<TopologyModel> finalModels = new ArrayList<TopologyModel>();
		for(TopologyModel model : topologyView.values())
		{
			if(model.isWellFormed()) 
			{
				finalModels.add(model);
			}
		}
		
		if(!finalModels.isEmpty()) // check for our final goal 
		{
			// set all final models to the application wide variable
			for(TopologyModel model : finalModels)
			{
				ServerConfigurationInterface.getJadeTopologies().topologyWasConnected(model);
			}
			
			// cleanup - close all connections
			for(Object rowID : getLaunchedOrConnectedServerIDs())
			{
				serverIDToLauncherMapping.get(rowID).close();
			}
			
			return true;
		}
		else
		{
			GeneralDialogs.error("Can not finish", "At least 1 master need to have pikater launched and "
					+ "connection to at least 1 slave (both the same topology) are needed to continue.");
			return false;
		}
	}

	@Override
	public boolean onBack()
	{
		return true;
	}
	
	// ------------------------------------------------------------------
	// PRIVATE INTERFACE - TABLE QUERIES
	
	private Collection<Object> getIncludedServerIDs()
	{
		Collection<Object> result = new ArrayList<Object>();
		for(Object rowID : dataSource.getItemIds())
		{
			if(dataSource.getInfoInstanceByID(rowID).isIncluded())
			{
				result.add(rowID);
			}
		}
		return result;
	}
	
	private Collection<Object> getConnectedServerIDs()
	{
		Collection<Object> result = new ArrayList<Object>();
		for(Object rowID : dataSource.getItemIds())
		{
			if(dataSource.getInfoInstanceByID(rowID).isConnected())
			{
				result.add(rowID);
			}
		}
		return result;
	}
	
	private Collection<Object> getLaunchedServerIDs()
	{
		Collection<Object> result = new ArrayList<Object>();
		for(Object rowID : dataSource.getItemIds())
		{
			if(dataSource.getInfoInstanceByID(rowID).isLaunched())
			{
				result.add(rowID);
			}
		}
		return result;
	}
	
	private Collection<Object> filterByType(Collection<Object> rowIDs, ServerType type)
	{
		Iterator<Object> i = rowIDs.iterator();
		while (i.hasNext())
		{
			Object rowID = i.next();
			if(dataSource.getInfoInstanceByID(rowID).getServerType() != type)
			{
				i.remove();
			}
		}
		return rowIDs;
	}
	
	private Set<Object> getIncludedNotConnectedServerIDs()
	{
		Set<Object> result = new HashSet<Object>();
		result.addAll(getIncludedServerIDs());
		result.removeAll(getConnectedServerIDs());
		return result;
	}
	
	private Set<Object> getLaunchedOrConnectedServerIDs()
	{
		Set<Object> result = new HashSet<Object>();
		result.addAll(getLaunchedServerIDs());
		result.addAll(getConnectedServerIDs());
		return result;
	}
	
	private Set<Object> getConnectedNotLaunchedMasterServerIDs()
	{
		Set<Object> result = new HashSet<Object>();
		result.addAll(filterByType(getConnectedServerIDs(), ServerType.MASTER));
		result.removeAll(filterByType(getLaunchedServerIDs(), ServerType.MASTER));
		return result;
	}
	
	private Set<Object> getConnectedNotLaunchedSlaveServerIDs()
	{
		Set<Object> result = new HashSet<Object>();
		result.addAll(filterByType(getConnectedServerIDs(), ServerType.SLAVE));
		result.removeAll(filterByType(getLaunchedServerIDs(), ServerType.SLAVE));
		return result;
	}
	
	private Collection<Object> getLaunchedMasterAndConnectedSlaveServerIDs()
	{
		Collection<Object> result = new ArrayList<Object>();
		result.addAll(filterByType(getLaunchedServerIDs(), ServerType.MASTER));
		result.addAll(filterByType(getConnectedServerIDs(), ServerType.SLAVE));
		return result;
	}
	
	private Map<String, TopologyModel> getTopologyView()
	{
		Map<String, TopologyModel> result = new HashMap<String, TopologyModel>();
		for(Object rowID : getLaunchedMasterAndConnectedSlaveServerIDs()) // add all designated servers to the corresponding topology models
		{
			RemoteServerInfoItem info = dataSource.getInfoInstanceByID(rowID);
			String topologyName = info.getTopologyName();
			if(!result.containsKey(topologyName))
			{
				result.put(topologyName, new TopologyModel());
			}
			result.get(topologyName).addServer(info.getServerType(), info.underlyingInfoInstance);
		}
		return result;
	}
	
	// ------------------------------------------------------------------
	// PRIVATE INTERFACE - MISCELLANEOUS
	
	private boolean areServersReachable(Collection<Object> serverIDs, boolean test_forceReturnTrue)
	{
		boolean result = true;
		for(Object rowID : serverIDs)
		{
			if(!serverIDToLauncherMapping.get(rowID).tryConnect(test_forceReturnTrue)) // updates GUI accordingly - no custom actions needed
			{
				result = false;
			}
		}
		return result;
	}
	
	private boolean isPikaterLaunchedWithNoErrors(Collection<Object> serverIDs)
	{
		Map<String, Object> hostnameToIDMapping = new HashMap<String, Object>();
		SortedSet<String> sortedHostnames = new TreeSet<String>(); // TODO: sorts lexicographically - "12" comes before "5"
		for(Object serverID : serverIDs)
		{
			String hostname = (String) dataSource.getInfoInstanceByID(serverID).getProperty(Header.HOSTNAME).getValue();
			hostnameToIDMapping.put(hostname, serverID);
			sortedHostnames.add(hostname);
		}
		
		boolean result = true;
		for(String hostname : sortedHostnames)
		{
			if(!serverIDToLauncherMapping.get(hostnameToIDMapping.get(hostname)).launchPikater()) // updates GUI accordingly - no custom actions needed
			{
				result = false;
			}
		}
		return result;
	}
}