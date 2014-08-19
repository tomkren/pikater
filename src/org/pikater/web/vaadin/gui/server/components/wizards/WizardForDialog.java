package org.pikater.web.vaadin.gui.server.components.wizards;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

public class WizardForDialog<C extends IWizardCommon> extends WizardWithOutput<C>
{
	private static final long serialVersionUID = -6211218565088889148L;
	
	private final Window parentPopup;
	
	public WizardForDialog(Window parentPopup, C output)
	{
		super(output);
		setSizeFull();
		addStyleName("dialogWizard");
		setContentPadding(true);
		
		this.parentPopup = parentPopup;
		
		getCancelButton().addClickListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 7767062741423812667L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				closeWizardAndTheParentPopup();
			}
		});
		getFinishButton().setEnabled(false);
		getFinishButton().setVisible(false);
	}
	
	protected Window getParentPopup()
	{
		return parentPopup;
	}
	
	public void closeWizardAndTheParentPopup()
	{
		parentPopup.close();
	}
}