package org.pikater.web.visualisation.implementation.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.visualisation.definition.result.AbstractDSVisResult;
import org.pikater.web.visualisation.implementation.charts.SingleChart;
import org.pikater.web.visualisation.implementation.charts.axis.Axis;
import org.pikater.web.visualisation.implementation.charts.coloring.Colorer;
import org.pikater.web.visualisation.implementation.datasource.single.ArffXYZPoint;
import org.pikater.web.visualisation.implementation.datasource.single.SingleArffDataset;
import org.pikater.web.visualisation.implementation.generator.base.Generator;

public abstract class SingleGenerator extends Generator {

	protected SingleArffDataset dataset;

	public SingleGenerator(AbstractDSVisResult<?, ?> progressListener, JPADataSetLO dslo, PrintStream output, int XIndex, int YIndex, int ColorIndex) {
		super(progressListener, output);
		this.dataset = new SingleArffDataset(dslo, XIndex, YIndex, ColorIndex);
		init();
	}

	public SingleGenerator(AbstractDSVisResult<?, ?> progressListener, JPADataSetLO dslo, PrintStream output, String XName, String YName, String ColorName) {
		super(progressListener, output);
		this.dataset = new SingleArffDataset(dslo, XName, YName, ColorName);
		init();
	}

	public SingleGenerator(AbstractDSVisResult<?, ?> progressListener, JPADataSetLO dslo, File datasetCachedFile, PrintStream output, int XIndex, int YIndex, int ColorIndex) {
		super(progressListener, output);
		this.dataset = new SingleArffDataset(dslo, datasetCachedFile, XIndex, YIndex, ColorIndex);
		init();
	}

	public SingleGenerator(AbstractDSVisResult<?, ?> progressListener, JPADataSetLO dslo, File datasetCachedFile, PrintStream output, String XName, String YName, String ColorName) {
		super(progressListener, output);
		this.dataset = new SingleArffDataset(dslo, datasetCachedFile, XName, YName, ColorName);
		init();
	}

	private void init() {
		this.instNum = dataset.getNumberOfInstances();
	}

	@Override
	public void create() throws IOException {
		renderer.begin();

		Axis yAxis = dataset.getYAxis();
		Axis xAxis = dataset.getXAxis();
		Colorer colorer = dataset.getZColorer();

		SingleChart sch = new SingleChart(Generator.DEFAULTCHARTSIZE, Generator.DEFAULTCHARTSIZE, renderer, xAxis, yAxis);

		sch.startChart();

		ArffXYZPoint next = null;

		try {
			while ((next = dataset.getNext()) != null) {
				sch.renderPoint(next.getX(), next.getY(), next.getZ(), 15, colorer);
				signalOneProcessedRow();
			}
		} finally {
			dataset.close();
		}

		renderer.end();
	}

}
