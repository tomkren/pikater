package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.toolboxes;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.web.vaadin.gui.server.components.anchor.Anchor;
import org.pikater.web.vaadin.gui.server.components.forms.OptionValueForm;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.toolbox.Toolbox;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@StyleSheet("boxOptionsToolbox.css")
public class BoxOptionsToolbox extends Toolbox
{
	private static final long serialVersionUID = -4029184252337221526L;
	
	/*
	 * Declare views for this toolbox.
	 */
	
	/**
	 * The default view. Provides an interface to display a view for specific option.
	 */
	private final OptionsOverview optionsOverview;
	
	/**
	 * View for specific option.
	 */
	private final OptionView optionView;
	
	/*
	 * Information context for views.
	 */
	
	private final BoxOptionsToolboxViewContext context;
	
	/*
	 * Other programmatic variables.
	 */
	
	private BoxOptionsToolboxView currentView;
	
	/*
	 * Constructor.
	 */

	public BoxOptionsToolbox(String caption, ClickListener minimizeAction)
	{
		super(caption, minimizeAction);
		setSizeFull();
		
		this.context = new BoxOptionsToolboxViewContext();
		this.optionsOverview = new OptionsOverview(context);
		this.optionView = new OptionView(context);
		
		// don't alter the call order:
		resetView();
		setContentFromSelectedBoxes(new AgentInfo[0]);
	}
	
	public void setContentFromSelectedBoxes(AgentInfo[] selectedBoxesInformation)
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
				currentView.getBoxIdentificationLabel().setValue("none");
				currentView.setEnabled(false);
				break;
				
			case 1: // a single specific box type is selected
				currentView.setEnabled(true);
				if(context.getCurrentlyViewedBoxesDataSource() != selectedBoxesInformation[0])
				{
					String newBoxIdentification = String.format("%s@%s", 
							BoxType.fromAgentInfo(selectedBoxesInformation[0]).name(),
							selectedBoxesInformation[0].getName()
					);
					optionsOverview.getBoxIdentificationLabel().setValue(newBoxIdentification);
					optionView.getBoxIdentificationLabel().setValue(newBoxIdentification);
					
					resetView();
					context.setCurrentlyViewedBoxesDataSource(selectedBoxesInformation[0]);
					optionsOverview.refreshContent();
				}
				break;
				
