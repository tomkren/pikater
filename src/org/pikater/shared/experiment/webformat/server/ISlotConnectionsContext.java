package org.pikater.shared.experiment.webformat.server;

import java.util.Set;

import org.pikater.shared.experiment.webformat.IBoxInfoCommon;

public interface ISlotConnectionsContext<I extends Object, B extends IBoxInfoCommon<I>>
{
	/**
	 * Gets the list of boxes to which an edge leads from box
	 * with the given ID.
	 * @param boxID
	 * @return
	 */
	Set<B> getFromNeighbours(I boxID);
	
	/**
	 * Gets the list of boxes from which an edge leads to box
	 * with the given ID.
	 * @param boxID
	 * @return
	 */
	Set<B> getToNeighbours(I boxID);
	
	/**
	 * Does an edge lead from box with the given ID to box with the given ID?
	 * @param fromBoxID
	 * @param toBoxID
	 * @return
	 */
	boolean edgeExistsBetween(I fromBoxID, I toBoxID);
}