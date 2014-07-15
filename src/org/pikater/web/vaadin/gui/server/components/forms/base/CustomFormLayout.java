package org.pikater.web.vaadin.gui.server.components.forms.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
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
	private final Map<AbstractField<? extends Object>, FieldInfo> fieldInfo;
	
	public CustomFormLayout(String submitButtonCaption)
	{
		super();
		setSpacing(true);
		
		this.fLayout = new FormLayout();
		addComponent(fLayout);
		
		this.buttonInterface = new HorizontalLayout();
		this.buttonInterface.setSizeUndefined();
		this.buttonInterface.setSpacing(true);
		
		final IOnSubmit submitAction = getSubmitAction();
		if(submitAction != null)
		{
			this.btn_actionBtn = new Button(submitButtonCaption);
			this.btn_actionBtn.setEnabled(false);
			this.btn_actionBtn.addClickListener(new Button.ClickListener()
			{
				private static final long serialVersionUID = -895164235023546550L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					// try to submit the form
					try
					{
						if(!submitAction.onSubmit())
						{
							return;
						}
					}
					catch (Throwable e)
					{
						PikaterLogger.logThrowable("Could not submit form because of the error below.", e);
						MyNotifications.showError("Internal error", "Form could not be submitted. Please contact the administrators.");
						return;
					}
					
					// if successful, set new values to fields to that the "isUpdated()" method now returns false
					for(AbstractField<? extends Object> field : fieldInfo.keySet())
					{
						setCommitted(field);
					}
					
					// mark the form as submitted and updated
					btn_actionBtn.setEnabled(false); // set this form updated
				}
			});
			this.buttonInterface.addComponent(this.btn_actionBtn);
		}
		else
		{
			this.btn_actionBtn = null;
		}
		addComponent(this.buttonInterface);
		
		this.fieldInfo = new HashMap<AbstractField<?>, FieldInfo>();
	}
	
	// --------------------------------------------------------------
	// PUBLIC INTERFACE
	
	/**
	 * At least one field's value is updated.
	 * @return
	 */
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
	
	// --------------------------------------------------------------
	// PROTECTED TYPES AND INTERFACE
	
	/**
	 * Only add fields with this method after all initializations have been made. The moment you
	 * add a field, value change events start affecting the form.
	 * @param notificationDescription
	 * @param field
	 */
	protected void addField(String notificationDescription, AbstractField<? extends Object> field) 
	{
		fieldInfo.put(field, new FieldInfo(notificationDescription, new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = -5059574562934358334L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				updateActionButton();
			}
		}));
		field.addValueChangeListener(fieldInfo.get(field).listener);
		fLayout.addComponent(field);
	}
	
	/**
	 * Completely detach the field from the form leaving it in original state.
	 * @param field
	 */
	protected void removeField(AbstractField<? extends Object> field) 
	{
		if(fieldInfo.containsKey(field))
		{
			field.removeValueChangeListener(fieldInfo.get(field).listener);
			fieldInfo.remove(field);
			fLayout.removeComponent(field);
		}
	}
	
	protected void addCustomButtonInterface(Component component)
	{
		buttonInterface.addComponent(component);
	}
	
	protected synchronized void updateActionButton()
	{
		// brute-force...
		if(btn_actionBtn != null)
		{
			btn_actionBtn.setEnabled(isFormValidAndUpdated());
		}
	}
	
	protected static <T extends Object> void setValueAndIgnoreReadOnly(AbstractField<T> field, T value)
	{
		boolean readOnly = field.isReadOnly();
		field.setReadOnly(false);
		field.setValue(value);
		field.setReadOnly(readOnly);
	}
	
	@SuppressWarnings("unchecked")
	protected static <T extends Object> T getValueBackup(AbstractField<T> field)
	{
		return (T) field.getData();
	}
	
	protected static <T extends Object> void setValueBackup(AbstractField<T> field, T value)
	{
		field.setData(value);
	}
	
	protected static <T extends Object> boolean isUpdated(AbstractField<T> field)
	{
		return !field.getValue().equals(getValueBackup(field)); // value backup may be null and should be last
	}
	
	protected static <T extends Object> void setCommitted(AbstractField<T> field)
	{
		setValueBackup(field, field.getValue());
	}
	
	protected interface IOnSubmit
	{
		/**
		 * An action to be called when the form is "submitted". You need not worry
		 * about anything form related in this listener. If successful, the
		 * {@link FormFieldWrapper#setComitted()} method will be called on all fields.
		 * If, for some reason, the action can not be performed, just return false
		 * and display some kind of a notification.
		 * @return true, if the operation was successful
		 */
		boolean onSubmit();
	}

	/*
	protected class OnSubmitResult
	{
		public final boolean successful;
	}
	*/
	
	// --------------------------------------------------------------
	// ABSTRACT INTERFACE 
	
	public abstract IOnSubmit getSubmitAction();
	
	// --------------------------------------------------------------
	// PRIVATE TYPES AND INTERFACE
	
	private static class FieldInfo
	{
		public final String notificationDescription;
		public final ValueChangeListener listener;
		
		public FieldInfo(String notificationDescription, ValueChangeListener listener)
		{
			this.notificationDescription = notificationDescription;
			this.listener = listener;
		}
	}
	
	private boolean isForm(boolean checkValid, boolean checkUpdated)
	{
		boolean atLeastOneUpdated = !checkUpdated;
		Iterator<Component> fieldIterator = fLayout.iterator();
		while(fieldIterator.hasNext())
		{
			AbstractField<?> field = (AbstractField<?>) fieldIterator.next();
			if(checkValid) 
			{
				if(field instanceof AbstractTextField)
				{
					AbstractTextField textField = (AbstractTextField) field;
					if(textField.isRequired() && textField.getValue().isEmpty())
					{
						MyNotifications.showError(null, String.format("The '%s' field is required to be set.", fieldInfo.get(field).notificationDescription));
						return false;
					}
				}
				
				if(!field.isValid())
				{
					// custom error message is assumed to be shown at the field
					return false;
				}
			}
			if(isUpdated(field))
			{
				atLeastOneUpdated = true;
			}
		}
		return atLeastOneUpdated;
	}
}