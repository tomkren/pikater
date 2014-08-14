package org.pikater.shared.experiment.webformat;

public interface IExperimentGraph<I extends Object, B extends IBoxInfoCommon<I>>
{
	boolean containsBox(I boxID);
	B getBox(I boxID);
	B addBox(B box);
	void clear();
	boolean isEmpty();
	boolean edgesDefinedFor(I boxID);
	void connect(I fromBoxID, I toBoxID);
	void disconnect(I fromBoxID, I toBoxID);
}