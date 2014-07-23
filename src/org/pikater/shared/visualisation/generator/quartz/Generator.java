package org.pikater.shared.visualisation.generator.quartz;

import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.visualisation.generator.ChartGenerator.IProgressListener;

public abstract class Generator {
	protected IProgressListener listener;
	protected PrintStream output;
	
	protected int instNum;
	protected int lastPercentage=-1;
	protected int percentage=0;
	protected int count=0;
	
	protected Generator(IProgressListener listener, PrintStream output){
		this.listener=listener;
		this.output=output;
	}
	
	public abstract void create() throws IOException; 
	
	
	protected void signalOneProcessedRow(){
		count++;
		
		percentage=100*count/instNum;
		
		if((listener!=null)&&(percentage>lastPercentage)){
			listener.updateProgress(percentage/100.0);
			lastPercentage=percentage;
		}
	}
	
	protected void signalFinish(){
		if(listener!=null){
			listener.finished();
		}
	}
}
