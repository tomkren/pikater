package org.pikater.web.vaadin.gui.server.components.forms.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.pikater.web.vaadin.gui.server.components.forms.fields.FormTextField;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public abstract class CustomFormLayout extends VerticalLayout
{
	private static final long serialVersionUID = 4919466788832055328L;
	
	private final FormLayout fLayout;
	private final HorizontalLayout buttonInterface;
	private final Button btn_actionBtn;
	private final Map<IFormField<Object>, String> fieldInfo;
	
	public CustomFormLayout(String actionButtonCaption)
	{
		super();
		setSizeUndefined();
		setSpacing(true);
		
		this.fLayout = new FormLayout();
		addComponent(fLayout);
		
		this.buttonInterface = new HorizontalLayout();
		this.buttonInterface.setSizeUndefined();
		this.buttonInterface.setSpacing(true);
		
		final ClickListener listener = getActionButtonListener();
		if(listener != null)
		{
			this.btn_actionBtn = new Button(actionButtonCaption);
			this.btn_actionBtn.setEnabled(false);
			this.btn_actionBtn.addClickListener(new Button.ClickListener()
			{
				private static final long serialVersionUID = -895164235023546550L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					btn_actionBtn.setEnabled(false); // set this form updated
					listener.buttonClick(event);
				}
			});
			this.buttonInterface.addComponent(this.btn_actionBtn);
		}
		else
		{
			this.btn_actionBtn = null;
		}
		addComponent(this.buttonInterface);
		
		this.fieldInfo = new HashMap<IFormField<Object>, String>();
	}
	
	@SuppressWarnings("unchecked")
	protected void addField(IFormField<?> field, String notificationDescription)
	{
		fieldInfo.put((IFormField<Object>) field, notificationDescription);
		
		if(field instanceof AbstractField<?>)
		{
			fLayout.addComponent((AbstractField<Object>) field);
			field.setOwnerForm(this);
		}
		else
		{
			throw new IllegalArgumentException("The 'field' argument is not a proper Vaadin field (AbstractField<T>).");
		}
	}
	
	protected void addCustomButton(Button btn)
	{
		buttonInterface.addComponent(btn);
	}
	
	public synchronized void updateFieldStatus(FormTextField field, boolean validatedAndUpdated)
	{
		// fieldStatus.put(field, validatedAndUpdated); // replaces the original value
		
		// first let's see how well this is going to perform in a brute force manner:
		if(btn_actionBtn != null)
		{
			btn_actionBtn.setEnabled(isFormValidAndUpdated());
		}
	}
	
	public boolean isFormUpdated()
	{
		return isForm(false, true);
	}
	
	/**
	 * All fields are valid (including the "required" attribute) and at least one is updated.
	 * @return
	 */
	public boolean isFormValidAndUpdated()
	{
		return isForm(true, true);
	}
	
	private boolean isForm(boolean checkValid, boolean checkUpdated)
	{
		boolean atLeastOneUpdated = !checkUpdated;
		Iterator<Component> fieldIterator = fLayout.iterator();
		while(fieldIterator.hasNext())
		{
			FormTextField field = (FormTextField) fieldIterator.next();
			if(checkValid) 
			{
				if(field.isRequired() && field.getValue().isEmpty())
				{
					MyNotifications.showError(null, String.format("The '%s' field is required to be set.", fieldInfo.get(field)));
					return false;
				}
				else if(!field.isValid())
				{
					// custom error message is assumed to be shown at the field
					return false;
				}
			}
			if(field.isUpdated())
			{
				atLeastOneUpdated = true;
			}
		}
		return atLeastOneUpdated;
	}
	
	public abstract ClickListener getActionButtonListener();
}