package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.BoxGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.EdgeGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.EdgeGraphItemClient.EndPoint;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.ItemRegistrationModule;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.BiDiOperation;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.AbstractGraphItemShared.RegistrationOperation;

public class SwapEdgeEndPointOperation extends BiDiOperation
{
	private final ItemRegistrationModule itemRegistrationModule;
	private final EdgeGraphItemClient edge;
	private final EndPoint affectedEndpoint;
	private final BoxGraphItemClient boxEndpoint_original;
	private final BoxGraphItemClient boxEndpoint_new;
	
	public SwapEdgeEndPointOperation(KineticEngine kineticEngine, EdgeGraphItemClient edge, EndPoint affectedEndpoint)
	{
		super(kineticEngine);
		
		this.itemRegistrationModule = (ItemRegistrationModule) kineticEngine.getModule(ItemRegistrationModule.moduleID);
		
		this.edge = edge;
		this.affectedEndpoint = affectedEndpoint;
		this.boxEndpoint_original = this.edge.getEndPoint(this.affectedEndpoint);
		this.boxEndpoint_new = kineticEngine.getHoveredBox();
		
		if(this.boxEndpoint_new == null)
		{
			throw new NullPointerException("Can not perform this operation because no hovered box was found. Did you"
					+ "somehow break the track mouse plugin's functions?");
		}
	}

	@Override
	public void firstExecution()
	{
		redo();
	}

	@Override
	public void undo()
	{
		itemRegistrationModule.doOperation(RegistrationOperation.UNREGISTER, false, true, edge);
		edge.setEndpoint(affectedEndpoint, boxEndpoint_original);
		edge.updateEdge();
		itemRegistrationModule.doOperation(RegistrationOperation.REGISTER, true, true, edge);
	}

	@Override
	public void redo()
	{
		itemRegistrationModule.doOperation(RegistrationOperation.UNREGISTER, false, true, edge);
		edge.setEndpoint(affectedEndpoint, boxEndpoint_new);
		edge.updateEdge();
		itemRegistrationModule.doOperation(RegistrationOperation.REGISTER, true, true, edge);
	}
	
	@Override
	public String toString()
	{
		return "SwapEdgeEndPointOperation";
	}
}