package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager;

import org.pikater.web.experiment.server.BoxInfoServer;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.AbstractBoxManagerView;

public interface IContextForViews extends IBoxManagerToolboxContext
{
	AbstractBoxManagerView<?> getCurrentView();
	AbstractBoxManagerView<?> getView(BoxManagerView view);
	void setView(AbstractBoxManagerView<?> view);
	void resetView();
	
	BoxInfoServer getCurrentBoxDataSource();
}