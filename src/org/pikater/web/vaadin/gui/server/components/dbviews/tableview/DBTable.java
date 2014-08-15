package org.pikater.web.vaadin.gui.server.components.dbviews.tableview;

import java.util.HashSet;
import java.util.Set;

import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.SortOrder;
import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.util.collections.CustomOrderSet;
import org.pikater.web.vaadin.gui.server.components.dbviews.IDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.paging.PagingComponent;
import org.pikater.web.vaadin.gui.server.components.paging.PagingComponent.IPagedComponent;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Table;

@StyleSheet("dbTable.css")
public class DBTable extends Table implements IDBTableContainerContext, IPagedComponent, ICommitable
{
	private static final long serialVersionUID = 6518669246548765191L;
	
	private final DBTableContainer tableContainer;
	private final PagingComponent pagingControls;
	private ITableColumn currentSortColumn;
	private SortOrder currentSortOrder;

	public DBTable()
	{
		super();
		
		setImmediate(true); // this is counted on in {@link DBTableLayout}
		setEditable(true);
		setColumnReorderingAllowed(false);
		setColumnCollapsingAllowed(true);
		setSelectable(true);
		setMultiSelect(true);
		setSortEnabled(true);
		setStyleName("dbTable");
		
		// don't violate the call order
		this.pagingControls = new PagingComponent(this);
		this.tableContainer = new DBTableContainer(this);
		this.currentSortColumn = null;
	}
	
	//-------------------------------------------------------------------
	// INHERITED TABLE INTERFACE
	
	@Override
	public DBTableContainer getContainerDataSource()
	{
		return tableContainer;
	}
	
	@Override
	public void setSortContainerPropertyId(Object propertyId)
	{
		ITableColumn newSortColumn = (ITableColumn) propertyId;
		if(newSortColumn == null)
		{
			throw new NullPointerException("Can not set null sort column. A column to sort with must always be set.");
		}
		else if(!newSortColumn.getColumnType().isSortable())
		{
			throw new IllegalArgumentException(String.format("The '%s' column is not sortable.", newSortColumn.getDisplayName()));
		}
		
		/*
		 * The new sort column has to be set now since the {@link #rebuildContainerRowIndex()} method
		 * depends on it.
		 */
		if(newSortColumn != currentSortColumn)
		{
			currentSortColumn = newSortColumn;
			currentSortOrder = SortOrder.ASCENDING;
		}
		else
		{
			currentSortOrder = currentSortOrder.invert();
		}
		
		/*
		 * Container row cache needs to be constructed before the Sortable interface is called. Vaadin
		 * doesn't do it, buggy little mischief...
		 */
		resetPaging(); // requires the above call (so that the subsequent database query is correct)
	}
	
	//-------------------------------------------------------------------
	// INHERITED CONTAINER RELATED INTERFACE
	
	@Override
	public QueryConstraints getQuery()
	{
		return new QueryConstraints(
				currentSortColumn,
				currentSortOrder,
				pagingControls.getOverallOffset(),
				pagingControls.getPageSize()
		);
	}
	
	//-------------------------------------------------------------------
	// INHERITED PAGING RELATED INTERFACE
	
	/* 
	 * Setting page length is important because otherwise, Vaadin table will
	 * display a fixed number of rows whether there are enough items to
	 * populate them or not.
	 * This assumes that the container respects the maximum number of results
	 * determined by paging.
	 */
	
	@Override
	public void onPageChanged(int page)
	{
		rebuildRowCache();
	}
	
	@Override
	public void onPageSizeChanged(int itemsPerPage)
	{
		resetPaging();
	}

	//-------------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	
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
	
	//-------------------------------------------------------------------
	// SELECTION RELATED INTERFACE
	
	@SuppressWarnings("unchecked")
	public Set<Object> getSelectedRowIDs()
	{
		Set<Object> result = new HashSet<Object>();
		final Object selectedRowIDs = getValue();
		if(selectedRowIDs != null)
		{
			if(isMultiSelect())
			{
				result.addAll((Set<Object>) selectedRowIDs);
			}
			else if(isSelectable())
			{
				result.add(selectedRowIDs); // a single value
			}
		}
		return result;
	}
	
	public boolean isARowSelected()
	{
		return !getSelectedRowIDs().isEmpty();
	}
	
	public AbstractTableRowDBView[] getViewsOfSelectedRows()
	{
		CustomOrderSet<Object> sortedSelectedItemSet = new CustomOrderSet<Object>(getSelectedRowIDs());
			
		AbstractTableRowDBView[] result = new AbstractTableRowDBView[sortedSelectedItemSet.size()];
		int index = 0;
		for(Object itemID : sortedSelectedItemSet)
		{
			result[index] = tableContainer.getItem(itemID).getRowView();
			index++;
		}
		return result;
	}
	
	//-------------------------------------------------------------------
	// OTHER PUBLIC INTERFACE

	/**
	 * The most important method. Without this, the table is but an empty shell.
	 * @param viewRoot
	 */
	public void setView(IDBViewRoot<? extends AbstractTableDBView> viewRoot)
	{
		// this must be first so that column collapsing works...
		tableContainer.setViewRoot(viewRoot);
		setContainerDataSource(tableContainer);

		// basic setup of columns
		Set<ITableColumn> allDefinedColumns = viewRoot.getUnderlyingDBView().getAllColumns();
		for(ITableColumn column : allDefinedColumns)
		{
			setColumnHeader(column, column.getDisplayName());
			setColumnAlignment(column, Align.CENTER);
			setColumnWidth(column, viewRoot.getColumnSize(column));
			setColumnCollapsible(column, true);
			setColumnCollapsed(column, true);
		}
		for(ITableColumn column : viewRoot.getUnderlyingDBView().getDefaultColumns())
		{
			setColumnCollapsed(column, false);
		}
		addHeaderClickListener(new HeaderClickListener()
		{
			private static final long serialVersionUID = -1276767165561401427L;

			@Override
			public void headerClick(HeaderClickEvent event)
			{
				ITableColumn column = (ITableColumn) event.getPropertyId();
				if(column.getColumnType().isSortable())
				{
					setSortContainerPropertyId(event.getPropertyId()); // Vaadin will not do this by itself... doh
				}
			}
		});

		// expand ratio of columns
		if((viewRoot.getExpandColumn() == null) || !allDefinedColumns.contains(viewRoot.getExpandColumn()))
		{
			// distribute available space evenly
			float expandRatio = 1 / (float) allDefinedColumns.size();
			for(ITableColumn column : allDefinedColumns)
			{
				setColumnExpandRatio(column, expandRatio);
			}
		}
		else
		{
			// one column takes up all the available space
			setColumnExpandRatio(viewRoot.getExpandColumn(), 1);
		}

		// this will rebuild the container row cache
		setSortContainerPropertyId(viewRoot.getUnderlyingDBView().getDefaultSortOrder());
	}
	
	public PagingComponent getPagingControls()
	{
		return pagingControls;
	}
	
	//-------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void resetPaging()
	{
		pagingControls.setPage(1, false);
		rebuildRowCache();
		pagingControls.updatePageCount(tableContainer.getUnconstrainedQueryResultsCount());
	}
	
	private void rebuildRowCache()
	{
		setPageLength(tableContainer.getItemIds().size());
		refreshRowCache();
	}
}