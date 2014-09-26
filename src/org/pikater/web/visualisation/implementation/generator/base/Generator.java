package org.pikater.web.visualisation.implementation.generator.base;

import java.io.IOException;
import java.io.PrintStream;

import org.pikater.web.visualisation.definition.result.AbstractDSVisResult;
import org.pikater.web.visualisation.implementation.exceptions.ChartException;
import org.pikater.web.visualisation.implementation.renderer.RendererInterface;

public abstract class Generator {
	protected AbstractDSVisResult<?,?> progressListener;
	protected PrintStream output;
	
	protected int instNum;
	protected int lastPercentage=-1;
	protected int percentage=0;
	protected int count=0;
	
	protected RendererInterface renderer;
	
	protected Generator(AbstractDSVisResult<?,?> progressListener, PrintStream output){
		this.progressListener=progressListener;
		this.output=output;
	}
	
	public abstract void create() throws IOException, ChartException; 
	
	
	protected void signalOneProcessedRow(){
		count++;
		
		percentage=100*count/instNum;
		
		if((progressListener!=null)&&(percentage>lastPercentage)){
			progressListener.updateProgress(percentage / new Float(100.0));
			lastPercentage=percentage;
		}
	}
}
