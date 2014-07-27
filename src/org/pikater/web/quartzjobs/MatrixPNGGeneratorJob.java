package org.pikater.web.quartzjobs;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.quartz.jobs.base.InterruptibleOneTimeJob;
import org.pikater.shared.visualisation.generator.ChartGenerator;
import org.pikater.shared.visualisation.generator.quartz.MatrixPNGGenerator;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskContext;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.ProgressDialogVisualizationTaskResult;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

public class MatrixPNGGeneratorJob extends InterruptibleOneTimeJob {

	public MatrixPNGGeneratorJob(){
		super(3);
	}
	
	@Override
	public boolean argumentCorrect(int index, Object arg) {
		switch(index){
		case 0:
			return arg instanceof IProgressDialogTaskContext;
		case 1:
			return arg instanceof JPADataSetLO;
		case 2:
			return arg instanceof String;
		default:
			return false;
		}
		
	}

	@Override
	public void buildJob(JobBuilder builder) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() throws JobExecutionException
	{
		IProgressDialogTaskContext listener=getArg(0);
		JPADataSetLO dslo=getArg(1);
		File file=new File((String)getArg(2));
		try {
			PrintStream output=new PrintStream(file);
			MatrixPNGGenerator mpngg=new MatrixPNGGenerator(listener, dslo, output);
			mpngg.create();
			listener.onTaskFinish(new ProgressDialogVisualizationTaskResult(ChartGenerator.MATRIX_CHART_SIZE, ChartGenerator.MATRIX_CHART_SIZE));
		} catch (IOException e) {
			file.delete();
			listener.onTaskFailed();
		}	

	}

	@Override
	public void interrupt() throws UnableToInterruptJobException
	{
		// TODO Auto-generated method stub
		
	}
}