package org.pikater.web.vaadin.gui.server.components.paging;

import java.util.Arrays;

import org.pikater.web.vaadin.gui.server.components.anchor.Anchor;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@StyleSheet("pagingComponent.css")
public class PagingComponent extends HorizontalLayout
{
	private static final long serialVersionUID = 5946124883390545518L;
	
	private final IPagedComponent context;
	private final ComboBox cb_pageLength;
	private final PagePicker pagePicker;

	public PagingComponent(final IPagedComponent context)
	{
		super();
		setSpacing(true);
		setStyleName("pagingComponent");
		
		this.context = context;
		
		Label label_pageLength = new Label("Items per page:");
		label_pageLength.setSizeUndefined();
		
		this.cb_pageLength = new ComboBox(null, Arrays.asList(15, 30, 50, 75, 100));
		this.cb_pageLength.setSizeUndefined();
		this.cb_pageLength.setNullSelectionAllowed(false);
		this.cb_pageLength.setNewItemsAllowed(false);
		this.cb_pageLength.setTextInputAllowed(false);
		this.cb_pageLength.setValue(15); // default page length
		this.cb_pageLength.setImmediate(true);
		this.cb_pageLength.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = 9139815620172779594L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				context.onPageSizeChanged((Integer) event.getProperty().getValue());
				reset();
			}
		});
		
		this.pagePicker = new PagePicker();
		
		addComponent(label_pageLength);
		setComponentAlignment(label_pageLength, Alignment.MIDDLE_LEFT);
		addComponent(cb_pageLength);
		setComponentAlignment(cb_pageLength, Alignment.MIDDLE_LEFT);
		addComponent(pagePicker);
		setComponentAlignment(pagePicker, Alignment.MIDDLE_RIGHT);
		
		setExpandRatio(label_pageLength, 0);
		setExpandRatio(cb_pageLength, 0);
		setExpandRatio(pagePicker, 1);
	}
	
	public int getPageSize()
	{
		return (Integer) cb_pageLength.getValue();
	}
	
	public int getCurrentlySelectedPage()
	{
		return pagePicker.getCurrentlySelectedPage();
	}
	
	public int getOverallOffset()
	{
		return (getCurrentlySelectedPage() - 1) * getPageSize();
	}
	
	public void reset()
	{
		pagePicker.reset();
	}
	
	// -------------------------------------------------------------
	// INNER TYPES
	
	public interface IPagedComponent
	{
		/**
		 * Gets the sum of all items across all pages.
		 * @return
		 */
		int getAllItemsCount();
		
		/**
		 * Callback for when user sets a different page size.
		 * @param itemsPerPage
		 */
		void onPageSizeChanged(int itemsPerPage);
		
		/**
		 * Callback for when user selects another page.</br>
		 * <font color="red">RED ALERT: </font> do not EVER call {@link #reset()} as a result of this call. 
		 * @param itemsPerPage
		 */
		void onPageChanged(int page);
	}
	
	private class PagePicker extends HorizontalLayout
	{
		private static final long serialVersionUID = -595408596224035721L;
		
		private final Anchor page_previous;
		private final ComboBox cb_selectedPage;
		private final Label label_lastPage;
		private final Anchor page_next;
		
		public PagePicker()
		{
			super();
			setSizeUndefined();
			setSpacing(true);
			
			Anchor page_first = new Anchor("<< First", new MouseEvents.ClickListener()
			{
				private static final long serialVersionUID = -3912177860445526917L;

				@Override
				public void click(ClickEvent event)
				{
					/*
					 * Don't call {@link #userSelectedPage(int page)} in here... it will get called
					 * via the value change listener. See below. 
					 */
					cb_selectedPage.setValue(1);
				}
			});
			page_previous = new Anchor("< Previous", new MouseEvents.ClickListener()
			{
				private static final long serialVersionUID = -5064772189847933893L;

				@Override
				public void click(ClickEvent event)
				{
					if(!isFirstPageSelected())
					{
						/*
						 * Don't call {@link #userSelectedPage(int page)} in here... it will get called
						 * via the value change listener. See below. 
						 */
						cb_selectedPage.setValue(getCurrentlySelectedPage() - 1);
					}
				}
			});
			
			Label label_selectedPage = new Label("Page:");
			
			this.cb_selectedPage = new ComboBox();		
			this.cb_selectedPage.setSizeUndefined();
			this.cb_selectedPage.setNullSelectionAllowed(false);
			this.cb_selectedPage.setTextInputAllowed(false);
			this.cb_selectedPage.setScrollToSelectedItem(true);
			this.cb_selectedPage.setImmediate(true);
			this.cb_selectedPage.setData(true);
			this.cb_selectedPage.addValueChangeListener(new Property.ValueChangeListener()
			{
				private static final long serialVersionUID = 1709427468483026288L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					if((Boolean) cb_selectedPage.getData())
					{
						Integer selectedPage = (Integer) event.getProperty().getValue();
						if(selectedPage != null) // just a safety check... selected page should not be null
						{
							userSelectedPage(selectedPage);
						}
					}
				}
			});
			
			this.label_lastPage = new Label();
			page_next = new Anchor("Next >", new MouseEvents.ClickListener()
			{
				private static final long serialVersionUID = -7651055555368147669L;

				@Override
				public void click(ClickEvent event)
				{
					if(!isLastPageSelected())
					{
						/*
						 * Don't call {@link #userSelectedPage(int page)} in here... it will get called
						 * via the value change listener. See above. 
						 */
						cb_selectedPage.setValue(getCurrentlySelectedPage() + 1);
					}
					
				}
			});
			Anchor page_last = new Anchor("Last >>", new MouseEvents.ClickListener()
			{
				private static final long serialVersionUID = -3144174540771713139L;

				@Override
				public void click(ClickEvent event)
				{
					/*
					 * Don't call {@link #userSelectedPage(int page)} in here... it will get called
					 * via the value change listener. See below. 
					 */
					cb_selectedPage.setValue(getLastPage());
				}
			});
			
			reset(1); // initializes the picker to provide correct initial information for the first database query
			
			PagePicker.this.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
			addComponent(page_first);
			addComponent(page_previous);
			addComponent(label_selectedPage);
			addComponent(cb_selectedPage);
			addComponent(new Label("of"));
			addComponent(label_lastPage);
			addComponent(page_next);
			addComponent(page_last);
		}
		
		public int getLastPage()
		{
			return cb_selectedPage.size();
		}
		
		public int getCurrentlySelectedPage()
		{
			return (Integer) cb_selectedPage.getValue();
		}
		
		public void reset()
		{
			double allItemsCount = context.getAllItemsCount();
			double pageSize = getPageSize();
			int pagesNeeded = (int) Math.ceil(allItemsCount / pageSize); 
			reset(pagesNeeded == 0 ? 1 : pagesNeeded);
		}
		
		// ---------------------------------------------------
		// PRIVATE INTERFACE
		
		/**
		 * This method is reserved for user-originated changes. Make sure not to call it otherwise.
		 * @param page
		 */
		private void userSelectedPage(int page)
		{
			// just a safety check... should never happen though
			if((page < 1) || (page > getLastPage()))
			{
				throw new IndexOutOfBoundsException();
			}
			
			cb_selectedPage.select(page);
			context.onPageChanged(page);
			afterChecks();
		}
		
		private void reset(int pages)
		{
			/*
			 * Manipulating with the combobox's items triggers value change event, which
			 * in turn calls {@link #setSelectedPage(int page)}. Make sure that the
			 * event 'knows' not to call it and set the application data field to 'false'.
			 */
			cb_selectedPage.setData(false);
			cb_selectedPage.removeAllItems();
			for(int i = 1; i <= pages; i++)
			{
				cb_selectedPage.addItem(i);
			}
			cb_selectedPage.select(1);
			cb_selectedPage.setData(true);
			
			// and finally:
			label_lastPage.setValue(String.valueOf(pages));
			afterChecks();
		}
		
		private boolean isFirstPageSelected()
		{
			return getCurrentlySelectedPage() == 1;
		}
		
		private boolean isLastPageSelected()
		{
			return getCurrentlySelectedPage() == getLastPage();
		}
		
		private void afterChecks()
		{
			page_previous.setEnabled(!isFirstPageSelected());
			page_next.setEnabled(!isLastPageSelected());
		}
	}
}