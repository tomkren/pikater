package org.pikater.shared.experiment.webformat.server;

public interface ISlotConnectionsContext<I extends Object>
{
	boolean edgeExistsBetween(I fromBoxID, I toBoxID);
}