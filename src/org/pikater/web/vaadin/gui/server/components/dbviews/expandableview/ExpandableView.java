package org.pikater.web.vaadin.gui.server.components.dbviews.expandableview;

import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithDynamicSteps;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

public abstract class ExpandableView extends WizardWithDynamicSteps<IWizardCommon> implements IContentComponent
{
	private static final long serialVersionUID = -7316996184841925957L;

	public ExpandableView()
	{
		super(new IWizardCommon() {}, false);
	}
}