package org.pikater.shared.database.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;

/**
 * Class for exporting experiment results to CSV format
 */
public class ResultExporter {
	PrintWriter out;
	/**
	 * Character, which is used to separate two columns
	 */
	public static char DELIMINITER=',';
	
	public ResultExporter(PrintWriter output){
		this.out=output;
	}
	
	public ResultExporter(OutputStream output){
		this.out=new PrintWriter(output);
	}
	
	/**
	 * Exports the results of a batch to CSV format by exporting all of its experiments
	 * <p>
	 * Header is printed on the first row.
	 * @param batch the {@link JPABatch} object, which results should be exported 
	 */
	public void export(JPABatch batch){
		header();
		for(JPAExperiment experiment : safe(batch.getExperiments())){
			for(JPAResult result : safe(experiment.getResults())){
				result(result);
			}
		}
	}
	
	/**
	 * Exports the result of one experiment to CSV format.
	 * <p>
	 * Header is printed on the first row. For exporting a whole batch
	 * use {@link ResultExporter#export(JPABatch)}
	 * @param experiment the {@link JPAExperiment} object, which result sshould be exported
	 */
	public void export(JPAExperiment experiment){
		header();
		for(JPAResult result : safe(experiment.getResults())){
			result(result);
		}
	}
	
	private void header(){
		StringBuilder sb=new StringBuilder();
		sb.append("agent name");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("duration");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("duration LR");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("error rate");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("kappa statistic");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("relative absolute error");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("root mean squared error");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("root relative squared error");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("start");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("finish");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("weka options");
		out.println(sb.toString());
	}
	
	private void result(JPAResult result){
		StringBuilder sb=new StringBuilder();
		sb.append(result.getAgentName());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(result.getDuration());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(result.getDurationLR());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(result.getErrorRate());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(result.getKappaStatistic());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(result.getRelativeAbsoluteError());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(result.getRootMeanSquaredError());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(result.getRootRelativeSquaredError());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(result.getStart());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(result.getFinish());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(NewOptions.exportToWeka(NewOptions.importXML(result.getOptions()).getOptions()));
		out.println(sb.toString());
	}
	
	private <T> List<T> safe(List<T> list){
		return list==null ? Collections.<T>emptyList() : list;
	}
	
	/**
	 * Closes the underlying {@link PrintWriter} object.
	 */
	public void close(){
		this.out.close();
	}
	
	/**
	 * Flushes the underlying {@link PrintWriter} object.
	 */
	public void flush(){
		this.out.flush();
	}
}
