package org.pikater.web.vaadin.gui.server.ui_visualization;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.pikater.web.sharedresources.IRegistrarResource;
import org.pikater.web.sharedresources.ResourceExpiration;
import org.pikater.web.sharedresources.ResourceRegistrar;
import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.CustomConfiguredUIServlet.PikaterUI;
import org.pikater.web.vaadin.gui.server.IUIArguments;
import org.pikater.web.vaadin.gui.server.ui_visualization.components.CompareDatasetsVisualizer;
import org.pikater.web.vaadin.gui.server.ui_visualization.components.SingleDatasetVisualizer;
import org.pikater.web.visualisation.definition.result.DSVisOneResult;
import org.pikater.web.visualisation.definition.result.DSVisTwoResult;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;

@Title("Visualization")
@Theme("pikater")
@Push(value = PushMode.AUTOMATIC)
@SuppressWarnings("rawtypes")
public class VisualizationUI extends CustomConfiguredUI
{
	private static final long serialVersionUID = -8917289148464357783L;
	public static final String PARAM_RESOURCE = "res";
	
	private DSVisUIArgs arguments = null;
	
	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * First parse and check needed arguments and if they're not correct, return 404.
		 */
		try
		{
			UUID resourceID = ResourceRegistrar.toResourceID(request.getParameter(PARAM_RESOURCE)); 
			arguments = (DSVisUIArgs) ResourceRegistrar.getResource(resourceID);
			arguments.getClass(); // spawn a null pointer exception if resource is not defined
		}
		catch(Throwable t)
		{
			returnErrorCode(HttpServletResponse.SC_NOT_FOUND);
		}
		
		/*
		 * Don't forget to call this.
		 * IMPORTANT:
		 * 1) You shouldn't update the UI in this method. You only provide the content component
		 * when you're asked to in the {@link #displayChildContent()} method.
		 * 2) When {@link #displayChildContent()} is called, this method is still not finished.
		 * You shouldn't have any initializing code after the super.init() call.
		 */
		super.init(request);
	}

	@Override
	protected void displayChildContent()
	{
		if(arguments instanceof DSVisOneUIArgs)
		{
			setContent(new SingleDatasetVisualizer((DSVisOneUIArgs) arguments));
		}
		else if(arguments instanceof DSVisTwoUIArgs)
		{
			setContent(new CompareDatasetsVisualizer((DSVisTwoUIArgs) arguments));
		}
		else
		{
			returnErrorCode(HttpServletResponse.SC_NOT_IMPLEMENTED, "Unknown arguments object was received: " + arguments.getClass().getSimpleName());
		}
	}
	
	//-------------------------------------------------------------------------------
	// SPECIAL TYPES
	
	public abstract static class DSVisUIArgs<T> implements IUIArguments, IRegistrarResource
	{
		private final T generatedResult;
		
		public DSVisUIArgs(T generatedResult)
		{
			this.generatedResult = generatedResult;
		}

		public T getGeneratedResult()
		{
			return generatedResult;
		}
		
		@Override
		public ResourceExpiration getLifeSpan()
		{
			return ResourceExpiration.ON_SESSION_END;
		}
		
		@Override
		public String toRedirectURL()
		{
			UUID thisClassesResourceID = ResourceRegistrar.registerResource(VaadinSession.getCurrent(), this);
			return CustomConfiguredUI.getRedirectURLToUI(PikaterUI.DATASET_VISUALIZATION) +
					String.format("?%s=%s", VisualizationUI.PARAM_RESOURCE, ResourceRegistrar.fromResourceID(thisClassesResourceID));
		}
	}
	
	public static class DSVisOneUIArgs extends DSVisUIArgs<DSVisOneResult> 
	{
		public DSVisOneUIArgs(DSVisOneResult generatedResult)
		{
			super(generatedResult);
		}
	}
	
	public static class DSVisTwoUIArgs extends DSVisUIArgs<DSVisTwoResult> 
	{
		public DSVisTwoUIArgs(DSVisTwoResult generatedResult)
		{
			super(generatedResult);
		}
	}
}