package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.web.vaadin.gui.server.components.borderlayout.AutoVerticalBorderLayout;
import org.pikater.web.vaadin.gui.server.components.tabsheet.ITabSheetOwner;
import org.pikater.web.vaadin.gui.server.components.tabsheet.TabSheet;
import org.pikater.web.vaadin.gui.server.components.tabsheet.TabSheetTabComponent;
import org.pikater.web.vaadin.gui.server.components.toolbox.Toolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.KineticComponent;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.KineticDnDWrapper;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.toolboxes.BoxBrowserToolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.toolboxes.BoxOptionsToolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.toolboxes.UtilitiesToolbox;
import org.pikater.web.vaadin.gui.shared.borderlayout.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.borderlayout.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.borderlayout.BorderLayoutUtil.Row;
import org.pikater.web.vaadin.gui.shared.borderlayout.Dimension;
import org.pikater.web.vaadin.gui.shared.borderlayout.Dimension.DimensionMode;
import org.pikater.web.vaadin.gui.shared.borderlayout.Dimension.DimensionUnit;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.MouseEvents.ClickEvent;

@StyleSheet("expEditor.css")
public class ExpEditor extends AutoVerticalBorderLayout implements ITabSheetOwner
{
	private static final long serialVersionUID = -3411515276069271598L;
	
	public enum ExpEditorToolbox
	{
		METHOD_BROWSER,
		METHOD_OPTION_MANAGER,
		UTILITIES;
		
