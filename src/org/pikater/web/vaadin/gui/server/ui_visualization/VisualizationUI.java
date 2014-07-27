package org.pikater.web.vaadin.gui.server.ui_visualization;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.sharedresources.IRegistrarResource;
import org.pikater.web.sharedresources.ResourceExpiration;
import org.pikater.web.sharedresources.ResourceRegistrar;
import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.CustomConfiguredUIServlet.PikaterUI;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;

@Title("Visualization")
@Theme("pikater")
@Push(value = PushMode.AUTOMATIC)
public class VisualizationUI extends CustomConfiguredUI
{
	private static final long serialVersionUID = -8917289148464357783L;
	public static final String PARAM_RESOURCE = "res";
	
	private VisualizationArguments arguments = null;

	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * First parse and check needed arguments and if they're not correct, return 404.
		 */
		try
		{
			UUID resourceID = ResourceRegistrar.toResourceID(request.getParameter(PARAM_RESOURCE)); 
			arguments = (VisualizationArguments) ResourceRegistrar.getResource(resourceID);
			arguments.getClass(); // spawn a null pointer exception is resource is not defined
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
		if(arguments.isOnlyOneDatasetDefined())
		{
			setContent(new SingleDatasetVisualizer(arguments.getArg1()));
		}
		else
		{
			setContent(new CompareDatasetsVisualizer(arguments.getArg1(), arguments.getArg2()));
		}
	}
	
	//-------------------------------------------------------------------------------
	// SPECIAL TYPES
	
	public static class VisualizationArguments implements IRegistrarResource
	{
		private final JPADataSetLO arg1;
		private final JPADataSetLO arg2; 
		
		/**
		 * Tells the UI to visualize a single dataset.
		 * @param arg1
		 */
		public VisualizationArguments(JPADataSetLO arg1)
		{
			this(arg1, null);
		}
		
		/**
		 * Tells the UI to compare the two datasets.
		 * @param arg1
		 */
		public VisualizationArguments(JPADataSetLO arg1, JPADataSetLO arg2)
		{
			this.arg1 = arg1;
			this.arg2 = arg2;
		}
		
		public JPADataSetLO getArg1()
		{
			return arg1;
		}

		public JPADataSetLO getArg2()
		{
			return arg2;
		}
		
		public boolean isOnlyOneDatasetDefined()
		{
			return (arg1 != null) && (arg2 == null);
		}

		@Override
		public ResourceExpiration getLifeSpan()
		{
			return ResourceExpiration.ON_SESSION_END;
		}
		
		public String toRedirectURL()
		{
			UUID thisClassesResourceID = ResourceRegistrar.registerResource(VaadinSession.getCurrent(), this);
			return CustomConfiguredUI.getRedirectURLToUI(PikaterUI.DATASET_VISUALIZATION) +
					String.format("?%s=%s", VisualizationUI.PARAM_RESOURCE, ResourceRegistrar.fromResourceID(thisClassesResourceID));
		}
	}
	
	public static class VisualizationArgument
	{
		private final JPADataSetLO dataset;
		private final String attrToCompare1;
		private final String attrToCompare2;
		private final String attrToCompareTo;
		
		public VisualizationArgument(JPADataSetLO dataset, String attrToCompare1, String attrToCompare2, String attrToCompareTo)
		{
			this.dataset = dataset;
			this.attrToCompare1 = attrToCompare1;
			this.attrToCompare2 = attrToCompare2;
			this.attrToCompareTo = attrToCompareTo;
		}
	}
}