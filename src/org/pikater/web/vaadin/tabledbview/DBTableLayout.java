package org.pikater.web.vaadin.tabledbview;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public abstract class DBTableLayout extends VerticalLayout
{
	private static final long serialVersionUID = 6591677491205750540L;
	
	private final CheckBox chb_commit;
	private final DBTable table;
	private final HorizontalLayout tablePagingControls;
	private final HorizontalLayout hl_btnInterface;
	private final Button btn_saveChanges;
	
	public DBTableLayout(AbstractTableDBView dbView, IColumn defaultSortOrder)
	{
		super();
		setSizeUndefined();
		setSpacing(true);
		
		this.chb_commit = new CheckBox("commit changes immediately", true);
		this.chb_commit.setSizeUndefined();
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
		
		this.table = new DBTable(dbView, defaultSortOrder);
		this.table.setSizeFull();
		
		this.tablePagingControls = this.table.getPagingControls();
		this.tablePagingControls.setSizeFull();
		
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
		hl_btnInterface.setSizeFull();
		hl_btnInterface.addComponent(btn_saveChanges);
		
		addComponent(this.chb_commit);
		addComponent(this.table);
		addComponent(this.tablePagingControls);
		addComponent(hl_btnInterface);
	}
	
	public DBTable getTable()
	{
		return table;
	}
	
	protected void addCustomActionButton(Button newBtn)
	{
		hl_btnInterface.addComponent(newBtn);
	}
}