		public String toDisplayName()
		{
			switch(this)
			{
				case METHOD_BROWSER:
					return "Available methods";
				case METHOD_OPTION_MANAGER:
					return "Method options";
				case UTILITIES:
					return "Utilities";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		public Border toComponentPosition()
		{
			switch(this)
			{
				case METHOD_BROWSER:
					return Border.WEST;
				case METHOD_OPTION_MANAGER:
					return Border.EAST;
				case UTILITIES:
					return Border.SOUTH;
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		public int toKeyCode()
		{
			switch(this)
			{
				case METHOD_BROWSER:
					return KeyCode.ARROW_LEFT;
				case METHOD_OPTION_MANAGER:
					return KeyCode.ARROW_RIGHT;
				case UTILITIES:
					return KeyCode.ARROW_DOWN;
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}
	
	// -------------------------------------------------------------
	// INDIVIDUAL GUI COMPONENTS
	
	private final Toolbar toolbar; // NORTH
	private final BoxBrowserToolbox toolbox_boxBrowser; // WEST
	private final TabSheet experimentTabs; // CENTER
	private final BoxOptionsToolbox toolbox_boxOptions; // EAST
	private final UtilitiesToolbox toolbox_util; // SOUTH
	
	// -------------------------------------------------------------
	// PROGRAMMATIC VARIABLES
	
	private final ExpEditorExtension extension;
	
	/*
	 * TODO: 
	 * - custom D&D component to mark the "drop place" visually? 
	 */
	
	public ExpEditor(boolean debugMode)
	{
		super();
		setSizeFull();
		setStyleName("expEditor");
		setCellSpacing(3);
		
		// NORTH COMPONENT INIT
		this.toolbar = new Toolbar(this, debugMode);
		this.toolbar.setSizeFull();
		this.toolbar.setStyleName("displayBorder");
		setComponent(Border.NORTH, this.toolbar);
		
		// WEST COMPONENT INIT
		this.toolbox_boxBrowser = new BoxBrowserToolbox(ExpEditorToolbox.METHOD_BROWSER.toDisplayName(), new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 812989325500737028L;

			@Override
			public void click(ClickEvent event)
			{
				minimizeToolbox(ExpEditorToolbox.METHOD_BROWSER);
			}
		});
		this.toolbox_boxBrowser.setStyleName("boxBrowserToolbox");
		setComponent(Border.WEST, this.toolbox_boxBrowser);
		addColumnStyleName(Column.WEST, "boxBrowserToolboxSize");
		
		// CENTER COMPONENT INIT
		this.experimentTabs = new TabSheet(this);
		this.experimentTabs.setSizeFull();
		this.experimentTabs.setStyleName("displayBorder");
		setComponent(Border.CENTER, this.experimentTabs);
		
		// EAST COMPONENT INIT
		this.toolbox_boxOptions = new BoxOptionsToolbox(ExpEditorToolbox.METHOD_OPTION_MANAGER.toDisplayName(), new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1236473439175631916L;

			@Override
			public void click(ClickEvent event)
			{
				minimizeToolbox(ExpEditorToolbox.METHOD_OPTION_MANAGER);
			}
		});
		this.toolbox_boxOptions.setStyleName("boxOptionsToolbox");
		setComponent(Border.EAST, this.toolbox_boxOptions);
		addColumnStyleName(Column.EAST, "boxOptionsToolboxSize");
		
		// SOUTH COMPONENT INIT
		this.toolbox_util = new UtilitiesToolbox(ExpEditorToolbox.UTILITIES.toDisplayName(), new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = -4668414159288469109L;

			@Override
			public void click(ClickEvent event)
			{
				minimizeToolbox(ExpEditorToolbox.UTILITIES);
			}
		});
		this.toolbox_util.setStyleName("utilitiesToolbox");
		setComponent(Border.SOUTH, this.toolbox_util);
		addRowStyleName(Row.SOUTH, "utilitiesToolboxSize");
		
		setRowHeight(Row.CENTER, new Dimension(DimensionMode.MAX));
		setColumnWidth(Column.CENTER, new Dimension(100, DimensionUnit.PCT));
		setToolboxVisible(ExpEditorToolbox.METHOD_OPTION_MANAGER, false);
		setToolboxVisible(ExpEditorToolbox.UTILITIES, false);
		
		for(final ExpEditorToolbox toolbox : ExpEditorToolbox.values())
		{
			addShortcutListener(new ShortcutListener("", toolbox.toKeyCode(), new int[] { ModifierKey.ALT })
			{
				private static final long serialVersionUID = 3317790314886446722L;

				@Override
				public void handleAction(Object sender, Object target)
				{
					openToolbox(toolbox);
				}
			});
		}
		
		this.extension = new ExpEditorExtension();
		this.extension.extend(this);
		this.extension.getClientRPC().command_loadBoxPictures(BoxType.getAllPictureURLs());
	}
	
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
	
	// -------------------------------------------------------------
	// PUBLIC INTERFACE
	
	public ExpEditorExtension getExtension()
	{
		return extension;
	}
	
	public Toolbar getToolbar()
	{
		return toolbar;
	}
	
	public Toolbox getToolbox(ExpEditorToolbox toolbox)
	{
		switch(toolbox)
		{
			case METHOD_BROWSER:
				return toolbox_boxBrowser;
			case METHOD_OPTION_MANAGER:
				return toolbox_boxOptions;
			case UTILITIES:
				return toolbox_util;
			default:
				throw new IllegalStateException("Unknown state: " + toolbox.name());
		}
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
	
	public void loadExperimentIntoNewTab(JPABatch experiment)
	{
		addTab(experiment.getName());
		getActiveKineticComponent().importExperiment(experiment);
	}
	
	public void openToolbox(ExpEditorToolbox toolbox)
	{
		setToolboxVisible(toolbox, true);
	}
	
	public void minimizeToolbox(ExpEditorToolbox toolbox)
	{
		setToolboxVisible(toolbox, false);
	}
	
	// -------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void addTab(String tabCaption)
	{
		KineticComponent contentComponent = new KineticComponent(this);
		experimentTabs.addTab(new CustomTabSheetTabComponent(tabCaption, contentComponent), new KineticDnDWrapper(contentComponent));
	}
	
	private void setToolboxVisible(ExpEditorToolbox toolbox, boolean visible)
	{
		switch(toolbox)
		{
			case METHOD_BROWSER:
				if(visible)
				{
					setColumnVisible(Column.WEST);
					if(toolbox_boxOptions.isVisible())
					{
						setToolboxVisible(ExpEditorToolbox.METHOD_OPTION_MANAGER, false);
					}
				}
				else
				{
					setColumnInvisible(Column.WEST, Column.CENTER);
				}
				break;
			case METHOD_OPTION_MANAGER:
				if(visible)
				{
					setColumnVisible(Column.EAST);
					if(toolbox_boxBrowser.isVisible())
					{
						setToolboxVisible(ExpEditorToolbox.METHOD_BROWSER, false);
					}
				}
				else
				{
					setColumnInvisible(Column.EAST, Column.CENTER);
				}
				break;
			case UTILITIES:
				if(visible)
				{
					setRowVisible(Row.SOUTH);
				}
				else
				{
					setRowInvisible(Row.SOUTH, Row.CENTER);
				}
				break;
			default:
				throw new IllegalStateException("Unknown state: " + toolbox.name());
		}
	}
}