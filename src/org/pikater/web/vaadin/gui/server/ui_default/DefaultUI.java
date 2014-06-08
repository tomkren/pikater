package org.pikater.web.vaadin.gui.server.ui_default;

import javax.servlet.annotation.WebServlet;

import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.CustomConfiguredUIServlet;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.IndexPage;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;

@Title("Pikatorium")
@Theme("pikater")
@Push(value = PushMode.AUTOMATIC)
public class DefaultUI extends CustomConfiguredUI
{
	private static final long serialVersionUID = 1964653532060950402L;
	
	/*
	 * Automatic UI finding takes this even one step further and allows you to leave out @VaadinServletConfiguration
	 * completely if you define your servlet class as a static inner class to your UI class.
	 * For clarity the variant with @VaadinServletConfiguration is likely the better option. Please do note that
	 * @VaadinServletConfiguration comes with defaults for some parameters, most importantly legacyPropertyToStringMode,
	 * which might be important if you are migrating an older application. 
	 */
	@WebServlet(value = {"/index", "/index/*", "/VAADIN/*"}, asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DefaultUI.class, widgetset = "org.pikater.web.vaadin.gui.PikaterWidgetset")
	public static class DefaultUIServlet extends CustomConfiguredUIServlet
	{
		private static final long serialVersionUID = -3494370492799211606L;
		
		/**
		 * Always keep this in sync with what's in the WebServlet annotation under the "value" parameter.
		 */
		public static final String mainURLPattern = "/index";
	}
	
	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * Don't forget to call this.
		 * Also note, that you shouldn't update the UI in child classes of
		 * {@link CustomConfiguredUI}. You only provide the content component
		 * when you're asked to in the {@link #getChildContent()} method.
		 */
		super.init(request);
	}
	
	@Override
	protected void displayChildContent()
	{
		/*
		 * Display index page if authenticated or make the user authenticate first and then display it.
		 */
		if(ManageAuth.isUserAuthenticated(getSession()))
		{
			displayIndexPage();
		}
		else
		{
			forceUserToAuthenticate(new IAuthenticationSuccessful()
			{
				@Override
				public void onSuccessfulAuth()
				{
					displayIndexPage();
				}
			});
		}
	}
	
	private void displayIndexPage()
	{
		// return new IndexPage for each UI unless you want all browser tabs to be synchronized and display the same content
		setContent(new IndexPage());
	}
}