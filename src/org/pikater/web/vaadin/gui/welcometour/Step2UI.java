package org.pikater.web.vaadin.gui.welcometour;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.pikater.web.vaadin.gui.MyDialogs;
import org.pikater.web.vaadin.gui.welcometour.RemoteServerInfoItem.Header;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class Step2UI extends VerticalLayout
{
	private static final String comboBoxDefaultItem = "---";
	
	public static final boolean filterFilledDefaultValue = false;
	
	private Table mainTable;
	
	public Step2UI(List<String> omittedModels, Collection<String> topologyNames, final Collection<RemoteServerInfoItem> allServers)
	{
		setSizeUndefined();
		setSpacing(true);
		mainTable = null;
		if(!omittedModels.isEmpty())
		{
			// TODO: display log button
			
			Label warning = new Label("<span class=\"errorLabel\">WARNING:</span> the following models were parsed with errors:");
			StringBuilder listHTML = new StringBuilder("<ul>");
			for(String omitted : omittedModels)
			{
				listHTML.append(String.format("<li>%s</li>", omitted));
			}
			listHTML.append("</ul>");
			Label list = new Label(listHTML.toString(), ContentMode.HTML);

			addComponent(warning);
			addComponent(list);
			addComponent(new Label("Should you wish for these models to be processed, correct them (corresponding errors can be found in the logs) and launch the application again."));
		}
		if(!allServers.isEmpty())
		{
			// and start building the UI
			final Step2TableContainer dataSource = new Step2TableContainer(allServers);
			mainTable = new Table();
			mainTable.setColumnReorderingAllowed(false);
			mainTable.setEditable(true);
			mainTable.setSelectable(true);
			mainTable.setMultiSelect(true);
			mainTable.setMultiSelectMode(MultiSelectMode.DEFAULT);
			mainTable.setSortEnabled(true);
			mainTable.setImmediate(true);
			mainTable.setContainerDataSource(dataSource);
			// mainTable.setSortContainerPropertyId(Step2TableContainer.header3_login); // exception if data source used?
			
			ComboBox topologySelection = new ComboBox("Filter by topology:");
			topologySelection.addItem(comboBoxDefaultItem);
			for(String topologyName : topologyNames)
			{
				topologySelection.addItem(topologyName);
			}
			topologySelection.setValue(comboBoxDefaultItem); // select the default value
			topologySelection.addValueChangeListener(new Property.ValueChangeListener()
			{
				@Override
				public void valueChange(ValueChangeEvent event)
				{
					dataSource.setFilterOnlyMachinesFromModel(event.getProperty().getValue() == comboBoxDefaultItem ? null : (String) event.getProperty().getValue());
					refresh();
				}
			});
			
			CheckBox chb_filterFilled = new CheckBox("Filter filled", filterFilledDefaultValue);
			chb_filterFilled.addValueChangeListener(new Property.ValueChangeListener()
			{
				@Override
				public void valueChange(ValueChangeEvent event)
				{
					dataSource.setFilterFilled((Boolean) event.getProperty().getValue());
					refresh();
				}
			});
			CheckBox chb_displayPasswordsInPlainText = new CheckBox("Display passwords in plain text", false);
			chb_displayPasswordsInPlainText.addValueChangeListener(new Property.ValueChangeListener()
			{
				@Override
				public void valueChange(ValueChangeEvent event)
				{
					dataSource.setDisplayPasswordsInPlainText((Boolean) event.getProperty().getValue());
					refresh();
				}
			});
			
			final Button batchSetHostnames = new Button("Set hostname for selected", new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					executeCodeIfTableSelectionNotEmpty(dataSource, Header.HOSTNAME);
				}
			});
			final Button batchSetLogins = new Button("Set login for selected", new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					executeCodeIfTableSelectionNotEmpty(dataSource, Header.USERNAME);
				}
			});
			final Button batchSetPasswords = new Button("Set password for selected", new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					executeCodeIfTableSelectionNotEmpty(dataSource, Header.PASSWORD);
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout();
			hLayout.setSpacing(true);
			hLayout.addComponent(batchSetHostnames);
			hLayout.addComponent(batchSetLogins);
			hLayout.addComponent(batchSetPasswords);
			
			// add components with spaces between them
			addComponent(new Label());
			addComponent(new Label("The following table shows all server entries from supplied topologies. Fill "
					+ "correct connection-related information and then click 'Next' to proceed:"));
			addComponent(new Label());
			addComponent(topologySelection);
			addComponent(new Label());
			addComponent(chb_filterFilled);
			addComponent(chb_displayPasswordsInPlainText);
			addComponent(new Label());
			addComponent(mainTable);
			addComponent(new Label());
			addComponent(hLayout);
		}
	}
	
	public void refresh()
	{
		mainTable.refreshRowCache();
	}
	
	private void executeCodeIfTableSelectionNotEmpty(final Step2TableContainer tableDataSource, final Header column)
	{
		@SuppressWarnings("unchecked")
		final Set<Integer> selectedRowIDs = (Set<Integer>) mainTable.getValue();
		if((selectedRowIDs != null) && !selectedRowIDs.isEmpty())
		{
			// prompt for the new value
			MyDialogs.createTextPromptDialog(getUI(), "Specify a new value", null, new MyDialogs.ITextPromptDialogResult()
			{
				@Override
				public boolean handleResult(String stringPromptResult)
				{
					// and use the new value
					tableDataSource.batchSetValues(selectedRowIDs, column, stringPromptResult);
					refresh();
					return true;
				}
			});
		}
		else
		{
			Notification.show("No table rows are selected", Type.WARNING_MESSAGE);
		}
	}
}