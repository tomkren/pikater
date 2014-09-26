package org.pikater.web.visualisation.implementation.generator;

import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.visualisation.definition.result.AbstractDSVisResult;
import org.pikater.web.visualisation.implementation.charts.MatrixChart;
import org.pikater.web.visualisation.implementation.datasource.multiple.MultipleArffDataset;
import org.pikater.web.visualisation.implementation.generator.base.ChartGenerator;
import org.pikater.web.visualisation.implementation.generator.base.Generator;
import org.pikater.web.visualisation.implementation.renderer.ImageRenderer;

/**
 * Obsolete.
 */
@Deprecated
public class MatrixPNGGenerator extends Generator
{
	private final MultipleArffDataset dataset;

	public MatrixPNGGenerator(AbstractDSVisResult<?, ?> progressListener, JPADataSetLO dslo, PrintStream output)
	{
		super(progressListener, output);

		this.dataset = new MultipleArffDataset(dslo);
		this.instNum = dataset.getNumberOfInstances();
	}

	@Override
	public void create() throws IOException
	{
		ImageRenderer ir = new ImageRenderer(ChartGenerator.MATRIX_CHART_SIZE,
				ChartGenerator.MATRIX_CHART_SIZE);
		ir.begin();

		int attrNum = dataset.getNumberOfAttributes();
		MatrixChart mchg = new MatrixChart(dataset,
				ChartGenerator.MATRIX_CHART_SIZE,
				ChartGenerator.MATRIX_CHART_SIZE, attrNum, ir, attrNum - 1);
		try
		{
			while (dataset.next())
			{
				for (int row = 0; row < attrNum; row++)
				{
					for (int column = 0; column < attrNum; column++)
					{
						mchg.renderPoint(row, column,
								dataset.getAttributeValue(column),
								dataset.getAttributeValue(row),
								dataset.getAttributeValue(attrNum - 1), 7);
					}
				}
				signalOneProcessedRow();
			}
		}
		finally
		{
			dataset.close();
		}

		ir.end();
		ImageIO.write(ir.getImage(), "PNG", output);
		output.close();
	}
}