package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.experiment.webformat.server.BoxType;
import org.pikater.shared.experiment.webformat.server.ExperimentGraphServer;
import org.pikater.web.sharedresources.ThemeResources;
import org.pikater.web.vaadin.gui.server.components.borderlayout.AutoVerticalBorderLayout;
import org.pikater.web.vaadin.gui.server.components.tabsheet.ITabSheetOwner;
import org.pikater.web.vaadin.gui.server.components.tabsheet.TabSheet;
import org.pikater.web.vaadin.gui.server.components.tabsheet.TabSheetTabComponent;
import org.pikater.web.vaadin.gui.server.components.toolbox.Toolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxbrowser.BoxBrowserToolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.BoxManagerToolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.IBoxManagerToolboxContext;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.KineticComponent;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.KineticDnDWrapper;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.utilcomponent.UtilitiesToolbox;
import org.pikater.web.vaadin.gui.shared.borderlayout.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.borderlayout.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.borderlayout.BorderLayoutUtil.Row;
import org.pikater.web.vaadin.gui.shared.borderlayout.Dimension;
import org.pikater.web.vaadin.gui.shared.borderlayout.Dimension.DimensionMode;
import org.pikater.web.vaadin.gui.shared.borderlayout.Dimension.DimensionUnit;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;

@StyleSheet("expEditor.css")
public class ExpEditor extends AutoVerticalBorderLayout implements ITabSheetOwner
{
	private static final long serialVersionUID = -3411515276069271598L;
	
	public enum ExpEditorToolbox
	{
		BOX_BROWSER,
		BOX_MANAGER,
		UTILITIES;
		
		public String toDisplayName()
		{
			switch(this)
			{
				case BOX_BROWSER:
					return "Available boxes";
				case BOX_MANAGER:
					return "Box manager";
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
				case BOX_BROWSER:
					return Border.WEST;
				case BOX_MANAGER:
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
				case BOX_BROWSER:
					return KeyCode.ARROW_LEFT;
				case BOX_MANAGER:
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
	private final BoxManagerToolbox toolbox_boxManager; // EAST
	private final UtilitiesToolbox toolbox_util; // SOUTH
	
	// -------------------------------------------------------------
	// PROGRAMMATIC VARIABLES
	
	private final ExpEditorExtension extension;
	
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
		this.toolbox_boxBrowser = new BoxBrowserToolbox(ExpEditorToolbox.BOX_BROWSER.toDisplayName(), new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 812989325500737028L;

			@Override
			public void click(ClickEvent event)
			{
				minimizeToolbox(ExpEditorToolbox.BOX_BROWSER);
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
		IBoxManagerToolboxContext contextForBoxManager = new IBoxManagerToolboxContext()
		{
			@Override
			public ExperimentGraphServer getCurrentGraph()
			{
				return getActiveKineticComponent().getExperimentGraph();
			}
		};
		this.toolbox_boxManager = new BoxManagerToolbox(contextForBoxManager, ExpEditorToolbox.BOX_MANAGER.toDisplayName(), new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1236473439175631916L;

			@Override
			public void click(ClickEvent event)
			{
				minimizeToolbox(ExpEditorToolbox.BOX_MANAGER);
			}
		});
		this.toolbox_boxManager.setStyleName("boxManagerToolbox");
		setComponent(Border.EAST, this.toolbox_boxManager);
		addColumnStyleName(Column.EAST, "boxManagerToolboxSize");
		
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
		setToolboxVisible(ExpEditorToolbox.BOX_MANAGER, false);
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
		
		// export all box images into a collection
		List<String> allPictureURLs = new ArrayList<String>();
		for(BoxType type : BoxType.values())
		{
			allPictureURLs.add(getBoxPictureURL(type));
		}
		
		// extend this component and load all images in GWT (cache in browser)
		this.extension = new ExpEditorExtension();
		this.extension.extend(this);
		this.extension.getClientRPC().command_loadBoxPictures(allPictureURLs.toArray(new String[0]));
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
	
	public static String getBoxPictureURL(BoxType type)
	{
		String imgRelativePath;
		switch(type)
		{
			case CHOOSE:
				imgRelativePath = ThemeResources.relPath_IMG_boxRecommenderIcon;
				break;
			case COMPUTE:
				imgRelativePath = ThemeResources.relPath_IMG_boxComputingIcon;
				break;
			case PROCESS_DATA:
				imgRelativePath = ThemeResources.relPath_IMG_boxDataProcessingIcon;
				break;
			case OPTION:
				imgRelativePath = ThemeResources.relPath_IMG_boxEvaluationIcon;
				break;
			case INPUT:
				imgRelativePath = ThemeResources.relPath_IMG_boxInputIcon;
				break;
			case MISC:
				imgRelativePath = ThemeResources.relPath_IMG_boxMiscellaneousIcon;
				break;
			case OUTPUT:
				imgRelativePath = ThemeResources.relPath_IMG_boxOutputIcon;
				break;
			case SEARCH:
				imgRelativePath = ThemeResources.relPath_IMG_boxSearcherIcon;
				break;
			case COMPOSITE:
				imgRelativePath = ThemeResources.relPath_IMG_boxWrapperIcon;
				break;
			default:
				throw new IllegalStateException("The following BoxType doesn't have image defined: " + type.name());
		}
		return ThemeResources.getVaadinRelativePathForResource(imgRelativePath);
	}
	
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
			case BOX_BROWSER:
				return toolbox_boxBrowser;
			case BOX_MANAGER:
				return toolbox_boxManager;
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
			case BOX_BROWSER:
				if(visible)
				{
					setColumnVisible(Column.WEST);
					if(toolbox_boxManager.isVisible())
					{
						setToolboxVisible(ExpEditorToolbox.BOX_MANAGER, false);
					}
				}
				else
				{
					setColumnInvisible(Column.WEST, Column.CENTER);
				}
				break;
			case BOX_MANAGER:
				if(visible)
				{
					setColumnVisible(Column.EAST);
					if(toolbox_boxBrowser.isVisible())
					{
						setToolboxVisible(ExpEditorToolbox.BOX_BROWSER, false);
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