package org.pikater.web.vaadin.gui.client;

import java.util.HashMap;
import java.util.Map;

import org.pikater.web.vaadin.gui.client.i18n.Translation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;

public class ClientVars
{
	// *************************************************************************************
	// PUBLIC FIELDS
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	// private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
	/**
	 * Our translation interface.
	 */
	public static final Translation translation = GWT.create(Translation.class);

	/**
	 * Just something to avoid bug/implementation confusion.
	 */
	public static final Command alertNotImplemented = new Command()
	{
		@Override
		public void execute()
		{
			Window.alert(translation.notImplemented());
		}
	};
	
	/**
	 * Basic attributes to print when calling node serialization into JSON.
	 */
	public static final JsArrayString jsonAttrsToSerialize = (JsArrayString) JsArrayString.createArray();
	static
	{
		jsonAttrsToSerialize.push("attrs");
		jsonAttrsToSerialize.push("x");
		jsonAttrsToSerialize.push("y");
		jsonAttrsToSerialize.push("id");
		jsonAttrsToSerialize.push("className");
		jsonAttrsToSerialize.push("children");
	}
	
	/**
	 * Indicates whether the application is in debug mode and should print various useful debugging information.
	 */
	public static final boolean DEBUG_MODE = true;
	
	// *************************************************************************************
	// PRIVATE FIELDS
	
	/**
	 * Code to handle keyboard input and some routines around it. It is marked static to avoid distributing
	 * an instance of this class to other kinetic components. If more editors are used in the same browser tab,
	 * the static modifier needs to be removed.
	 */
	private static final boolean[] keyDown = new boolean[256]; // defaults to false
	
	/**
	 * Stores cursor styles for elements when desired and to be changed, so that the change can be rolled back later. 
	 */
	private static final Map<Integer, String> previousCursors = new HashMap<Integer, String>();
	
	// *************************************************************************************
	// PUBLIC METHODS
	
	/**
	 * Adds code that gets called after an event is triggered and even before the browser processes it. Allows for cancelling the event also.
	 */
	public static void setPreviewBrowserEvents()
	{
		Event.addNativePreviewHandler(new NativePreviewHandler()
		{
			@Override
			public void onPreviewNativeEvent(NativePreviewEvent event)
			{
				switch (event.getTypeInt())
				{
					case 128: // keydown
						keyDown[event.getNativeEvent().getKeyCode()] = true; // register key being pushed down
						break;
					case 512: // keyup
						keyDown[event.getNativeEvent().getKeyCode()] = false; // register key being released
						break;
					default:
						// System.out.println("Type int: " + event.getTypeInt());
						// System.out.println("NE key code: " + event.getNativeEvent().getKeyCode());
						break;
				}
			}
		});
	}
	
	public static void setCursorType(Element element, MyCursor cursorType)
	{
		previousCursors.put(element.hashCode(), element.getStyle().getCursor());
		element.getStyle().setProperty("cursor", cursorType.toString());
	}
	
	public static void rollBackCursor(Element element)
	{
		element.getStyle().setProperty("cursor", previousCursors.get(element.hashCode()));
	}
	
	public static boolean isShiftKeyDown()
	{
		return isKeyDown(KeyCodes.KEY_SHIFT);
	}
	
	public static boolean isControlKeyDown()
	{
		return isKeyDown(KeyCodes.KEY_CTRL);
	}
	
	public static boolean isAltKeyDown()
	{
		return isKeyDown(KeyCodes.KEY_ALT);
	}
	
	// *************************************************************************************
	// PRIVATE METHODS
	
	private static boolean isKeyDown(int keyCode)
	{
		return keyDown[keyCode];
	}
}
