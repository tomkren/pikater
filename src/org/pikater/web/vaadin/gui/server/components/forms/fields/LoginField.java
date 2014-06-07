package org.pikater.web.vaadin.gui.server.components.forms.fields;

public class LoginField extends AbstractFormField
{
	private static final long serialVersionUID = -348416203355318763L;

	/**
	 * Creates a login text field with preset caption, suitable for form layouts.
	 * @param loginValue
	 * @param readOnly
	 */
	public LoginField(String loginValue, boolean required, boolean readOnly) 
	{
		super("Login:", "Enter a login", loginValue, required, readOnly);
	}
}