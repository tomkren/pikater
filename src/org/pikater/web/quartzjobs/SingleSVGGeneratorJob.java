package org.pikater.web.quartzjobs;

import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.quartz.jobs.base.InterruptibleOneTimeJob;
import org.pikater.shared.visualisation.generator.quartz.SingleSVGGenerator;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs.IProgressDialogTaskContext;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

public class SingleSVGGeneratorJob extends InterruptibleOneTimeJob {

	public SingleSVGGeneratorJob() {
		super(6);
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
			return arg instanceof String;
		case 3:
		case 4:
		case 5:
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
			JPADataSetLO dslo=getArg(1);
			try {
				PrintStream output=new PrintStream((String)getArg(2));
				Object attrArg1=getArg(3);
				Object attrArg2=getArg(4);
				Object attrArg3=getArg(5);
				
				SingleSVGGenerator ssvgg;
				if((attrArg1 instanceof String)&&(attrArg2 instanceof String)&&(attrArg3 instanceof String)){
					ssvgg=new SingleSVGGenerator(listener, dslo, output, (String)attrArg1,(String)attrArg2, (String)attrArg3);
				}else if((attrArg1 instanceof Integer)&&(attrArg2 instanceof Integer)&&(attrArg3 instanceof Integer)){
					ssvgg=new SingleSVGGenerator(listener, dslo, output, (Integer)attrArg1,(Integer)attrArg2, (Integer)attrArg3);
				}else{
					output.close();
					throw new JobExecutionException();
				}
				
				ssvgg.create();
				
			} catch (IOException e) {
				throw new JobExecutionException();
			}


	}

}
