package org.pikater.web.vaadin.gui.client.kineticengine.operations.base;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;

import com.google.gwt.user.client.Command;

public abstract class TempConditionOperation
{
	protected final KineticEngine engine;
	protected final Command cmd;
	
	protected TempConditionOperation(KineticEngine engine, Command cmd)
	{
		this.engine = engine;
		this.cmd = cmd;
		init();
		createConditions();
		cmd.execute();
		returnConditions();
	}

	protected abstract void init();
	protected abstract void createConditions();
	protected abstract void returnConditions();
}
