package org.pikater.web.vaadin.gui.server.layouts.cellbrowser;

import java.util.HashMap;
import java.util.Map;

import org.pikater.web.vaadin.gui.server.layouts.cellbrowser.cell.CellBrowserCell;
import org.pikater.web.vaadin.gui.server.layouts.cellbrowser.cell.CellBrowserCellSource;
import org.pikater.web.vaadin.gui.server.layouts.cellbrowser.cell.DraggableCellBrowserCell;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.HorizontalLayout;

@StyleSheet("cellBrowser.css")
public class CellBrowser extends HorizontalLayout
{
	private static final long serialVersionUID = 4542038527458028778L;
	
	public enum CellBrowserDragSelection
	{
		NONE,
		ALL_CELLS,
		LEAF_CELLS,
		LAST_LEVEL_LEAF_CELLS;
	}
	
	// -----------------------------------------------------------------
	// FIELDS
	
	private static final int level_root = 0;
	
	private final CellBrowserDragSelection dragSelection;
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
			CellBrowserColumn parentColumn = (CellBrowserColumn) ((DraggableCellBrowserCell) sourceCell.getParent()).getParent();
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
	
	public CellBrowser(ICellBrowserTreeViewModel viewModel, Object userRootValue, CellBrowserDragSelection dragSelection)
	{
		super();
		
		this.addStyleName("cellbrowser");
		
		this.dragSelection = dragSelection;
		this.viewModel = viewModel;
		this.columnInfos = new HashMap<Integer, CellBrowserColumnInfoWrapper>();
		this.sourceToChildColumnComponentsMapping = new HashMap<CellBrowserCellSource, CellBrowserCellProvider>();
		this.innerRootValue = new CellBrowserCellSource(userRootValue);
		
		recursivelyConstructComponentTree(level_root, innerRootValue);
		registerComponents();
	}
	
	// -----------------------------------------------------------------
	// COMPONENT CONSTRUCTION METHODS
	
	private void recursivelyConstructComponentTree(final int level, final CellBrowserCellSource parentSource)
	{
		/*
		 * IMPORTANT: at this point we assume that none of the previous levels was a leaf level (viewModel.isValueInLeafColumn()).
		 */
		
		// we do not know how many columns we will eventually create, so only define a column if necessary:
		if(!columnInfos.containsKey(level)) // column not defined yet
		{
			this.columnInfos.put(level, new CellBrowserColumnInfoWrapper(new CellBrowserColumn(level)));
		}
		
		// fetch and construct all information and cells for this level
		ICellBrowserCellProvider infoForThisLevel = viewModel.getChildInfoForSource(parentSource.source);
		CellBrowserCellProvider cellProviderForThisLevel = new CellBrowserCellProvider(infoForThisLevel, sharedCellClickListener, dragSelection);
		
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
		for(DraggableCellBrowserCell dragWrapperForCell : cellProviderForThisLevel.getChildCells())
		{
			CellBrowserCell cell = dragWrapperForCell.getWrappedCell();
			boolean dontExpandAnymore = viewModel.isValueInLeafColumn(cell.getSourceObject().source);
			if(!dontExpandAnymore) // do still expand
			{
				recursivelyConstructComponentTree(level + 1, cell.getSourceObject());
			}
			
			postProcessCell(dragWrapperForCell, cell, dontExpandAnymore);
		}
	}
	
	private void postProcessCell(DraggableCellBrowserCell dragWrapper, CellBrowserCell cell, boolean isCellInLeafColumn)
	{
		// this is pretty self-explanatory
		boolean isCellLeaf = isCellInLeafColumn || sourceToChildColumnComponentsMapping.get(cell.getSourceObject()).noChildCellsDefined(); 
		if(isCellLeaf)
		{
			cell.setLeaf();
		}
		
		// remove D&D functionality from cell if it is excluded by the desired drag selection
		switch (dragSelection)
		{
			case NONE: // cell IS NOT allowed to implement D&D:
				dragWrapper.disableDnD();
				break;
			case ALL_CELLS: // cell IS allowed to implement D&D and by default, it does... do nothing
				break;
			case LAST_LEVEL_LEAF_CELLS:
				if(!isCellInLeafColumn)
				{
					dragWrapper.disableDnD();
				}
				break;
			case LEAF_CELLS:
				if(!isCellLeaf)
				{
					dragWrapper.disableDnD();
				}
				break;
			default:
				throw new IllegalArgumentException(String.format("Drag selection '%s' not implemented yet.", dragSelection.name()));
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