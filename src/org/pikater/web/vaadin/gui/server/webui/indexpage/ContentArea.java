package org.pikater.web.vaadin.gui.server.webui.indexpage;

import org.pikater.web.vaadin.gui.server.webui.MyDialogs;
import org.pikater.web.vaadin.gui.server.webui.MyDialogs.OnOkClicked;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.IContentComponent;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.IWebFeature;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Panel;

public class ContentArea extends Panel
{
	private static final long serialVersionUID = 7642456908975377869L;
	
	public ContentArea()
	{
		super();
		setStyleName("contentArea");
		setSizeFull();
	}
	
	public void setContent(final IWebFeature feature)
	{
		if(getContent() instanceof IContentComponent)
		{
			IContentComponent currentComponent = (IContentComponent) getContent();
			if(currentComponent.hasUnsavedProgress())
			{
				MyDialogs.createSimpleConfirmDialog(getUI(), currentComponent.getCloseDialogMessage(), new OnOkClicked()
				{
					@Override
					public boolean handleOkEvent()
					{
						setContent((AbstractComponent) feature.toComponent());
						return true;
					}
				});
			}
		}
		else
		{
			setContent((AbstractComponent) feature.toComponent());
		}
	}
}