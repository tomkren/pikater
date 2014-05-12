package org.pikater.web.vaadin.gui.server.components.cellbrowser;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.HorizontalLayout;

public class CellBrowser extends HorizontalLayout
{
	private static final long serialVersionUID = 4542038527458028778L;
	
	// -----------------------------------------------------------------
	// FIELDS
	
	private static final int level_root = 0;
	
	private final ICellBrowserTreeViewModel viewModel;
	private final Map<Integer, CellBrowserColumnInfoWrapper> columnInfos;
	private final Map<CellBrowserCellSource, CellBrowserCellProvider> sourceToChildColumnComponentsMapping;
	private final CellBrowserCellSource innerRootValue;
	
	private final ClickListener sharedCellClickListener = new ClickListener()
	{
		private static final long serialVersionUID = -1323970518606352430L;

		@Override
		public void click(ClickEvent event)
		{
			// some prerequisites
			CellBrowserCell sourceCell = (CellBrowserCell) event.getSource();
			CellBrowserColumn parentColumn = (CellBrowserColumn) sourceCell.getParent();
			int sourceCellsLevel = parentColumn.getCellBrowserLevel();
			
			// actually do stuff
			if(selectedCellAtLevelEquals(sourceCellsLevel, sourceCell))
			{
				// do nothing
			}
			else
			{
				// first clear selection and cells in subsequent levels
				for(int level = sourceCellsLevel + 1; level < columnInfos.size(); level++)
				{
					CellBrowserColumnInfoWrapper columnInfo = columnInfos.get(level);
					columnInfo.column.removeAllComponents();
					if(columnInfo.isACellSelected())
					{
						columnInfo.cellUnselected();
					}
					else
					{
						break; // no more levels are displayed anyway
					}
				}
				
				// and then expand and select accordingly
				selectCellAndExpand(sourceCell, sourceCellsLevel);
			}
		}
	};
	
	// -----------------------------------------------------------------
	// CONSTRUCTOR
	
	public CellBrowser(ICellBrowserTreeViewModel viewModel, Object userRootValue)
	{
		super();
		
		this.addStyleName("cellbrowser");
		
		this.viewModel = viewModel;
		this.columnInfos = new HashMap<Integer, CellBrowserColumnInfoWrapper>();
		this.sourceToChildColumnComponentsMapping = new HashMap<CellBrowserCellSource, CellBrowserCellProvider>();
		this.innerRootValue = new CellBrowserCellSource(userRootValue);
		
		recursivelyConstructComponentTree(level_root, innerRootValue, null);
		registerComponents();
	}
	
	// -----------------------------------------------------------------
	// COMPONENT CONSTRUCTION METHODS
	
	private void recursivelyConstructComponentTree(int level, CellBrowserCellSource parentSource, CellBrowserCell parentCell)
	{
		/*
		 * IMPORTANT: at this point we assume that none of the previous levels was a leaf level (viewModel.isValueInLeafColumn()).
		 */
		
		// we do not know how many columns we will eventually create, so only define a column if necessary:
		CellBrowserColumn column = null;
		if(columnInfos.containsKey(level)) // column already defined
		{
			column = columnInfos.get(level).column; // use it
		}
		else // column not yet defined
		{
			// define it
			column = new CellBrowserColumn(level);
			this.columnInfos.put(level, new CellBrowserColumnInfoWrapper(column));
		}
		
		// fetch and construct all information and cells for this level
		ICellBrowserCellProvider infoForThisLevel = viewModel.getChildInfoForSource(parentSource.source);
		CellBrowserCellProvider cellProviderForThisLevel = new CellBrowserCellProvider(infoForThisLevel, sharedCellClickListener);
		
		// a little "flashback" - mark previous level's cell as leaf if no child sources are provided
		if((parentCell != null) && cellProviderForThisLevel.getCells().isEmpty())
		{
			parentCell.setLeaf();
		}
		
		// register created cells to the inner map for nice and quick future reference
		sourceToChildColumnComponentsMapping.put(parentSource, cellProviderForThisLevel);
		
		/*
		 * Recursively continue iterating until we arrive at the leaf level.
		 * 
		 * We have to iterate over the already created child cells, because they contain generated
		 * source objects. If we were to use "childInfoForCurrentValue.getSourceObjects()", we would
		 * create a new collection of these objects and references from the created children would be
		 * useless.
		 */
		for(CellBrowserCell cell : cellProviderForThisLevel.getCells())
		{
			if(!viewModel.isValueInLeafColumn(cell.getSourceObject().source)) // condition preventing stack overflow
			{
				recursivelyConstructComponentTree(level + 1, cell.getSourceObject(), cell);
			}
			else
			{
				cell.setLeaf();
			}
		}
	}
	
	private void registerComponents()
	{
		// add auto-sized columns to the cell browser - note that if they have no child components, they won't be visible
		for(CellBrowserColumnInfoWrapper columnInfo : columnInfos.values())
		{
			addComponent(columnInfo.column);
		}
		
		// fill the root-level column by default
		setComponentsAtLevelForSource(level_root, innerRootValue);
	}
	
	private void selectCellAndExpand(CellBrowserCell sourceCell, int sourceCellsLevel)
	{
		// expand if not at the leaf level
		if(sourceCellsLevel != (columnInfos.size() - 1))
		{
			setComponentsAtLevelForSource(sourceCellsLevel + 1, sourceCell.getSourceObject());
		}

		// and select the new cell
		columnInfos.get(sourceCellsLevel).cellSelected(sourceCell);
	}
	
	// -----------------------------------------------------------------
	// MISCELLANOUS ROUTINES
	
	private void setComponentsAtLevelForSource(int level, CellBrowserCellSource source)
	{
		columnInfos.get(level).column.setComponents(sourceToChildColumnComponentsMapping.get(source));
	}
	
	private boolean selectedCellAtLevelEquals(int level, CellBrowserCell cell)
	{
		return columnInfos.get(level).getSelectedCell() == cell;
	}
}