package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.web.vaadin.gui.server.components.borderlayout.AutoVerticalBorderLayoutExtension;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionMode;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Row;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(AutoVerticalBorderLayoutExtension.class)
public class AutoVerticalBorderLayoutExtensionConnector extends AbstractExtensionConnector
{
	private static final long serialVersionUID = 6766120104518020715L;
	
	// private final MainUIExtensionServerRpc serverRPC = RpcProxy.create(MainUIExtensionServerRpc.class, this);
	
	private static final String firstRowStyleName = "avbl-firstRow";
	private static final String secondRowStyleName = "avbl-secondRow";
	private static final String thirdRowStyleName = "avbl-thirdRow";
	private static final String firstColumnStyleName = "avbl-firstColumn";
	private static final String secondColumnStyleName = "avbl-secondColumn";
	private static final String thirdColumnStyleName = "avbl-thirdColumn";
	
	private Element encapsulatingTable = null;
	
	private Element firstRow = null;
	private Element secondRow = null;
	private Element thirdRow = null;
	private Element firstColumn = null;
	private Element secondColumn = null;
	private Element thirdColumn = null;
	
	/*
	 * GENERAL IMPORTANT NOTE:
	 * The in-built style builder uses the "camelCase" format (no hyphens, underscores, spaces etc) so
	 * when using the "style.setProperty()" methods, make sure the property names are in that format.
	 * For example: "borderSpacing" builds into "border-spacing".
	 */

	public AutoVerticalBorderLayoutExtensionConnector()
	{
		registerRpc(AutoVerticalBorderLayoutExtensionClientRpc.class, new AutoVerticalBorderLayoutExtensionClientRpc()
		{
			private static final long serialVersionUID = 8760526187099873218L;

			@Override
			public void setRowHeight(final Row designatedRow, final DimensionMode dimMode)
			{
				Scheduler.get().scheduleDeferred(new ScheduledCommand()
				{
					@Override
				    public void execute()
					{
						if(dimMode == DimensionMode.MAX)
						{
							// set everything else to auto prior to setting the desired element to max
							for(Row row : Row.values())
							{
								if(row != designatedRow)
								{
									getElementByRow(row).getStyle().setProperty("height", DimensionMode.AUTO.toString());
								}
							}
						}
						getElementByRow(designatedRow).getStyle().setProperty("height", dimMode.toString());
					}
				});
			}

			@Override
			public void setColumnWidth(final Column designatedColumn, final DimensionMode dimMode)
			{
				Scheduler.get().scheduleDeferred(new ScheduledCommand()
				{
					@Override
				    public void execute()
					{
						if(dimMode == DimensionMode.MAX)
						{
							// set everything else to auto prior to setting the desired element to max
							for(Column column : Column.values())
							{
								if(column != designatedColumn)
								{
									getElementByColumn(column).getStyle().setProperty("width", DimensionMode.AUTO.toString());
								}
							}
						}
						getElementByColumn(designatedColumn).getStyle().setProperty("width", dimMode.toString());
					}
				});
			}
			
			@Override
			public void setRowVisible(final Row row, final boolean visible)
			{
				Scheduler.get().scheduleDeferred(new ScheduledCommand()
				{
					@Override
				    public void execute()
					{
						getElementByRow(row).getStyle().setVisibility(visible ? Visibility.VISIBLE : Visibility.HIDDEN);
					}
				});
			}

			@Override
			public void setColumnVisible(final Column column, final boolean visible)
			{
				Scheduler.get().scheduleDeferred(new ScheduledCommand()
				{
					@Override
				    public void execute()
					{
						getElementByColumn(column).getStyle().setVisibility(visible ? Visibility.VISIBLE : Visibility.HIDDEN);
					}
				});
			}

			@Override
			public void setComponentVisible(final Border border, final boolean visible)
			{
				Scheduler.get().scheduleDeferred(new ScheduledCommand()
				{
					@Override
				    public void execute()
					{
						getElementByBorder(border).getStyle().setVisibility(visible ? Visibility.VISIBLE : Visibility.HIDDEN);
					}
				});
			}
			
			@Override
			public void setBorderSpacing(final int pixels)
			{
				Scheduler.get().scheduleDeferred(new ScheduledCommand()
				{
					@Override
				    public void execute()
					{
						// encapsulatingTable.getStyle().setProperty("border-spacing", String.valueOf(pixels) + "px"); // hyphen format - GWT complains
						encapsulatingTable.getStyle().setProperty("borderSpacing", String.valueOf(pixels) + "px"); // camelCase format
					}
				});
			}

			@Override
			public void setCellSpacing(final int pixels)
			{
				Scheduler.get().scheduleDeferred(new ScheduledCommand()
				{
					@Override
				    public void execute()
					{
						encapsulatingTable.setAttribute("cellspacing", String.valueOf(pixels) + "px");
					}
				});
			}

			@Override
			public void setCellPadding(final int pixels)
			{
				Scheduler.get().scheduleDeferred(new ScheduledCommand()
				{
					@Override
				    public void execute()
					{
						encapsulatingTable.setAttribute("cellpadding", String.valueOf(pixels) + "px");
					}
				});
			}
		});
	}
	
