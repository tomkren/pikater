package org.pikater.web.vaadin.tabledbview;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.QueryConstraints;
import org.pikater.web.vaadin.gui.server.components.paging.PagingComponent;
import org.pikater.web.vaadin.gui.server.components.paging.PagingComponent.IPagingComponentContext;
import org.pikater.web.vaadin.tabledbview.DBTableContainer.ISortableTableContainerContext;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Table;

@StyleSheet("dbTable.css")
public class DBTable extends Table implements ICommitable, IPagingComponentContext
{
	private static final long serialVersionUID = 6518669246548765191L;
	
	private final DBTableContainer tableContainer;
	private final PagingComponent pagingControls;

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
		setStyleName("dbTable");
		
		// first setup the paging controls, otherwise NullPointerException occurs
		this.pagingControls = new PagingComponent(this);
		
		// then setup the container
		this.tableContainer = new DBTableContainer(new ISortableTableContainerContext()
		{
			@Override
			public QueryConstraints getQuery()
			{
				return new QueryConstraints(
						(IColumn) getSortContainerPropertyId(),
						pagingControls.getOverallOffset(),
						pagingControls.getPageSize()
				);
			}
			
			@Override
			public Table getParentTable()
			{
				return DBTable.this;
			}
			
		}, dbView);
		
		// and finish initialization
		setContainerDataSource(tableContainer);
		setSortContainerPropertyId(defaultSortColumn);
		addHeaderClickListener(new HeaderClickListener()
		{
			private static final long serialVersionUID = -1276767165561401427L;

			@Override
			public void headerClick(HeaderClickEvent event)
			{
				IColumn column = (IColumn) event.getPropertyId();
				if(column.getColumnType().isSortable())
				{
					setSortContainerPropertyId(event.getPropertyId()); // Vaadin will not do this by itself... doh
				}
			}
		});
	}
	
	@Override
	public void setSortContainerPropertyId(Object propertyId)
	{
		IColumn column = (IColumn) propertyId;
		if(propertyId == null)
		{
			throw new NullPointerException("Can not set null sort column. A column to sort with must always be set.");
		}
		else if(!column.getColumnType().isSortable())
		{
			throw new IllegalArgumentException(String.format("The '%s' column is not sortable.", column.getDisplayName()));
		}
		
		Object lastSortPropertyID = getSortContainerPropertyId();
		/*
		 * The new sort column has to be set now since the {@link #rebuildContainerRowIndex()} method
		 * depends on it.
		 */
		super.setSortContainerPropertyId(propertyId);
		if((lastSortPropertyID == null) || !lastSortPropertyID.equals(propertyId))
		{
			/*
			 * Container row cache needs to be constructed before the Sortable interface is called. Vaadin
			 * doesn't do it, buggy little mischief...
			 */
			rebuildContainerRowIndex(true);
		}
	}
	
	@Override
	public void commitToDB()
	{
		setEnabled(false);
		if(!isImmediate())
		{
			commit(); // commits changes to properties which commit them to the view
		}
		tableContainer.commitToDB(); // commits changes to the database; changes are taken from the view
		setEnabled(true);
	}
	
	@Override
	public int getAllItemsCount()
	{
		return tableContainer.getUnconstrainedQueryResultsCount();
	}

	@Override
	public void onPageSizeChanged(int itemsPerPage)
	{
		rebuildContainerRowIndex(true);
		refreshRowCache();
	}

	@Override
	public void onPageChanged(int page)
	{
		rebuildContainerRowIndex(false);
		refreshRowCache();
	}
	
	public PagingComponent getPagingControls()
	{
		return pagingControls;
	}
	
	//-------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	/**
	 * Sets the table to display exactly the number of items that were fetched from the database.</br>
	 * As a side effect, refreshes the container's row index, just as the name of this method suggests.</br>
	 * Setting page lenth is important because otherwise, Vaadin table will display a fixed number of
	 * rows whether there are enough items to populate them or not.
	 */
	private void rebuildContainerRowIndex(boolean resetPagePicker)
	{
		/*
		 * Sets the table to display exactly the number of items that were fetched
		 * from the database.
		 * As a side effect, refreshes the container's row index, just as the
		 * name of this method suggests.
		 * Setting page lenth is important because otherwise, Vaadin table will
		 * display a fixed number of rows whether there are enough items to
		 * populate them or not.
		 */
		setPageLength(tableContainer.getItemIds().size());
		
		/*
		 * Additionally, paging controls may need to be reset (first page set and total amount of pages updated).
		 */
		if(resetPagePicker)
		{
			pagingControls.reset();
		}
	}
}