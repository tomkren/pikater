package org.pikater.shared.visualisation;

import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs.IProgressDialogContextForJobs;

public class DummyProgress implements IProgressDialogContextForJobs {

	@Override
	public void updateProgress(float percentage) {
		if(percentage>=1.0){
			System.out.println("1.00");
		}else{
		System.out.print(percentage+"..");
		}
	}

	@Override
	public void onTaskFinish()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskFailed()
	{
		// TODO Auto-generated method stub
		
	}
}
