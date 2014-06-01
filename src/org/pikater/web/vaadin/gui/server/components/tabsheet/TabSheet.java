package org.pikater.web.vaadin.gui.server.components.tabsheet;

import java.util.HashMap;
import java.util.Map;

import org.pikater.web.vaadin.MyResources;
import org.pikater.web.vaadin.gui.server.MyDialogs;
import org.pikater.web.vaadin.gui.server.MyDialogs.OnOkClicked;
import org.pikater.web.vaadin.gui.server.components.IconButton;
import org.pikater.web.vaadin.gui.server.components.borderlayout.AutoVerticalBorderLayout;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionMode;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Row;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

@StyleSheet("tabSheet.css")
public class TabSheet extends CustomComponent
{
	private static final long serialVersionUID = -4571956367761332160L;
	
	private final AutoVerticalBorderLayout innerLayout;
	private final HorizontalLayout tabBar;
	private final Panel defaultContent;
	private TabSheetTabComponent selectedTabComponent;

	private final Map<TabSheetTabComponent, Panel> tabToContentContainer;
	private final ITabSheetOwner owner;
	
	public TabSheet(ITabSheetOwner owner)
	{
		super();
		
		/*
		 * Inner component init. 
		 */
		
		this.tabBar = new HorizontalLayout();
		this.tabBar.setStyleName("custom-tabsheet-tabs");
		this.tabBar.setSpacing(true);

		/*
		 * Layout init.
		 */
		
		// basic layout setup - this layout will not be changed in any way during its lifetime so no need to store it in a field
		this.innerLayout = new AutoVerticalBorderLayout();
		this.innerLayout.setSizeFull();
		this.innerLayout.setStyleName("custom-tabsheet");
		this.innerLayout.setBorderSpacing(0);
		this.innerLayout.setRowVisible(Row.CENTER, false);
		this.innerLayout.setComponent(Border.NORTH, this.tabBar);
		this.innerLayout.setRowHeight(Row.SOUTH, DimensionMode.MAX);
		
		// this component's basic setup
		setCompositionRoot(innerLayout);
		setSizeFull();
		
		/*
		 * TabSheet init.
		 */
		
		this.defaultContent = createContentContainer(null);
		this.defaultContent.setEnabled(false);
		// note: don't violate the call order with the following:
		doAddTab(createAddTabComponent());
		
		/*
		 * Miscellaneous init. 
		 */
		
		this.selectedTabComponent = null;
		this.tabToContentContainer = new HashMap<TabSheetTabComponent, Panel>();
		this.owner = owner;
	}
	
	//---------------------------------------------------------------
	// PUBLIC INTERFACE
	
	public void addTab(TabSheetTabComponent tabComponent, AbstractComponent contentComponent)
	{
		prepareTabComponent(tabComponent);
		doAddTab(tabComponent);
		tabToContentContainer.put(tabComponent, createContentContainer(contentComponent));
		setSelectedTab(tabComponent);
	}
	
	public TabSheetTabComponent getSelectedTab()
	{
		return selectedTabComponent;
	}
	
	public void setSelectedTab(TabSheetTabComponent tabComponent)
	{
		if(selectedTabComponent != null)
		{
			selectedTabComponent.setSelected(false);
		}
		selectedTabComponent = tabComponent;
		tabComponent.setSelected(true);
		setSelectedContent(tabToContentContainer.get(tabComponent));
		owner.onTabSelectionChange();
	}
	
	//---------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void doAddTab(AbstractComponent newTab)
	{
		this.tabBar.addComponent(newTab, tabBar.getComponentCount() == 0 ? 0 : tabBar.getComponentCount() - 1);
		this.tabBar.setComponentAlignment(newTab, Alignment.MIDDLE_CENTER);
	}
	
	private AbstractComponent createAddTabComponent()
	{
		IconButton addTabButton = new IconButton(MyResources.img_plusIcon16);
		addTabButton.addClickListener(new com.vaadin.event.MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = -7054477583680936381L;

			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event)
			{
				owner.addEmptyTab();
			}
		});
		addTabButton.setStyleName("custom-tabsheet-tabs-tab-add");
		return addTabButton;
	}
	
	private void prepareTabComponent(final TabSheetTabComponent tabComponent)
	{
		tabComponent.addClickListener(new com.vaadin.event.MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = -3908357742920865682L;

			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event)
			{
				if(tabComponent != selectedTabComponent)
				{
					setSelectedTab(tabComponent);
				}
			}
		});
		tabComponent.addCloseHandler(new com.vaadin.event.MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = -8426832013522667216L;

			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event)
			{
				if(tabComponent.canCloseTab()) // ask whether we can close this tab
				{
					closeTab();
				}
				else // if not, give the user a chance to cancel the action
				{
					MyDialogs.createSimpleConfirmDialog(getUI(), "Really close this tab? The content will be lost, if unsaved.", new OnOkClicked()
					{
						/*
						 * If the user confirms, do the following: 
						 */
						
						@Override
						public boolean handleOkEvent()
						{
							closeTab();
							return true;
						}
					});
				}
			}
			
			private void closeTab()
			{
				// first handle the case if we remove the selected tab
				if(selectedTabComponent == tabComponent)
				{
					int currentPosition = tabBar.getComponentIndex(selectedTabComponent);
					if(currentPosition > 0)
					{
						setSelectedTab((TabSheetTabComponent) tabBar.getComponent(currentPosition - 1));
					}
					else if (currentPosition < tabBar.getComponentCount() - 2) // not the last tab
					{
						setSelectedTab((TabSheetTabComponent) tabBar.getComponent(currentPosition + 1));
					}
					else // no tabs to select anymore
					{
						setSelectedContent(null);
					}
				}
				
				// and then remove everything related to this tab
				tabToContentContainer.remove(tabComponent);
				tabBar.removeComponent(tabComponent);
			}
		});
		tabComponent.setStyleName("custom-tabsheet-tabs-tab");
	}
	
	private Panel createContentContainer(AbstractComponent content)
	{
		Panel result = new Panel();
		result.setSizeFull();
		result.setStyleName("custom-tabsheet-content-container");
		if(content != null)
		{
			result.setContent(content);
		}
		return result;
	}
	
	private void setSelectedContent(Panel content)
	{
		innerLayout.setComponent(Border.SOUTH, content != null ? content : defaultContent);
	}
}