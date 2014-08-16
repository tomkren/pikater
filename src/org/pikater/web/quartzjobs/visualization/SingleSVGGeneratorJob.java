package org.pikater.web.quartzjobs.visualization;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.quartz.jobs.base.InterruptibleImmediateOneTimeJob;
import org.pikater.web.visualisation.definition.result.DSVisOneResult;
import org.pikater.web.visualisation.implementation.generator.quartz.SingleSVGGenerator;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

/**
 * This should now be obsolete in favor of the other 2 jobs.
 */
@Deprecated
public class SingleSVGGeneratorJob extends InterruptibleImmediateOneTimeJob
{

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
			return arg instanceof DSVisOneResult;
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
	public void buildJob(JobBuilder builder)
	{
	}

	@Override
	protected void execute() throws JobExecutionException {
		
		DSVisOneResult listener=getArg(0);
			JPADataSetLO dslo=getArg(1);
			File file=new File((String)getArg(2));
			try {
				PrintStream output=new PrintStream(file);
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
				listener.finished();
			} catch (IOException e) {
				file.delete();
				listener.failed(); // don't forget to... important cleanup will take place
				throw new JobExecutionException();
			}


	}

}
