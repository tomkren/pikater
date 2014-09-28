package org.pikater.web.vaadin.gui.server.components.wizards;

/**
 * A special wizard spin-off to look better in dialogs.
 * 
 * @author SkyCrawl
 *
 * @param <C> The wizard's "state" class.
 */
public abstract class WizardForDialog<C extends IWizardCommon> extends WizardWithOutput<C>
{
	private static final long serialVersionUID = -6211218565088889148L;
	
	public WizardForDialog(C output)
	{
		super(output);
		setSizeFull();
		addStyleName("dialogWizard");
		setContentPadding(true);
	}
}