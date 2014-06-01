package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.shared.util.CustomOrderSet;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.components.cellbrowser.CellBrowser;
import org.pikater.web.vaadin.gui.server.components.cellbrowser.ICellBrowserTreeViewModel;
import org.pikater.web.vaadin.gui.server.components.cellbrowser.ICellBrowserCellProvider;
import org.pikater.web.vaadin.gui.server.components.cellbrowser.CellBrowser.CellBrowserDragSelection;
import org.pikater.web.vaadin.gui.server.components.cellbrowser.cell.CellBrowserCellSource;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class BoxCellBrowser extends CustomComponent
{
	private static final long serialVersionUID = -3713343452343142549L;

	public BoxCellBrowser()
	{
		super();
		
		ICellBrowserTreeViewModel viewModel = new ICellBrowserTreeViewModel()
		{
			private static final long serialVersionUID = -6017232301646386206L;

			@Override
			public ICellBrowserCellProvider getChildInfoForSource(final Object value)
			{
				if (value == null) // we are asking for the first cell browser column
				{
					return new ICellBrowserCellProvider()
					{
						private static final long serialVersionUID = 2631696253310693866L;

						@Override
						public CustomOrderSet<CellBrowserCellSource> getSourceObjects()
						{
							Collection<CellBrowserCellSource> unsortedObjects = new ArrayList<CellBrowserCellSource>();
							for(BoxType type : BoxType.values())
							{
								unsortedObjects.add(new CellBrowserCellSource(type));
							}
							return new CustomOrderSet<CellBrowserCellSource>(unsortedObjects, new Comparator<CellBrowserCellSource>()
							{
								@Override
								public int compare(CellBrowserCellSource o1, CellBrowserCellSource o2)
								{
									BoxType bt1 = (BoxType) o1.source;
									BoxType bt2 = (BoxType) o2.source;
									return bt1.name().compareTo(bt2.name()); // sort alphabetically
								}
							});
						}

						@Override
						public AbstractComponent getComponentForSource(CellBrowserCellSource source)
						{
							BoxType type = (BoxType) source.source;
							int boxCountForThisType = ServerConfigurationInterface.getLatestBoxDefinitions().getByType(type).size();
							return new Label(String.format("%s (%d)", type.name(), boxCountForThisType));
						}
					};
				}
				else if(value instanceof BoxType) // we are asking for the second cell browser column
				{
					return new ICellBrowserCellProvider()
					{
						private static final long serialVersionUID = -1466084171199940432L;

						@Override
						public CustomOrderSet<CellBrowserCellSource> getSourceObjects()
						{
							Collection<CellBrowserCellSource> unsortedObjects = new ArrayList<CellBrowserCellSource>();
							for(BoxInfo info : ServerConfigurationInterface.getLatestBoxDefinitions().getByType((BoxType) value))
							{
								unsortedObjects.add(new CellBrowserCellSource(info));
							}
							return new CustomOrderSet<CellBrowserCellSource>(unsortedObjects,
									new Comparator<CellBrowserCellSource>()
									{
										@Override
										public int compare(CellBrowserCellSource o1, CellBrowserCellSource o2)
										{
											BoxInfo i1 = (BoxInfo) o1.source;
											BoxInfo i2 = (BoxInfo) o2.source;
											return i1.getName().compareTo(i2.getName()); // sort alphabetically by name
										}
									}
							);
						}

						@Override
						public AbstractComponent getComponentForSource(CellBrowserCellSource source)
						{
							BoxInfo info = (BoxInfo) source.source;
							Label result = new Label(info.getName());
							result.setDescription(info.getDescription()); // tooltip
							return result;
						}
					};
				}
				else
				{
					throw new IllegalArgumentException(String.format("No column provider defined for source type '%s'.", value.getClass().getName()));
				}
			}
			
			@Override
			public boolean isValueInLeafColumn(Object value)
			{
				if(value == null)
				{
					return false;
				}
				else
				{
					return (value instanceof BoxInfo);
				}
			}
		};
		
		setCompositionRoot(new CellBrowser(viewModel, null, CellBrowserDragSelection.LAST_LEVEL_LEAF_CELLS));
	}
}
