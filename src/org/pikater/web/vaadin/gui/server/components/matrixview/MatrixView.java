package org.pikater.web.vaadin.gui.server.components.matrixview;

import org.pikater.web.vaadin.gui.server.components.flowlayout.FlowLayout;
import org.pikater.web.vaadin.gui.server.components.flowlayout.FlowLayout.LayoutOrientation;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@StyleSheet("matrixView.css")
public class MatrixView<I extends Object> extends VerticalLayout
{
	private static final long serialVersionUID = -4102353757340619486L;
	
	/*
	 * Some static/configuration variables.
	 */
	
	private static final String HEADER_WIDTH = "150px";
	private static final String HEADER_HEIGHT = "30px"; // synchronize this with the header component's line height (CSS)
	
	/*
	 * Data source related variables.
	 */
	
	private final IMatrixDataSource<I, ? extends Component> dataSource;
	
	/*
	 * Components to keep track of.
	 */
	
	private final FlowLayout content;
	private HeaderComponent selectedHeader_leftIndex;
	private HeaderComponent selectedHeader_topIndex;
	
	public MatrixView(IMatrixDataSource<I, ? extends Component> dataSource)
	{
		super();
		setPrimaryStyleName("matrixView");
		
		/*
		 * Setup data source variables.
		 */
		
		this.dataSource = dataSource;
		this.selectedHeader_leftIndex = null;
		this.selectedHeader_topIndex = null;
		
		/*
		 * Setup the UI.
		 */
		
		this.content = new FlowLayout();
		content.setSizeFull();
		content.addStyleName("main-subcomponent");
		content.addStyleName("content");
		updateContent();
		
		// first row
		FlowLayout fLayout_indexTop = new FlowLayout();
		fLayout_indexTop.setSizeFull();
		fLayout_indexTop.addStyleName("topHeaders");
		fLayout_indexTop.addStyleName("main-subcomponent");
		for(I indexTop : dataSource.getTopIndexSet())
		{
			final HeaderComponent header = new HeaderComponent(indexTop.toString());
			header.setAssociatedIndex(indexTop);
			header.addClickListener(new ClickListener()
			{
				private static final long serialVersionUID = 6643479653093109482L;

				@Override
				public void click(ClickEvent event)
				{
					if(selectedHeader_topIndex != null)
					{
						if(selectedHeader_topIndex != header)
						{
							selectedHeader_topIndex.invertSelection();
							selectedHeader_topIndex = header;
							selectedHeader_topIndex.invertSelection();
						}
						else
						{
							selectedHeader_topIndex.invertSelection();
							selectedHeader_topIndex = null;
						}
					}
					else
					{
						selectedHeader_topIndex = header;
						selectedHeader_topIndex.invertSelection();
					}
					updateContent();
				}
			});
			fLayout_indexTop.addComponent(header);
		}
		
		final HeaderComponent header_reset = new HeaderComponent("Reset selection");
		header_reset.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = -911534300593086566L;

			@Override
			public void click(ClickEvent event)
			{
				boolean somethingWasSelected = false;
				if(selectedHeader_leftIndex != null)
				{
					somethingWasSelected = true;
					selectedHeader_leftIndex.invertSelection();
					selectedHeader_leftIndex = null;
				}
				if(selectedHeader_topIndex != null)
				{
					somethingWasSelected = true;
					selectedHeader_topIndex.invertSelection();
					selectedHeader_topIndex = null;
				}
				if(somethingWasSelected)
				{
					updateContent();
				}
			}
		});
		
		HorizontalLayout firstRow = new HorizontalLayout();
		firstRow.setStyleName("firstRow");
		firstRow.setWidth("100%");
		firstRow.setSpacing(true);
		firstRow.addComponent(header_reset);
		firstRow.addComponent(fLayout_indexTop);
		firstRow.setExpandRatio(fLayout_indexTop, 1);
		
		// second row
		FlowLayout fLayout_indexLeft = new FlowLayout(LayoutOrientation.VERTICAL);
		fLayout_indexLeft.addStyleName("leftHeaders");
		fLayout_indexLeft.addStyleName("main-subcomponent");
		for(I indexLeft : dataSource.getLeftIndexSet())
		{
			final HeaderComponent header = new HeaderComponent(indexLeft.toString());
			header.setAssociatedIndex(indexLeft);
			header.addClickListener(new ClickListener()
			{
				private static final long serialVersionUID = 5086608785319580110L;

				@Override
				public void click(ClickEvent event)
				{
					if(selectedHeader_leftIndex != null)
					{
						if(selectedHeader_leftIndex != header)
						{
							selectedHeader_leftIndex.invertSelection();
							selectedHeader_leftIndex = header;
							selectedHeader_leftIndex.invertSelection();
						}
						else
						{
							selectedHeader_leftIndex.invertSelection();
							selectedHeader_leftIndex = null;
						}
					}
					else
					{
						selectedHeader_leftIndex = header;
						selectedHeader_leftIndex.invertSelection();
					}
					updateContent();
				}
			});
			fLayout_indexLeft.addComponent(header);
		}
		
		HorizontalLayout secondRow = new HorizontalLayout();	
		secondRow.setSizeFull();
		secondRow.setStyleName("secondRow");
		secondRow.setSpacing(true);
		secondRow.addComponent(fLayout_indexLeft);
		secondRow.addComponent(content);
		secondRow.setExpandRatio(content, 1);
		
		// top-level setup
		addComponent(firstRow);
		addComponent(secondRow);
		setExpandRatio(secondRow, 1);
	}
	
	//--------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void updateContent()
	{
		content.removeAllComponents();
		if(selectedHeader_leftIndex != null)
		{
			updateContentFor(selectedHeader_leftIndex.getAssociatedIndex());
		}
		else
		{
			for(I leftIndex : dataSource.getLeftIndexSet())
			{
				updateContentFor(leftIndex);
			}
		}
	}
	
	private void updateContentFor(I indexLeft)
	{
		if(selectedHeader_topIndex != null)
		{
			updateContentFor(indexLeft, selectedHeader_topIndex.getAssociatedIndex());
		}
		else
		{
			for(I indexTop : dataSource.getTopIndexSet())
			{
				updateContentFor(indexLeft, indexTop);
			}
		}
	}
	
	private void updateContentFor(I indexLeft, I indexTop)
	{
		Component currentComponent = dataSource.getElement(indexLeft, indexTop);
		if(currentComponent != null)
		{
			content.addComponent(currentComponent);
		}
	}
	
	//--------------------------------------------------------
	// PRIVATE TYPES
	
	private class HeaderComponent extends Panel
	{
		private static final long serialVersionUID = 8986511648022361418L;
		
		private boolean selected;
		private I associatedIndex;
		
		public HeaderComponent(String caption)
		{
			super();
			setWidth(HEADER_WIDTH);
			setStyleName("headerComponent");
			addStyleName("notSelected");
			
			this.selected = false;
			this.associatedIndex = null;
			
			Label lbl_inner = new Label(caption.toUpperCase(), ContentMode.HTML);
			lbl_inner.setSizeFull();
			lbl_inner.setHeight(HEADER_HEIGHT);
			
			setContent(lbl_inner);
		}
		
		public I getAssociatedIndex()
		{
			return associatedIndex;
		}

		public void setAssociatedIndex(I associatedIndex)
		{
			this.associatedIndex = associatedIndex;
		}

		public void invertSelection()
		{
			if(isSelected())
			{
				removeStyleName("selected");
				addStyleName("notSelected");
			}
			else
			{
				removeStyleName("notSelected");
				addStyleName("selected");
			}
			selected = !selected;
		}
		
		public boolean isSelected()
		{
			return selected;
		}
	}
}