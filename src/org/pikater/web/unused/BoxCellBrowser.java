package org.pikater.web.unused;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.shared.util.collections.CustomOrderSet;
import org.pikater.web.experiment.client.BoxInfoClient;
import org.pikater.web.experiment.server.BoxType;
import org.pikater.web.vaadin.gui.server.layouts.cellbrowser.CellBrowser;
import org.pikater.web.vaadin.gui.server.layouts.cellbrowser.ICellBrowserCellProvider;
import org.pikater.web.vaadin.gui.server.layouts.cellbrowser.ICellBrowserTreeViewModel;
import org.pikater.web.vaadin.gui.server.layouts.cellbrowser.CellBrowser.CellBrowserDragSelection;
import org.pikater.web.vaadin.gui.server.layouts.cellbrowser.cell.CellBrowserCellSource;

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
							// BoxType type = (BoxType) source.source;
							// int boxCountForThisType = ServerConfigurationInterface.getKnownAgents().getListByType(type).size();
							// return new Label(String.format("%s (%d)", type.name(), boxCountForThisType));
							return null;
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
							/*
							for(AgentInfo info : ServerConfigurationInterface.getKnownAgents().getListByType((BoxType) value))
							{
								unsortedObjects.add(new CellBrowserCellSource(info));
							}
							*/
							return new CustomOrderSet<CellBrowserCellSource>(unsortedObjects,
									new Comparator<CellBrowserCellSource>()
									{
										@Override
										public int compare(CellBrowserCellSource o1, CellBrowserCellSource o2)
										{
											AgentInfo i1 = (AgentInfo) o1.source;
											AgentInfo i2 = (AgentInfo) o2.source;
											return i1.getName().compareTo(i2.getName()); // sort alphabetically by name
										}
									}
							);
						}

						@Override
						public AbstractComponent getComponentForSource(CellBrowserCellSource source)
						{
							AgentInfo info = (AgentInfo) source.source;
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
					return value instanceof BoxInfoClient;
				}
			}
		};
		
		setCompositionRoot(new CellBrowser(viewModel, null, CellBrowserDragSelection.LAST_LEVEL_LEAF_CELLS));
	}
}
