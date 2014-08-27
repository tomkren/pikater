package org.pikater.web.vaadin.gui.client.gwtmanagers;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;

/**
 * This class acts as a keyboard listener. Listens to keyboards events and logs which
 * keys are currently pressed.
 */
public class GWTKeyboardManager
{
	public static NativePreviewHandler getNativePreviewHandler()
	{
		return new NativePreviewHandler()
		{
			/**
			 * The underlying code is called after an event is triggered and even before
			 * the browser processes it which allows for event cancelling. 
			 */
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
		};
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
	// PRIVATE FIELDS & METHODS
	
	/**
	 * Code to handle keyboard input and some routines around it. It is marked static to avoid distributing
	 * an instance of this class to other kinetic components. If more editors are used in the same browser tab,
	 * the static modifier needs to be removed.
	 */
	private static final boolean[] keyDown = new boolean[256]; // defaults to false
	
	private static boolean isKeyDown(int keyCode)
	{
		return keyDown[keyCode];
	}
}