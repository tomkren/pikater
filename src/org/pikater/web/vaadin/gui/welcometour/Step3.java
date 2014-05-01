package org.pikater.web.vaadin.gui.welcometour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pikater.shared.TopologyModel;
import org.pikater.shared.TopologyModel.ServerType;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.pikater.PikaterSSHLauncher;
import org.pikater.web.vaadin.gui.welcometour.Step3TableContainer;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class Step3 extends WelcomeTourWizardStep
{
	/**
	 * Internal variable storing the top level GUI element of this wizard step.
	 */
	private final Component content;
	
	/**
	 * Individual low-level GUI components.
	 */
	private final TabSheet tabPanel;
	private final Tab tab_console;
	private final Table connectionTable;
	private Component lastConsoleComponentUsed;
	private final Label default_Component;
	
	/**
	 * Various other variables.
	 */
	private final Step3TableContainer dataSource;
	private final Map<Object, PikaterSSHLauncher> serverIDToLauncherMapping;
	
	public Step3(WelcomeTourWizard parentWizard)
	{
		super(parentWizard);
		
		this.dataSource = new Step3TableContainer(parentWizard.getWrappedModels());
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
			@Override
			public void buttonClick(ClickEvent event)
			{ 
				Collection<Object> connectedNotLaunchedMasterIDs = getConnectedNotLaunchedMasterServerIDs();
				if(!connectedNotLaunchedMasterIDs.isEmpty())
				{
					if(!areMastersLaunchedWithNoErrors(connectedNotLaunchedMasterIDs)) // updates GUI accordingly 
					{
						Notification.show("Pikater could not be launched on some of the masters. Either do nothing or use the console to find out what went wrong.", 
								Type.ERROR_MESSAGE);
					}
				}
				else
				{
					Notification.show("All connect masters have pikater launched. Connect some others and try again.", Type.WARNING_MESSAGE);
				}
			}
		});
		Button btn_checkAvailabilityOfIncludedServers = new Button("Connect to designated machines", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				Collection<Object> includedNotConnectedServerIDs = getIncludedNotConnectedServerIDs();
				if(!includedNotConnectedServerIDs.isEmpty())
				{
					// first check that all the included servers are reachable
					if(!areServersReachable(includedNotConnectedServerIDs, false)) // updates GUI accordingly
					{
						Notification.show("Some of the servers are unreachable. Exclude them or double check / repair their availability and connection information.", 
								Type.ERROR_MESSAGE);
					}
					if(!getConnectedServerIDs().isEmpty())
					{
						btn_launchPikaterOnConnectedServers.setEnabled(true);
					}
				}
				else
				{
					Notification.show("All included servers are connected. Include some others and try again.", Type.WARNING_MESSAGE);
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
		 * TODO - vertical TabPanel by topology - content would be all the servers in the topology, with the same table headers as now 
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
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				if(connectionTable.getValue() != null) // something is selected
				{
					Component newComponent = serverIDToLauncherMapping.get(connectionTable.getValue()).getOutputConsoleComponent();
					if(newComponent == null)
					{
						newComponent = default_Component;
					}
					else
					{
						newComponent.setHeight("100%");
					}
					
					tabPanel.replaceComponent(lastConsoleComponentUsed, newComponent);
					lastConsoleComponentUsed = newComponent;
					tabPanel.setSelectedTab(tab_console); // TODO: doesn't trigger the inner "selected tab changed event"
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
		
		this.default_Component = new Label();
		this.lastConsoleComponentUsed = this.default_Component;
		
		this.tabPanel = new TabSheet();
		this.tabPanel.addTab(vLayout, "Overview of connections");
		this.tabPanel.addTab(default_Component, "Console for selected connection");
		this.tab_console = this.tabPanel.getTab(lastConsoleComponentUsed);
		// this.tab_console.setEnabled(false);
		this.tabPanel.setHeight("550px");
		
		this.content = tabPanel;
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
			if(model.isWellFormed()) // at least 1 master has pikater launched and 1 slave is connected - also determined by the way which 'topologyView' is constructed 
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
			Notification.show("Can not finish because the goal was not met: pikater launched on at least 1 master and "
					+ "connection to at least 1 slave of a single topology is needed to continue.", Type.ERROR_MESSAGE);
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
	
	private Collection<Object> getIncludedNotConnectedServerIDs()
	{
		Set<Object> result = new HashSet<Object>();
		result.addAll(getIncludedServerIDs());
		result.removeAll(getConnectedServerIDs());
		return result;
	}
	
	private Collection<Object> getLaunchedOrConnectedServerIDs()
	{
		Set<Object> result = new HashSet<Object>();
		result.addAll(getLaunchedServerIDs());
		result.addAll(getConnectedServerIDs());
		return result;
	}
	
	private Collection<Object> getConnectedNotLaunchedMasterServerIDs()
	{
		Set<Object> result = new HashSet<Object>();
		result.addAll(filterByType(getConnectedServerIDs(), ServerType.MASTER));
		result.removeAll(filterByType(getLaunchedServerIDs(), ServerType.MASTER));
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
	
	private boolean areMastersLaunchedWithNoErrors(Collection<Object> masterIDs)
	{
		boolean result = true;
		for(Object rowID : masterIDs)
		{
			if(!serverIDToLauncherMapping.get(rowID).launchPikater()) // updates GUI accordingly - no custom actions needed
			{
				result = false;
			}
		}
		return result;
	}
}