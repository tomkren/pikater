package org.pikater.web.vaadin.gui.server.components.experimenteditor;

import org.pikater.shared.experiment.webformat.SchemaDataSource;
import org.pikater.web.vaadin.MyResources;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;

public class ExperimentEditor extends VerticalLayout
{
	private static final long serialVersionUID = -3411515276069271598L;
	
	// -------------------------------------------------------------
	// INDIVIDUAL GUI COMPONENTS
	
	private final ExperimentEditorToolbar toolbar;
	private final TabSheet experimentTabs;
	private final BoxCellBrowser boxCellBrowser;
	
	// -------------------------------------------------------------
	// PROGRAMMATIC VARIABLES
	
	public ExperimentEditor(boolean debugMode)
	{
		super();
		setSizeFull();
		setSpacing(true);
		addStyleName("experiment-editor");
		
		this.toolbar = new ExperimentEditorToolbar(this, debugMode);
		this.toolbar.addStyleName("displayBorder");
		this.toolbar.setSizeUndefined();
		this.experimentTabs = new TabSheet();
		this.experimentTabs.addStyleName("displayBorder");
		this.experimentTabs.setSizeFull();
		this.boxCellBrowser = new BoxCellBrowser();
		this.boxCellBrowser.addStyleName("displayBorder");
		this.boxCellBrowser.setSizeUndefined();
		
		addComponent(this.toolbar);
		addComponent(this.experimentTabs);
		addComponent(this.boxCellBrowser);
		
		setExpandRatio(this.experimentTabs, 1);
		
		this.experimentTabs.setCloseHandler(new CloseHandler()
		{
			private static final long serialVersionUID = 9202309204180871550L;

			@Override
			public void onTabClose(TabSheet tabsheet, Component tabContent)
			{
				/*
				if(existsUnsavedContent())
				{
					closingEvent.setMessage("Are you sure? Editor content will be lost, if unsaved.");
				}
				else
				{
					tabsheet.removeComponent(tabContent);
				}
				*/
				tabsheet.removeComponent(tabContent);
			}
		});
		
		/*
		 * IPORTANT: don't alter the call order.
		 */
		final Panel dummyPannel = new Panel();
		this.experimentTabs.addSelectedTabChangeListener(new SelectedTabChangeListener()
		{
			private static final long serialVersionUID = -1341095599657850763L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event)
			{
				if(experimentTabs.getSelectedTab() == dummyPannel)
				{
					addEmptyTab();
					experimentTabs.setSelectedTab(experimentTabs.getComponentCount() - 2);
				}
				else
				{
					// TODO: reload toolbar
				}
			}
		});
		
		// the following tab is selected when added, which creates a new empty tab after that (see the selected tab change listener above)
		experimentTabs.addTab(dummyPannel, null, MyResources.img_plusIcon32).setDescription("Add a new tab.");
	}
	
	// -------------------------------------------------------------
	// PUBLIC INTERFACE
	
	public void addEmptyTab()
	{
		addTab("untitled", null);
	}
	
	public KineticComponent getActiveKineticComponent()
	{
		return ((WrappedKineticComponent) experimentTabs.getSelectedTab()).getWrappedComponent();
	}
	
	public void loadExperiment(SchemaDataSource experiment)
	{
		loadExperiment(getActiveKineticComponent(), experiment);
	}
	
	// -------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void addTab(String caption, SchemaDataSource experimentToLoad)
	{
		// define the new kinetic component and create the new tab:
		KineticComponent newKineticComponent = new KineticComponent(this);
		WrappedKineticComponent wrappedKineticComponent = new WrappedKineticComponent(newKineticComponent);
		experimentTabs.addTab(wrappedKineticComponent, caption, null, experimentTabs.getComponentCount() - 1).setClosable(true);
		
		// after that, load the given experiment, if any at all
		loadExperiment(newKineticComponent, experimentToLoad);
	}
	
	private static void loadExperiment(KineticComponent component, SchemaDataSource experiment)
	{
		if(experiment != null)
		{
			component.getClientRPC().loadExperiment(experiment);
		}
	}
}