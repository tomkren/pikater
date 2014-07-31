package org.pikater.web.visualisation.implementation.generator.quartz;

import java.io.IOException;
import java.io.PrintStream;

import org.pikater.web.visualisation.definition.result.AbstractDatasetVisualizationResult;

public abstract class Generator {
	protected AbstractDatasetVisualizationResult progressListener;
	protected PrintStream output;
	
	protected int instNum;
	protected int lastPercentage=-1;
	protected int percentage=0;
	protected int count=0;
	
	protected Generator(AbstractDatasetVisualizationResult progressListener, PrintStream output){
		this.progressListener=progressListener;
		this.output=output;
	}
	
	public abstract void create() throws IOException; 
	
	
	protected void signalOneProcessedRow(){
		count++;
		
		percentage=100*count/instNum;
		
		if((progressListener!=null)&&(percentage>lastPercentage)){
			progressListener.updateProgress(percentage / new Float(100.0));
			lastPercentage=percentage;
		}
	}
}
