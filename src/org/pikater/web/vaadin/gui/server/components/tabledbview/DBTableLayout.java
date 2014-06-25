package org.pikater.web.vaadin.gui.server.components.tabledbview;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.NamedActionDBViewValue;
import org.pikater.web.vaadin.gui.server.components.tabledbview.views.AbstractTableGUIView;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class DBTableLayout extends VerticalLayout implements IDBTableLayout
{
	private static final long serialVersionUID = 6591677491205750540L;
	
	private final CheckBox chb_commit;
	private final DBTable table;
	private final HorizontalLayout tablePagingControls;
	private final HorizontalLayout hl_btnInterface;
	private final Button btn_saveChanges;
	
	public DBTableLayout(AbstractTableDBView dbView, boolean immediateCommit)
	{
		super();
		setSizeUndefined();
		setSpacing(true);
		
		this.chb_commit = new CheckBox("commit changes immediately", immediateCommit);
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
		this.chb_commit.setEnabled(immediateCommit);
		this.chb_commit.setVisible(immediateCommit);
		
		this.table = createTable(AbstractTableGUIView.getInstanceFromDBView(dbView));
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
		this.btn_saveChanges.setVisible(!immediateCommit);
		
		hl_btnInterface = new HorizontalLayout();
		hl_btnInterface.setSpacing(true);
		hl_btnInterface.setSizeUndefined();
		hl_btnInterface.addComponent(btn_saveChanges);
		
		addComponent(this.chb_commit);
		addComponent(this.table);
		addComponent(this.tablePagingControls);
		addComponent(hl_btnInterface);
		
		// leave this for last
		this.chb_commit.setValue(immediateCommit);
	}
	
	public DBTable getTable()
	{
		return table;
	}
	
	protected void addCustomActionComponent(Component component)
	{
		hl_btnInterface.addComponent(component);
	}
	
	protected DBTable createTable(AbstractTableGUIView<? extends AbstractTableDBView> dbView)
	{
		return new DBTable(dbView);
	}

	@Override
	public void dbViewActionCalled(IColumn column, AbstractTableRowDBView row, NamedActionDBViewValue originalAction)
	{
		originalAction.actionExecuted(getTable().isImmediate());
	}
}