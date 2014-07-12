package org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems;

public class AbstractGraphItemShared
{
	public enum RegistrationOperation
	{
		/**
		 * Register and display items immediately.
		 */
		REGISTER,
		
		/**
		 * Remove items from the graph immediately but don't destroy or disconnect them.
		 */
		UNREGISTER
	}
}