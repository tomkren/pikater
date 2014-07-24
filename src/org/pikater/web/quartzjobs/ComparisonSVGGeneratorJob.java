package org.pikater.web.quartzjobs;

import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.quartz.jobs.base.InterruptibleOneTimeJob;
import org.pikater.shared.visualisation.generator.quartz.ComparisonSVGGenerator;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs.IProgressDialogTaskContext;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

public class ComparisonSVGGeneratorJob extends InterruptibleOneTimeJob {

	public ComparisonSVGGeneratorJob() {
		super(10);
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {

	}

	@Override
	public boolean argumentCorrect(int index, Object arg) {
		switch(index){
		case 0:
			return arg instanceof IProgressDialogTaskContext;
		case 1:
			return arg instanceof JPADataSetLO;
		case 2:
			return arg instanceof JPADataSetLO;
		case 3:
			return arg instanceof String;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			return ((arg instanceof Integer)||(arg instanceof String));
		default:
			return false;
		}
	}

	@Override
	public void buildJob(JobBuilder builder) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() throws JobExecutionException {
		IProgressDialogTaskContext listener=getArg(0);
		JPADataSetLO dslo1=getArg(1);
		JPADataSetLO dslo2=getArg(2);
		
		if(dslo1.getHash()==dslo2.getHash()){
			throw new JobExecutionException("Why compare the dataset to itself?");
		}
		
		try {
			PrintStream output=new PrintStream((String)getArg(3));
			Object attrArg1=getArg(4);
			Object attrArg2=getArg(5);
			Object attrArg3=getArg(6);
			Object attrArg4=getArg(7);
			Object attrArg5=getArg(8);
			Object attrArg6=getArg(9);

			ComparisonSVGGenerator csvgg;
			if(
					(attrArg1 instanceof String)&&(attrArg2 instanceof String)&&(attrArg3 instanceof String)&&
					(attrArg4 instanceof String)&&(attrArg5 instanceof String)&&(attrArg6 instanceof String)
					)
			{
				csvgg=new ComparisonSVGGenerator(listener, output,dslo1,dslo2, (String)attrArg1,(String)attrArg2, (String)attrArg3,(String)attrArg4,(String)attrArg5, (String)attrArg6);
			}else if(
					(attrArg1 instanceof Integer)&&(attrArg2 instanceof Integer)&&(attrArg3 instanceof Integer)&&
					(attrArg4 instanceof Integer)&&(attrArg5 instanceof Integer)&&(attrArg6 instanceof Integer)
					)
			{
				csvgg=new ComparisonSVGGenerator(listener, output,dslo1,dslo2, (Integer)attrArg1,(Integer)attrArg2, (Integer)attrArg3,(Integer)attrArg4,(Integer)attrArg5, (Integer)attrArg6);
			}else{
				output.close();
				throw new JobExecutionException();
			}

			csvgg.create();
			
		} catch (IOException e) {
			throw new JobExecutionException();
		}


	}

}
