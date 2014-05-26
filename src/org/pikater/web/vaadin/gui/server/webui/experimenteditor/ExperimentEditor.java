package org.pikater.web.vaadin.gui.server.webui.experimenteditor;

import org.pikater.shared.experiment.webformat.Experiment;
import org.pikater.web.vaadin.gui.server.components.borderlayout.AutoVerticalBorderLayout;
import org.pikater.web.vaadin.gui.server.components.kineticcomponent.KineticComponent;
import org.pikater.web.vaadin.gui.server.components.kineticcomponent.KineticDnDWrapper;
import org.pikater.web.vaadin.gui.server.components.tabsheet.ITabSheetOwner;
import org.pikater.web.vaadin.gui.server.components.tabsheet.TabSheet;
import org.pikater.web.vaadin.gui.server.components.tabsheet.TabSheetTabComponent;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionMode;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Row;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

@StyleSheet("experimentEditor.css")
public class ExperimentEditor extends AutoVerticalBorderLayout implements ITabSheetOwner
{
	private static final long serialVersionUID = -3411515276069271598L;
	
	// -------------------------------------------------------------
	// INDIVIDUAL GUI COMPONENTS
	
	private final Toolbar toolbar;
	private final TabSheet experimentTabs;
	private final BoxCellBrowser boxCellBrowser;
	
	// -------------------------------------------------------------
	// PROGRAMMATIC VARIABLES
	
	/*
	 * TODO: 
	 * - special toolbox overlay?
	 * - custom D&D component to mark the "drop place" visually? 
	 */ 
	
	public ExperimentEditor(boolean debugMode)
	{
		super();
		setSizeFull();
		setStyleName("experiment-editor");
		setCellSpacing(3);
		
		this.toolbar = new Toolbar(this, debugMode);
		this.toolbar.setSizeFull();
		this.toolbar.setStyleName("displayBorder");
		this.experimentTabs = new TabSheet(this);
		this.experimentTabs.setSizeFull();
		this.experimentTabs.setStyleName("displayBorder");
		this.boxCellBrowser = new BoxCellBrowser();
		this.boxCellBrowser.setSizeUndefined();
		this.boxCellBrowser.setStyleName("displayBorder");
		
		setComponent(Border.NORTH, this.toolbar);
		
		Panel panel = new Panel(new Label("poliket"));
		panel.setSizeFull();
		panel.setStyleName("displayBorder");
		setComponent(Border.WEST, panel);
		
		setComponent(Border.CENTER, this.experimentTabs);
		
		panel = new Panel(new Label("poliket"));
		panel.setSizeFull();
		panel.setStyleName("displayBorder");
		setComponent(Border.EAST, panel);
		
		setComponent(Border.SOUTH, this.boxCellBrowser);
		setRowHeight(Row.CENTER, DimensionMode.MAX);
		setColumnWidth(Column.CENTER, DimensionMode.MAX);
	}
	
	// -------------------------------------------------------------
	// PUBLIC INTERFACE
	
	@Override
	public void addEmptyTab()
	{
		addTab("untitled");
	}
	
	@Override
	public void onTabSelectionChange()
	{
		toolbar.onTabSelectionChange(getActiveKineticComponent());
	}
	
	public Toolbar getToolbar()
	{
		return toolbar;
	}
	
	public CustomTabSheetTabComponent getActiveTab()
	{
		TabSheetTabComponent tabComponent = experimentTabs.getSelectedTab();
		if(tabComponent != null)
		{
			return (CustomTabSheetTabComponent) tabComponent; 
		}
		else
		{
			return null;
		}
	}
	
	public KineticComponent getActiveKineticComponent()
	{
		CustomTabSheetTabComponent activeTab = getActiveTab();
		if(activeTab != null)
		{
			return (KineticComponent) getActiveTab().getContentComponent();
		}
		else
		{
			return null;
		}
	}
	
	public void loadExperimentIntoNewTab(String tabCaption, Experiment experiment)
	{
		if(experiment != null)
		{
			addTab(tabCaption);
			getActiveKineticComponent().getClientRPC().command_receiveExperimentToLoad(experiment);
		}
		else
		{
			throw new NullPointerException("Can not load a 'null' experiment.");
		}
	}
	
	// -------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void addTab(String tabCaption)
	{
		KineticComponent contentComponent = new KineticComponent(this);
		experimentTabs.addTab(new CustomTabSheetTabComponent(tabCaption, contentComponent), new KineticDnDWrapper(contentComponent));
	}
}