	//---------------------------------------------------------------
	// INITIALIZATION CODE
	
	@Override
	protected void extend(ServerConnector target)
	{
		final Widget extendedWidget = ((ComponentConnector) target).getWidget();
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
		    public void execute()
			{
				// first crawl the structure and get references to elements of interest
				encapsulatingTable = extendedWidget.getElement().getFirstChildElement();
				checkElement(encapsulatingTable, null);
				
				firstRow = encapsulatingTable.getFirstChildElement().getFirstChildElement();
				checkElement(firstRow, firstRowStyleName);
				secondRow = firstRow.getNextSiblingElement();
				checkElement(secondRow, secondRowStyleName);
				thirdRow = secondRow.getNextSiblingElement();
				checkElement(thirdRow, thirdRowStyleName);
				
				firstColumn = secondRow.getFirstChildElement();
				checkElement(firstColumn, firstColumnStyleName);
				secondColumn = firstColumn.getNextSiblingElement();
				checkElement(secondColumn, secondColumnStyleName);
				thirdColumn = secondColumn.getNextSiblingElement();
				checkElement(thirdColumn, thirdColumnStyleName);
			}
		});
	}
	
	private void checkElement(Element elem, String styleName)
	{
		if(elem == null)
		{
			throw new NullPointerException("Could not find the element with style name '" + styleName + "'");
		}
		else if((styleName != null) && !elem.getClassName().equals(styleName))
		{
			throw new IllegalStateException("Malformed template HTML file for auto vertical border layout. Unknown class name found: "
					+ "'" + elem.getClassName() + "'. Expected: '" + styleName + "'.");
		}
	}
	
	//---------------------------------------------------------------
	// MISCELLANEOUS
	
	private Element getElementByRow(Row row)
	{
		switch(row)
		{
			case NORTH:
				return firstRow;
			case CENTER:
				return secondRow;
			case SOUTH:
				return thirdRow;
			default:
				throw new IllegalStateException("Unknown row: '" + row.name() + "'");
		}
	}
	
	private Element getElementByColumn(Column column)
	{
		switch(column)
		{
			case WEST:
				return firstColumn;
			case CENTER:
				return secondColumn;
			case EAST:
				return thirdColumn;
			default:
				throw new IllegalStateException("Unknown column: '" + column.name() + "'");
		}
	}
	
	private Element getElementByBorder(Border border)
	{
		switch(border)
		{
			case WEST:
			case CENTER:
			case EAST:
				return getElementByColumn(border.toColumn());
				
			case NORTH:
			case SOUTH:
				return getElementByRow(border.toRow());
			
			default:
				throw new IllegalStateException("Unknown border: '" + border.name() + "'");
		}
	}
}