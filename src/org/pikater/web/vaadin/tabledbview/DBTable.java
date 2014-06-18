package org.pikater.web.vaadin.tabledbview;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.QueryConstraints;
import org.pikater.web.vaadin.tabledbview.DBTableContainer.ISortableTableContainerContext;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Table;

@StyleSheet("dbTable.css")
public class DBTable extends Table implements ICommitable
{
	private static final long serialVersionUID = 6518669246548765191L;
	
	private final DBTableContainer tableContainer;

	public DBTable(AbstractTableDBView dbView, IColumn defaultSortColumn)
	{
		super();
		
		setImmediate(true);
		setEditable(true);
		setColumnReorderingAllowed(false);
		setColumnCollapsingAllowed(true);
		setSelectable(true);
		setMultiSelect(true);
		setSortEnabled(true);
		setPageLength(0); // display all rows without scrollbars, don't display empty rows
		setStyleName("dbTable");
		
		this.tableContainer = new DBTableContainer(new ISortableTableContainerContext()
		{
			@Override
			public QueryConstraints getQuery()
			{
				return new QueryConstraints((IColumn) getSortContainerPropertyId(), 0, 20); // TODO: offset and max results
			}
			
			@Override
			public Table getParentTable()
			{
				return DBTable.this;
			}
			
		}, dbView);
		setContainerDataSource(tableContainer);
		setSortContainerPropertyId(defaultSortColumn);
	}
	
	@Override
	public void commitToDB()
	{
		if(!isImmediate())
		{
			commit(); // commits changes to the container
		}
		tableContainer.commitToDB(); // commits changes to the database; changes are taken from the container
	}
	
	@Override
	public void setSortContainerPropertyId(Object propertyId)
	{
		if(propertyId == null)
		{
			throw new NullPointerException("Can not set null sort column. A column to sort with must always be set.");
		}
		
		Object lastSortPropertyID = getSortContainerPropertyId();
		if((lastSortPropertyID == null) || !lastSortPropertyID.equals(propertyId))
		{
			/*
			 * Container row cache needs to be constructed before the Sortable interface is called. Vaadin
			 * doesn't do it, buggy little mischief...
			 */
			tableContainer.getItemIds();
		}
		
		super.setSortContainerPropertyId(propertyId);
	}
}