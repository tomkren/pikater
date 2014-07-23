package org.pikater.shared.visualisation;

import org.pikater.shared.visualisation.generator.ChartGenerator.IProgressListener;

public class DummyProgress implements IProgressListener {

	@Override
	public void updateProgress(double percentage) {
		if(percentage>=1.0){
			System.out.println("1.00");
		}else{
		System.out.print(percentage+"..");
		}
	}

	@Override
	public void finished() {
		// TODO Auto-generated method stub
		
	}

}
