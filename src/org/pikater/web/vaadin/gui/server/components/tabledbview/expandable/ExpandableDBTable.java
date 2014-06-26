package org.pikater.web.vaadin.gui.server.components.tabledbview.expandable;

import org.pikater.shared.database.views.base.IExpandableDBView;
import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.tabledbview.DBTable;
import org.pikater.web.vaadin.gui.server.components.tabledbview.DBTableContainer;
import org.pikater.web.vaadin.gui.server.components.tabledbview.views.AbstractTableGUIView;

import com.vaadin.data.Property;

public class ExpandableDBTable extends DBTable
{
	private static final long serialVersionUID = 2398378434116761979L;
	
	public ExpandableDBTable(AbstractTableGUIView<? extends AbstractTableDBView> dbView, final IDBTableLayoutOwnerExpandable layoutOwner)
	{
		super(dbView);
		
		// override selection properties from parent
		setSelectable(true);
		setMultiSelect(false);
		
		addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = -7913408841414105618L;

			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				Object itemID = event.getProperty().getValue();
				if(itemID != null)
				{
					// inheritance ensures that underlying container is always of this type
					DBTableContainer container = (DBTableContainer) getContainerDataSource();
					
					// overriding the {@link #rowsExpandIntoOtherViews()} method ensures the row view implements the interface
					IExpandableDBView rowView = (IExpandableDBView) container.getItem(itemID).getRowView();
					
					// only expansion check remains
					AbstractTableDBView nextTableView = rowView.getNextView();
					if(nextTableView != null)
					{
						layoutOwner.switchToView(nextTableView);
					}
					else
					{
						MyNotifications.showInfo(null, "No additional view provided.");
					}
				}
			}
		});
	}
	
	@Override
	public boolean rowsExpandIntoOtherViews()
	{
		return true;
	}
}