			default: // multiple specific box types are selected
				currentView.getBoxIdentificationLabel().setValue("multiple");
				currentView.setEnabled(false);
				break;
		}
	}
	
	private void resetView()
	{
		setView(this.optionsOverview);
	}
	
	private void setView(BoxOptionsToolboxView view)
	{
		setToolboxContent(view);
		currentView = view;
	}
	
	//---------------------------------------------------------------------------
	// CONTEXT INFORMATION FOR THIS TOOLBOX'S VIEWS
	
	private class BoxOptionsToolboxViewContext
	{
		/**
		 * Information for currently viewed Box.
		 */
		private AgentInfo currentlyViewedBoxesDataSource;
		
		/**
		 * The most recently viewed option in this toolbox.
		 */
		private NewOption currentlyViewedOption;
		
		public BoxOptionsToolboxViewContext()
		{
			this.currentlyViewedBoxesDataSource = null;
			this.currentlyViewedOption = null;
		}

		public AgentInfo getCurrentlyViewedBoxesDataSource()
		{
			return currentlyViewedBoxesDataSource;
		}

		public void setCurrentlyViewedBoxesDataSource(AgentInfo currentlyViewedBoxesDataSource)
		{
			this.currentlyViewedBoxesDataSource = currentlyViewedBoxesDataSource;
		}
		
		public NewOption getCurrentlyViewedOption()
		{
			return currentlyViewedOption;
		}

		public void setCurrentlyViewedOption(NewOption currentlyViewedOption)
		{
			this.currentlyViewedOption = currentlyViewedOption;
		}
	}
	
	//---------------------------------------------------------------------------
	// DEFINE VIEWS FOR THIS TOOLBOX
	
	private abstract class BoxOptionsToolboxView extends VerticalLayout
	{
		private static final long serialVersionUID = 6336413820137970823L;
		
		private final BoxOptionsToolboxViewContext context;
		private final Label boxIdentificationLabel;
		
		public BoxOptionsToolboxView(BoxOptionsToolboxViewContext context)
		{
			this.context = context;
			
			this.boxIdentificationLabel = new Label()
			{
				private static final long serialVersionUID = -3028878769001633631L;

				@Override
				public void setValue(String newStringValue)
				{
					super.setValue("Selected box: " + newStringValue);
				}
			};
			this.boxIdentificationLabel.setSizeUndefined();
			this.boxIdentificationLabel.setStyleName("emphasizedLabel");
		}
		
		protected BoxOptionsToolboxViewContext getContext()
		{
			return context;
		}
		
		protected Label getBoxIdentificationLabel()
		{
			return boxIdentificationLabel;
		}
	}
	
	private class OptionsOverview extends BoxOptionsToolboxView
	{
		private static final long serialVersionUID = -7812656982876797148L;
		
		private final VerticalLayout optionsLayout;
		
		public OptionsOverview(BoxOptionsToolboxViewContext context)
		{
			super(context);
			setStyleName("optionsOverview");
			getBoxIdentificationLabel().addStyleName("verticalComponentSpacing");
			
			this.optionsLayout = new VerticalLayout();
			this.optionsLayout.setSpacing(true);
			this.optionsLayout.setCaption("Options:");
			
			addComponent(getBoxIdentificationLabel());
			addComponent(optionsLayout);
		}
		
		@Override
		public void setEnabled(boolean enabled)
		{
			this.optionsLayout.setEnabled(enabled);
		}
		
		protected void refreshContent()
		{
			this.optionsLayout.removeAllComponents();
			for(final NewOption option : getContext().getCurrentlyViewedBoxesDataSource().getOptions())
			{
				HorizontalLayout optionBar = new HorizontalLayout();
				optionBar.setSizeFull();
				optionBar.setHeight("25px");
				optionBar.setSpacing(true);
				optionBar.setStyleName("optionBar");
				
				Label lbl_option = new Label(option.getName());
				lbl_option.setSizeFull();
				lbl_option.setDescription(option.getDescription());
				
				Button btn_editOption = new Button("Edit", new Button.ClickListener()
				{
					private static final long serialVersionUID = 4258994618274439965L;

					@Override
					public void buttonClick(ClickEvent event)
					{
						optionView.setContentFromOption(option);
						setView(optionView);
					}
				});
							
				optionBar.addComponent(lbl_option);
				optionBar.setComponentAlignment(lbl_option, Alignment.MIDDLE_LEFT);
				optionBar.addComponent(btn_editOption);
				optionBar.setComponentAlignment(btn_editOption, Alignment.MIDDLE_RIGHT);
				optionBar.setExpandRatio(lbl_option, 3);
				optionBar.setExpandRatio(btn_editOption, 1);
				
				this.optionsLayout.addComponent(optionBar);
			}
		}
	}
	
	private class OptionView extends BoxOptionsToolboxView
	{
		private static final long serialVersionUID = -2739307294944663713L;
		
		private final Anchor link_optionName;
		private OptionValueForm currentForm;
		
		public OptionView(BoxOptionsToolboxViewContext context)
		{
			super(context);
			setStyleName("optionView");
			
			Button btn_backToOverview = new Button("Back to overview", new Button.ClickListener()
			{
				private static final long serialVersionUID = 8283308445389621644L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					resetView();
				}
			});
			btn_backToOverview.setStyleName("verticalComponentSpacing");
			
			Label lbl_values = new Label("Value for option");
			lbl_values.setStyleName("emphasizedLabel");
			this.link_optionName = new Anchor("", "");
			this.link_optionName.addStyleName("emphasizedLabel");
			
			HorizontalLayout hl_optionIndetification = new HorizontalLayout();
			hl_optionIndetification.setStyleName("verticalComponentSpacing");
			hl_optionIndetification.setSpacing(true);
			hl_optionIndetification.addComponent(lbl_values);
			hl_optionIndetification.addComponent(this.link_optionName);
			
			addComponent(btn_backToOverview);
			addComponent(getBoxIdentificationLabel());
			addComponent(hl_optionIndetification);
		}

		private void setContentFromOption(NewOption option)
		{
			if(getContext().getCurrentlyViewedOption() != option)
			{
				link_optionName.setValue(option.getName() + ":");
				link_optionName.setDescription(option.getDescription());
				if(currentForm != null)
				{
					removeComponent(currentForm);
				}
				if(option.isSingleValue())
				{
					Value value = option.toSingleValue();
					TypeRestriction restriction = option.getValueRestrictionForIndex(0); 
					
					currentForm = new OptionValueForm(value, restriction);
					currentForm.setSizeFull();
					// currentForm.setStyleName("valueForm");
					addComponent(currentForm);
						
					getContext().setCurrentlyViewedOption(option);
				}
				else
				{
					MyNotifications.showError("Unimplemented", "This option has multiple values.");
				}
			}
		}
	}
}