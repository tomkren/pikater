package org.pikater.web.vaadin.gui.server.components.experimenteditor;

import org.pikater.shared.experiment.webformat.BoxInfo;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.DragAndDropWrapper;

public class WrappedKineticComponent extends DragAndDropWrapper
{
	private static final long serialVersionUID = 5184871976150233156L;

	public WrappedKineticComponent(final KineticComponent kineticComponent)
	{
		super(kineticComponent);
		setStyleName("experiment-editor-kinetic-container");
		
		setDropHandler(new DropHandler()
		{
			private static final long serialVersionUID = 3609742031571169442L;

			@Override
			public AcceptCriterion getAcceptCriterion()
			{
				return AcceptAll.get();
			}
			
			@Override
			public void drop(DragAndDropEvent event)
			{
				WrapperTargetDetails details = (WrapperTargetDetails) event.getTargetDetails();
				WrapperTransferable transferable = (WrapperTransferable) event.getTransferable();
				CustomLayout droppedComponent = (CustomLayout) transferable.getDraggedComponent();
				BoxInfo boxInfo = (BoxInfo) droppedComponent.getData();
				
				System.out.println();
				
				// TODO: client position is Window relative... we need this component's relative position => send it from KineticComponentWidget when loaded
				
				kineticComponent.getClientRPC().createBox(boxInfo, details.getMouseEvent().getClientX(), details.getMouseEvent().getClientY());
			}
		});
	}
	
	public KineticComponent getWrappedComponent()
	{
		return (KineticComponent) getCompositionRoot();
	}
}