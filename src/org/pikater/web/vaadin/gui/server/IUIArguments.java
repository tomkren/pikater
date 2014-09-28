package org.pikater.web.vaadin.gui.server;

import com.vaadin.ui.UI;

/**
 * Basic interface whose implementations provide
 * redirect URLs to {@link UI} instances, possibly
 * even with arguments.
 * 
 * @author SkyCrawl
 */
public interface IUIArguments
{
	String toRedirectURL();
}