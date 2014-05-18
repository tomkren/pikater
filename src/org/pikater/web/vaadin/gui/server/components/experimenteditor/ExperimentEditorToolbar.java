package org.pikater.web.vaadin.gui.server.components.experimenteditor;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ExperimentEditorToolbar extends HorizontalLayout
{
	private static final long serialVersionUID = 3627484964167144046L;
	
	private final ExperimentEditor parentEditor;
	
	public ExperimentEditorToolbar(ExperimentEditor parentEditor, boolean debugMode)
	{
		super();
		setSpacing(true);
		
		this.parentEditor = parentEditor;
		build(debugMode);
	}
	
	private void build(boolean debugMode)
	{
		if(debugMode)
		{
			Button btn_setLeftTA1 = new Button("Set boxes layer (left)");
			btn_setLeftTA1.addClickListener(new ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					// TODO Auto-generated method stub
					// leftTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_BOXES, GWTMisc.jsonAttrsToSerialize));
				}
			});
			addComponent(btn_setLeftTA1);
			
			Button btn_setLeftTA2 = new Button("Set edge layer (left)");
			btn_setLeftTA2.addClickListener(new ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					// TODO Auto-generated method stub
					// leftTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_EDGES, GWTMisc.jsonAttrsToSerialize));
					// leftTextArea.setText(kineticCanvas.getEngine().serializeToJSON(EngineComponent.LAYER_EDGES));
				}
			});
			addComponent(btn_setLeftTA2);
			
			Button btn_setRightTA = new Button("Set selection (right)");
			btn_setRightTA.addClickListener(new ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					// TODO Auto-generated method stub
					// rightTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_SELECTION, GWTMisc.jsonAttrsToSerialize));
				}
			});
			addComponent(btn_setRightTA);
			
			Button btn_displayComparison = new Button("Display JSON comparison");
			btn_displayComparison.addClickListener(new ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					// TODO Auto-generated method stub
					// jsonComparisonPanel.show();
				}
			});
			addComponent(btn_displayComparison);
		}
		
		Button btn_saveStageJSON = new Button("Serialize");
		btn_saveStageJSON.addClickListener(new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				// TODO Auto-generated method stub
				/*
				new TempDeselectOperation(kineticCanvas.getEngine(), new Command()
				{
					@Override
					public void execute()
					{
						// TODO:
						GWTMisc.alertNotImplemented.execute();
						
						server.experimentSchemaSerialized(
								kineticCanvas.getEngine().serializeToJSON(EngineComponent.LAYER_BOXES),
								kineticCanvas.getEngine().getEdgeListJSON()
						);
					}
				});
				*/
			}
		});
		addComponent(btn_saveStageJSON);
		
		Button btn_loadStageJSON = new Button("Deserialize");
		btn_loadStageJSON.addClickListener(new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				// TODO Auto-generated method stub

				// GWTMisc.alertNotImplemented.execute();
				// kineticState.deserialize(dLayerJSON, edgeListJSON);
				// setFocus(true);
			}
		});
		addComponent(btn_loadStageJSON);
	}
}
