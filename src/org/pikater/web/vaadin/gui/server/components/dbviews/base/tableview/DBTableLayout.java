package org.pikater.web.vaadin.gui.server.components.dbviews.base.tableview;

import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.AbstractDBViewRoot;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

@StyleSheet("dbTableLayout.css")
public class DBTableLayout extends VerticalLayout
{
	private static final long serialVersionUID = 6591677491205750540L;
	
	private final CheckBox chb_commit;
	private final DBTable table;
	private final HorizontalLayout tablePagingControls;
	private final HorizontalLayout hl_btnInterface;
	private final Button btn_saveChanges;
	
	public DBTableLayout()
	{
		super();
		setStyleName("dbTableLayout");
		setSizeFull();
		setSpacing(true);
		
		this.chb_commit = new CheckBox("commit changes immediately", true); // table is, by default, immediate
		this.chb_commit.setSizeUndefined();
		this.chb_commit.setImmediate(true);
		this.chb_commit.addValueChangeListener(new ValueChangeListener()
		{
			private static final long serialVersionUID = -5325141700170503845L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				boolean checked = (Boolean) event.getProperty().getValue();
				table.setImmediate(checked);
				btn_saveChanges.setVisible(!checked);
			}
		});
		
		this.table = new DBTable();
		this.table.setWidth("100%");
		
		this.tablePagingControls = this.table.getPagingControls();
		this.tablePagingControls.setWidth("100%");
		
		this.btn_saveChanges = new Button("Save changes", new Button.ClickListener()
		{
			private static final long serialVersionUID = -8473715451478153672L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				table.commitToDB();
			}
		});
		this.btn_saveChanges.setVisible(false);
		
		hl_btnInterface = new HorizontalLayout();
		hl_btnInterface.setSpacing(true);
		hl_btnInterface.setSizeUndefined();
		hl_btnInterface.addComponent(btn_saveChanges);
		
		addComponent(this.chb_commit);
		addComponent(this.table);
		addComponent(this.tablePagingControls);
		addComponent(hl_btnInterface);
	}
	
	@Override
	public void setReadOnly(boolean readOnly)
	{
		super.setReadOnly(readOnly);
		
		chb_commit.setValue(readOnly); // hides or shows the "save" button
		chb_commit.setVisible(!readOnly);
	}
	
	public DBTable getTable()
	{
		return table;
	}
	
	public void setView(AbstractDBViewRoot<? extends AbstractTableDBView> viewRoot)
	{
		table.setView(viewRoot);
	}
	
	public void setCommitImmediately(boolean immediate)
	{
		chb_commit.setValue(immediate);
	}
	
	public void setPagingPadding(boolean enabled)
	{
		if(enabled)
		{
			addStyleName("pagingPadding");
		}
		else
		{
			removeStyleName("pagingPadding");
		}
	}
	
	public void addCustomActionComponent(Component component)
	{
		hl_btnInterface.addComponent(component);
	}
}