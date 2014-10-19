package org.pikater.shared.database.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.pikater.core.ontology.subtrees.newoption.NewOptions;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.daos.DAOs;

/**
 * Class for exporting experiment results to CSV format
 */
public class ResultExporter {
	PrintWriter out;
	/**
	 * Character, which is used to separate two columns
	 */
	public static final char DELIMINITER = ',';

	public ResultExporter(PrintWriter output) {
		this.out = output;
	}

	public ResultExporter(OutputStream output) {
		this.out = new PrintWriter(output);
	}

	/**
	 * Exports the results of a batch to CSV format by exporting all of its
	 * experiments
	 * <p>
	 * Header is printed on the first row.
	 * 
	 * @param batch
	 *            the {@link JPABatch} object, which results should be exported
	 */
	public void export(JPABatch batch) {
		this.export(batch.getId());
	}

	/**
	 * Exports the results of a batch to CSV format by exporting all of its
	 * experiments
	 * <p>
	 * Header is printed on the first row.
	 * 
	 * @param batchID
	 *            the ID of batch, which results should be exported
	 */
	public void export(int batchID) {
		header(null, null, null);
		List<Object[]> batchExperimentResultList = DAOs.batchDAO
				.getByIDwithResults(batchID);
		for (Object[] triplet : batchExperimentResultList) {
			row((JPABatch) triplet[0], (JPAExperiment) triplet[1],
					(JPAResult) triplet[2]);
		}
	}

	public void header(JPABatch batch, JPAExperiment experiment) {
		StringBuilder sb = new StringBuilder();

		sb.append("batch ID");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("batch name");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("note");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("batch created");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("batch finished");
		sb.append(ResultExporter.DELIMINITER);

		sb.append("model strategy");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("used model class");
		sb.append(ResultExporter.DELIMINITER);

		out.println(sb.toString());
	}

	public void header(JPAExperiment experiment, JPAResult result) {
		StringBuilder sb = new StringBuilder();

		sb.append("experiment ID");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("model strategy");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("used model class");
		sb.append(ResultExporter.DELIMINITER);

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

	public void header(JPABatch batch, JPAExperiment experiment,
			JPAResult result) {
		StringBuilder sb = new StringBuilder();

		sb.append("batch ID");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("batch name");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("note");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("batch created");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("batch finished");
		sb.append(ResultExporter.DELIMINITER);

		sb.append("model strategy");
		sb.append(ResultExporter.DELIMINITER);
		sb.append("used model class");
		sb.append(ResultExporter.DELIMINITER);

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

	public void row(JPABatch batch, JPAExperiment experiment) {
		StringBuilder sb = new StringBuilder();

		sb.append(batch.getId());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(batch.getName());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(batch.getNote());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(batch.getCreated());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(batch.getFinished());
		sb.append(ResultExporter.DELIMINITER);

		sb.append(experiment.getModelStrategy().name());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(experiment.getUsedModel() != null ? experiment.getUsedModel()
				.getAgentClassName() : "no_model");
		sb.append(ResultExporter.DELIMINITER);

		out.println(sb.toString());
	}

	public void row(JPAExperiment experiment, JPAResult result) {
		StringBuilder sb = new StringBuilder();

		sb.append(experiment.getId());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(experiment.getModelStrategy().name());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(experiment.getUsedModel() != null ? experiment.getUsedModel()
				.getAgentClassName() : "no_model");
		sb.append(ResultExporter.DELIMINITER);

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
		sb.append(NewOptions.exportToWeka(NewOptions.importXML(
				result.getOptions()).getOptions()));
		out.println(sb.toString());
	}

	public void row(JPABatch batch, JPAExperiment experiment, JPAResult result) {
		StringBuilder sb = new StringBuilder();

		sb.append(batch.getId());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(batch.getName());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(batch.getNote());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(batch.getCreated());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(batch.getFinished());
		sb.append(ResultExporter.DELIMINITER);

		sb.append(experiment.getModelStrategy().name());
		sb.append(ResultExporter.DELIMINITER);
		sb.append(experiment.getUsedModel() != null ? experiment.getUsedModel()
				.getAgentClassName() : "no_model");
		sb.append(ResultExporter.DELIMINITER);

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
		sb.append(NewOptions.exportToWeka(NewOptions.importXML(
				result.getOptions()).getOptions()));
		out.println(sb.toString());
	}

	/**
	 * Closes the underlying {@link PrintWriter} object.
	 */
	public void close() {
		this.out.close();
	}

	/**
	 * Flushes the underlying {@link PrintWriter} object.
	 */
	public void flush() {
		this.out.flush();
	}
}
