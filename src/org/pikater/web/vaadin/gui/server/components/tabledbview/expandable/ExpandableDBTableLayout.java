package org.pikater.web.vaadin.gui.server.components.tabledbview.expandable;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.web.vaadin.gui.server.components.tabledbview.DBTable;
import org.pikater.web.vaadin.gui.server.components.tabledbview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.tabledbview.views.AbstractTableGUIView;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;

public class ExpandableDBTableLayout extends DBTableLayout implements WizardStep
{
	private static final long serialVersionUID = 168918900017666358L;
	
	private final String caption;
	private final IDBTableLayoutOwnerExpandable layoutOwner;

	public ExpandableDBTableLayout(AbstractTableDBView dbView, IDBTableLayoutOwnerExpandable layoutOwner)
	{
		super(dbView);
		setSizeFull();
		
		this.caption = dbView.getClass().getSimpleName();
		this.layoutOwner = layoutOwner;
	}
	
	/**
	 * Override to use the expandable table instead.
	 */
	@Override
	protected DBTable createTable(AbstractTableGUIView<? extends AbstractTableDBView> dbView)
	{
		return new ExpandableDBTable(dbView, layoutOwner);
	}
	
	//-------------------------------------------------------------
	// WIZARD INTERFACE
	
	@Override
	public String getCaption()
	{
		return caption;
	}

	@Override
	public Component getContent()
	{
		return this;
	}

	@Override
	public boolean onAdvance()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onBack()
	{
		return true;
	}
}