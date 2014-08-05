package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager;

import org.pikater.shared.experiment.webformat.server.BoxInfoServer;
import org.pikater.web.vaadin.gui.server.components.toolbox.Toolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.AbstractBoxManagerView;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.BoxManagerOptionView;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.BoxManagerOverview;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.BoxManagerSlotView;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents.ClickListener;

@StyleSheet("boxManagerToolbox.css")
public class BoxManagerToolbox extends Toolbox implements IContextForViews
{
	private static final long serialVersionUID = -4029184252337221526L;
	
	/*
	 * Declare views for this toolbox.
	 */
	
	/**
	 * The default view. Provides an interface to display a view for specific option.
	 */
	private final BoxManagerOverview overview;
	
	/**
	 * View for specific option.
	 */
	private final BoxManagerOptionView optionView;
	
	/**
	 * View for specific slot, either input or output.
	 */
	private final BoxManagerSlotView slotView;
	
	/**
	 * The currently displayed view in this toolbox.
	 */
	private AbstractBoxManagerView<?> currentView;
	
	/*
	 * Information for views.
	 */
	
	private BoxInfoServer currentBoxDataSource;
	
	/*
	 * Constructor.
	 */

	public BoxManagerToolbox(String caption, ClickListener minimizeAction)
	{
		super(caption, minimizeAction);
		setSizeFull();
		
		this.currentBoxDataSource = null;
		this.overview = new BoxManagerOverview(this);
		this.optionView = new BoxManagerOptionView(this);
		this.slotView = new BoxManagerSlotView(this);
		
		// don't alter the call order:
		resetView();
		setContentFromSelectedBoxes(new BoxInfoServer[0]);
	}
	
	@Override
	public BoxInfoServer getCurrentBoxDataSource()
	{
		return currentBoxDataSource;
	}
	
	@Override
	public AbstractBoxManagerView<?> getView(BoxManagerView view)
	{
		switch(view)
		{
			case OPTIONVIEW:
				return optionView;
			case OVERVIEW:
				return overview;
			case SLOTVIEW:
				return slotView;
			default:
				throw new IllegalStateException("Unknown state: " + view.name());
		}
	}

	@Override
	public void setView(AbstractBoxManagerView<?> view)
	{
		setToolboxContent(view);
		currentView = view;
	}
	
	@Override
	public void resetView()
	{
		setView(this.overview);
	}
	
	/**
	 * Resets the content of this toolbox according to the given resources.
	 * @param selectedBoxesInformation
	 * @return whether the new content needs to be displayed
	 */
	public boolean setContentFromSelectedBoxes(BoxInfoServer[] selectedBoxesInformation)
	{
		/*
		 * Batch options managing could be supported in future:
		 * Set<AgentInfo> selectedBoxesByType = new HashSet<AgentInfo>(Arrays.asList(selectedBoxesInformation));
		 * switch(selectedBoxesByType.size())
		 */
		
		// at this moment, only implement managing for a single box
		switch(selectedBoxesInformation.length)
		{
			case 0: // nothing is selected
				currentView.setBoxIdentificationLabel("none");
				currentView.setEnabled(false);
				return false;
				
			case 1: // a single specific box type is selected
				currentView.setEnabled(true);
				if(getCurrentBoxDataSource() != selectedBoxesInformation[0])
				{
					currentBoxDataSource = selectedBoxesInformation[0];
					this.overview.setContentFrom(currentBoxDataSource);
					resetView();
				}
				currentView.refreshBoxIdentificationLabel();
				return true;
				
			default: // multiple specific box types are selected
				currentView.setBoxIdentificationLabel("multiple");
				currentView.setEnabled(false);
				return false;
		}
	}
}