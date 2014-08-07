package org.pikater.shared.experiment.webformat;

public interface IExperimentGraph<I extends Object, B extends IBoxInfo<I>>
{
	void clear();
	boolean containsBox(I boxID);
	B getBox(I boxID);
	B addBox(B box);
	boolean edgesDefinedFor(I boxID);
	void connect(I fromBoxID, I toBoxID);
	void disconnect(I fromBoxID, I toBoxID);
}