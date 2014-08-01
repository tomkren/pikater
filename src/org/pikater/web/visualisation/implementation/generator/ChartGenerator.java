package org.pikater.web.visualisation.implementation.generator;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.visualisation.definition.result.AbstractDSVisResult;
import org.pikater.web.visualisation.implementation.charts.MatrixChart;
import org.pikater.web.visualisation.implementation.charts.SingleChart;
import org.pikater.web.visualisation.implementation.charts.axis.Axis;
import org.pikater.web.visualisation.implementation.charts.axis.exception.AxisNotJoinableException;
import org.pikater.web.visualisation.implementation.charts.coloring.Colorer;
import org.pikater.web.visualisation.implementation.charts.coloring.exception.ColorerNotMergeableException;
import org.pikater.web.visualisation.implementation.datasource.multiple.MultipleArffDataset;
import org.pikater.web.visualisation.implementation.datasource.single.ArffXYZPoint;
import org.pikater.web.visualisation.implementation.datasource.single.SingleArffDataset;
import org.pikater.web.visualisation.implementation.renderer.ImageRenderer;
import org.pikater.web.visualisation.implementation.renderer.RendererInterface;
import org.pikater.web.visualisation.implementation.renderer.SVGRenderer;

public class ChartGenerator {

	public static int SINGLE_CHART_SIZE=1000;
	/**
	 * Obsolete.
	 */
	@Deprecated
	public static int MATRIX_CHART_SIZE=2000;
	
	/**
	 * Creates an <b>SVG</b> encoded chart for dataset for the {@link JPADataSetLO JPADataSetLO} object.
	 * <p>
	 * If it is possible the X, Y axis and {@link Colorer  Colorer} are autoscaled based on data stored in database. In
	 * case, that no such information is stored default scale values are used for axes, but the
	 * result may not be visually satisfying. 
	 * <p>
	 * Variant generating generating PNG chart: {@link ChartGenerator#generatePNGSingleDatasetChart(JPADataSetLO, PrintStream, int, int, int)}
	 * 
	 * @param input The JPA entity holding the information for the dataset
	 * @param output The stream where the output file is written 
	 * @param XIndex The index of the attribute used for the X axis
	 * @param YIndex The index of the attribute used for the Y axis 
	 * @param ColorIndex The index of the attribute used for the Colorer.
	 * @throws IOException
	 */
	public static void generateSVGSingleDatasetChart(JPADataSetLO input,PrintStream output,int XIndex,int YIndex,int ColorIndex) throws IOException{
		SVGRenderer svgr=new SVGRenderer(output, SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		svgr.begin();
		generateSingleDatasetChart(input,svgr, XIndex, YIndex, ColorIndex,null);
		svgr.end();
		output.close();
	}
	
	public static void generateSVGSingleDatasetChart(JPADataSetLO input,PrintStream output,int XIndex,int YIndex,int ColorIndex, AbstractDSVisResult listener) throws IOException{
		SVGRenderer svgr=new SVGRenderer(output, SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		svgr.begin();
		generateSingleDatasetChart(input,svgr, XIndex, YIndex, ColorIndex,listener);
		svgr.end();
		output.close();
	}
	
	/**
	 * Creates an <b>SVG</b> encoded chart for dataset for the {@link JPADataSetLO JPADataSetLO} object.
	 * <p>
	 * If it is possible the X, Y axis and {@link Colorer  Colorer} are autoscaled based on data stored in database. In
	 * case, that no such information is stored default scale values are used for axes, but the
	 * result may not be visually satisfying. 
	 * <p>
	 * Variant generating generating PNG chart: {@link ChartGenerator#generatePNGSingleDatasetChart(JPADataSetLO, PrintStream, String, String, String)}
	 * 
	 * @param input The JPA entity holding the information for the dataset
	 * @param output The stream where the output file is written 
	 * @param XName The name of the attribute used for the X axis
	 * @param YName The name of the attribute used for the Y axis 
	 * @param ColorName The name of the attribute used for the Colorer.
	 * @throws IOException
	 */
	public static void generateSVGSingleDatasetChart(JPADataSetLO input,PrintStream output,String XName,String YName,String ColorName) throws IOException{
		SVGRenderer svgr=new SVGRenderer(output, SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		svgr.begin();
		generateSingleDatasetChart(input,svgr, XName, YName, ColorName,null);
		svgr.end();
		output.close();
	}
	
	public static void generateSVGSingleDatasetChart(JPADataSetLO input,PrintStream output,String XName,String YName,String ColorName, AbstractDSVisResult listener) throws IOException{
		SVGRenderer svgr=new SVGRenderer(output, SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		svgr.begin();
		generateSingleDatasetChart(input,svgr, XName, YName, ColorName,listener);
		svgr.end();
		output.close();
	}
	
	/**
	 * Creates an <b>PNG</b> encoded chart for dataset for the {@link JPADataSetLO JPADataSetLO} object.
	 * <p>
	 * If it is possible the X, Y axis and {@link Colorer  Colorer} are autoscaled based on data stored in database. In
	 * case, that no such information is stored default scale values are used for axes, but the
	 * result may not be visually satisfying.
	 * <p> 
	 * Variant generating generating SVG chart: {@link ChartGenerator#generateSVGSingleDatasetChart(JPADataSetLO, PrintStream, int, int, int)}
	 * 
	 * @param input The JPA entity holding the information for the dataset
	 * @param output The stream where the output file is written 
	 * @param XIndex The index of the attribute used for the X axis
	 * @param YIndex The index of the attribute used for the Y axis 
	 * @param ColorIndex The index of the attribute used for the Colorer.
	 * @throws IOException
	 */
	public static void generatePNGSingleDatasetChart(JPADataSetLO input,PrintStream output,int XIndex,int YIndex,int ColorIndex) throws IOException{
		ImageRenderer ir=new ImageRenderer(SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		ir.begin();
		generateSingleDatasetChart(input,ir, XIndex, YIndex, ColorIndex,null);
		ir.end();
		
		ImageIO.write(ir.getImage(), "PNG", output);
		output.close();
	}
	
	public static void generatePNGSingleDatasetChart(JPADataSetLO input,PrintStream output,int XIndex,int YIndex,int ColorIndex,AbstractDSVisResult listener) throws IOException{
		ImageRenderer ir=new ImageRenderer(SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		ir.begin();
		generateSingleDatasetChart(input,ir, XIndex, YIndex, ColorIndex,listener);
		ir.end();
		
		ImageIO.write(ir.getImage(), "PNG", output);
		output.close();
	}
	
	/**
	 * Creates an <b>PNG</b> encoded chart for dataset for the {@link JPADataSetLO JPADataSetLO} object.
	 * <p>
	 * If it is possible the X, Y axis and {@link Colorer  Colorer} are autoscaled based on data stored in database. In
	 * case, that no such information is stored default scale values are used for axes, but the
	 * result may not be visually satisfying.
	 * <p> 
	 * Variant generating generating SVG chart: {@link ChartGenerator#generateSVGSingleDatasetChart(JPADataSetLO, PrintStream, String, String, String)}
	 * 
	 * @param input The JPA entity holding the information for the dataset
	 * @param output The stream where the output file is written 
	 * @param XName The name of the attribute used for the X axis
	 * @param YName The name of the attribute used for the Y axis 
	 * @param ColorName The name of the attribute used for the Colorer.
	 * @throws IOException
	 */
	public static void generatePNGSingleDatasetChart(JPADataSetLO input,PrintStream output,String XName,String YName,String ColorName) throws IOException{
		ImageRenderer ir=new ImageRenderer(SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		ir.begin();
		generateSingleDatasetChart(input,ir, XName, YName, ColorName,null);
		ir.end();
		
		ImageIO.write(ir.getImage(), "PNG", output);
		output.close();
	}
	
	public static void generatePNGSingleDatasetChart(JPADataSetLO input,PrintStream output,String XName,String YName,String ColorName, AbstractDSVisResult listener) throws IOException{
		ImageRenderer ir=new ImageRenderer(SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		ir.begin();
		generateSingleDatasetChart(input,ir, XName, YName, ColorName,listener);
		ir.end();
		
		ImageIO.write(ir.getImage(), "PNG", output);
		output.close();
	}
	
	
	
	private static void generateSingleDatasetChart(JPADataSetLO input,RendererInterface renderer,int XIndex,int YIndex,int ColorIndex, AbstractDSVisResult listener) throws IOException
	{
		SingleArffDataset dataset=new SingleArffDataset(
				input,
				XIndex, 
				YIndex,
				ColorIndex);
		generateSingleDatasetChartFromDataset(dataset, renderer, listener);
	}
	
	private static void generateSingleDatasetChart(JPADataSetLO input,RendererInterface renderer,String XName,String YName,String ColorName, AbstractDSVisResult listener) throws IOException
	{
		SingleArffDataset dataset=new SingleArffDataset(
				input,
				XName, 
				YName,
				ColorName);
		generateSingleDatasetChartFromDataset(dataset, renderer, listener);
	}
	
	private static void generateSingleDatasetChartFromDataset(SingleArffDataset dataset,RendererInterface renderer, AbstractDSVisResult listener) throws IOException{
		Axis yAxis=dataset.getYAxis();
		Axis xAxis=dataset.getXAxis();
		Colorer colorer=dataset.getZColorer();

		SingleChart sch=new SingleChart(SINGLE_CHART_SIZE, SINGLE_CHART_SIZE, renderer, xAxis, yAxis);
		sch.setMargin(50);
		sch.setLabelSize(150);
		sch.setBackGroundColor(Color.getHSBColor(0.0f, 0.0f, 0.95f));
		sch.setHorizontalCaptionColor(Color.GRAY);
		sch.setVerticalCaptionColor(Color.GRAY);
		sch.setCaptionSize(35);

		sch.startChart();

		ArffXYZPoint next=null;
		
		int instNum=dataset.getNumberOfInstances();
		int lastPercentage=-1;
		int percentage=0;
		int count=0;
		
		while((next=dataset.getNext())!=null){
			sch.renderPoint(next.getX(), next.getY(), next.getZ(), 15, colorer);
			count++;
			
			percentage=100*count/instNum;
			
			if((listener!=null)&&(percentage>lastPercentage)){
				listener.updateProgress(percentage / new Float(100));
				lastPercentage=percentage;
			}
		}

		dataset.close();
	}
	

	/**
	 * Generates a MatrixChart in <b>SVG</b> format for the given {@link JPADataSetLO} object.
	 * <p>
	 * Let the number of dataset's attributes be N, then the created chart has N rows and
	 * N columns and in each subchart the {@link Colorer} is based on N-1<sup>th</sup> attribute. 
	 * <p>
	 * If it is possible all axes and the colorer are autoscaled using the data stored in the database.
	 * <p>
	 * Variant of this function generating chart in PNG format can be found here: {@link ChartGenerator#generatePNGMatrixDatasetChart(JPADataSetLO, PrintStream) 
	 * @param input The input {@link JPADataSetLO} object.
	 * @param output The stream where the result is written
	 * @throws IOException
	 */
	public static void generateSVGMatrixDatasetChart(JPADataSetLO input,PrintStream output) throws IOException{
		SVGRenderer svgr=new SVGRenderer(output, MATRIX_CHART_SIZE, MATRIX_CHART_SIZE);
		svgr.begin();
		generateMatrixDatasetChart(input,svgr,null);
		svgr.end();
		output.close();
	}
	
	public static void generateSVGMatrixDatasetChart(JPADataSetLO input,PrintStream output,AbstractDSVisResult listener) throws IOException{
		SVGRenderer svgr=new SVGRenderer(output, MATRIX_CHART_SIZE, MATRIX_CHART_SIZE);
		svgr.begin();
		generateMatrixDatasetChart(input,svgr,listener);
		svgr.end();
		output.close();
	}
	
	
	/**
	 * Generates a MatrixChart in <b>PNG</b> format for the given {@link JPADataSetLO} object.
	 * <p>
	 * Let the number of dataset's attributes be N, then the created chart has N rows and
	 * N columns and in each subchart the {@link Colorer} is based on N-1<sup>th</sup> attribute. 
	 * <p>
	 * If it is possible all axes and the colorer are autoscaled using the data stored in the database.
	 * <p>
	 * Variant of this function generating chart in SVG format can be found here: {@link ChartGenerator#generateSVGMatrixDatasetChart(JPADataSetLO, PrintStream) 
	 * @param input The input {@link JPADataSetLO} object.
	 * @param output The stream where the result is written
	 * @throws IOException
	 */
	public static void generatePNGMatrixDatasetChart(JPADataSetLO input,PrintStream output) throws IOException{
		ImageRenderer ir=new ImageRenderer(MATRIX_CHART_SIZE, MATRIX_CHART_SIZE);
		ir.begin();
		generateMatrixDatasetChart(input,ir,null);
		ir.end();
		
		ImageIO.write(ir.getImage(), "PNG", output);
		
		output.close();
	}
	
	public static void generatePNGMatrixDatasetChart(JPADataSetLO input,PrintStream output, AbstractDSVisResult listener) throws IOException{
		ImageRenderer ir=new ImageRenderer(MATRIX_CHART_SIZE, MATRIX_CHART_SIZE);
		ir.begin();
		generateMatrixDatasetChart(input,ir, listener);
		ir.end();
		
		ImageIO.write(ir.getImage(), "PNG", output);
		
		output.close();
	}
	
	private static void generateMatrixDatasetChart(JPADataSetLO input,RendererInterface renderer, AbstractDSVisResult listener) throws IOException
	{
		MultipleArffDataset dataset=new MultipleArffDataset(input);

		int attrNum=dataset.getNumberOfAttributes();
		
		MatrixChart mchg=new MatrixChart(dataset, MATRIX_CHART_SIZE, MATRIX_CHART_SIZE, attrNum, renderer, attrNum-1);
		
		while(dataset.next()){
			
			for(int row=0;row<attrNum;row++){
				for(int column=0;column<attrNum;column++){
					mchg.renderPoint(row, column, dataset.getAttributeValue(column), dataset.getAttributeValue(row), dataset.getAttributeValue(attrNum-1), 7);
				}
			}
		}

		dataset.close();
	}
	
	/**
	 * Generates a Comparison Chart for two given datasets in <b>SVG</b> format.
	 * <p>
	 * Axes are autoscaled using data from the {@link JPADataSetLO} objects. After single 
	 * autoscaling (like at Single Charts) the merging the axes and colorer intervals is done, that
	 * prevents to be chart distorted in case, that the attribute intervals are dissimilar.
	 * <p>
	 * The values from the dataset input1 are drawn using circles with larger radius.
	 * <p>
	 * Variant of this function generating PNG chart is available here: {@link ChartGenerator#generatePNGComparisonDatasetChart(JPADataSetLO, JPADataSetLO, PrintStream, int, int, int, int, int, int) 
	 * @param input1 The {@link JPADataSetLO} object of the first dataset 
	 * @param input2 The {@link JPADataSetLO} object of the second dataset
	 * @param output The output stream, where the result is written
	 * @param XIndex1 The index of attribute from the first dataset used for the X axis
	 * @param XIndex2 The index of attribute from the second dataset used for the X axis
	 * @param YIndex1 The index of attribute from the first dataset used for the Y axis
	 * @param YIndex2 The index of attribute from the second dataset used for the Y axis
	 * @param ColorIndex1 The index of attribute from the first dataset used for Colorer
	 * @param ColorIndex2 The index of attribute from the second dataset used for Colorer
	 * @throws IOException
	 * @throws AxisNotJoinableException Exception thrown when the attributes for the same axis can't be joined e.g. because they're of different types
	 * @throws ColorerNotMergeableException Exception thrown when the attributes for colorer can't be joined e.g. because they're of different types
	 */
	public static void generateSVGComparisonDatasetChart(JPADataSetLO input1,JPADataSetLO input2,PrintStream output,int XIndex1,int XIndex2,int YIndex1,int YIndex2,int ColorIndex1,int ColorIndex2) throws IOException, AxisNotJoinableException, ColorerNotMergeableException{
		SVGRenderer svgr=new SVGRenderer(output, SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		svgr.begin();
		generateComparisonDatasetChart(input1, input2, svgr, XIndex1, XIndex2, YIndex1, YIndex2, ColorIndex1, ColorIndex2);
		svgr.end();
		output.close();
	}
	
	
	/**
	 * Generates a Comparison Chart for two given datasets in <b>SVG</b> format.
	 * <p>
	 * Axes are autoscaled using data from the {@link JPADataSetLO} objects. After single 
	 * autoscaling (like at Single Charts) the merging the axes and colorer intervals is done, that
	 * prevents to be chart distorted in case, that the attribute intervals are dissimilar.
	 * <p>
	 * The values from the dataset input1 are drawn using circles with larger radius.
	 * <p>
	 * Variant of this function generating PNG chart is available here: {@link ChartGenerator#generatePNGComparisonDatasetChart(JPADataSetLO, JPADataSetLO, PrintStream, String, String, String, String, String, String) 
	 * @param input1 The {@link JPADataSetLO} object of the first dataset 
	 * @param input2 The {@link JPADataSetLO} object of the second dataset
	 * @param output The output stream, where the result is written
	 * @param XName1 The name of attribute from the first dataset used for the X axis
	 * @param XName2 The name of attribute from the second dataset used for the X axis
	 * @param YName1 The name of attribute from the first dataset used for the Y axis
	 * @param YName2 The name of attribute from the second dataset used for the Y axis
	 * @param ColorName1 The name of attribute from the first dataset used for Colorer
	 * @param ColorName2 The name of attribute from the second dataset used for Colorer
	 * @throws IOException
	 * @throws AxisNotJoinableException Exception thrown when the attributes for the same axis can't be joined e.g. because they're of different types
	 * @throws ColorerNotMergeableException Exception thrown when the attributes for colorer can't be joined e.g. because they're of different types
	 */
	public static void generateSVGComparisonDatasetChart(JPADataSetLO input1,JPADataSetLO input2,PrintStream output,String XName1,String XName2,String YName1,String YName2,String ColorName1,String ColorName2) throws IOException, AxisNotJoinableException, ColorerNotMergeableException{
		SVGRenderer svgr=new SVGRenderer(output, SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		svgr.begin();
		generateComparisonDatasetChart(input1, input2, svgr, XName1, XName2, YName1, YName2, ColorName1, ColorName2);
		svgr.end();
		output.close();
	}
	
	
	/**
	 * Generates a Comparison Chart for two given datasets in <b>PNG</b> format.
	 * <p>
	 * Axes are autoscaled using data from the {@link JPADataSetLO} objects. After single 
	 * autoscaling (like at Single Charts) the merging the axes and colorer intervals is done, that
	 * prevents to be chart distorted in case, that the attribute intervals are dissimilar.
	 * <p>
	 * The values from the dataset input1 are drawn using circles with larger radius.
	 * <p>
	 * Variant of this function generating SVG chart is available here: {@link ChartGenerator#generateSVGComparisonDatasetChart(JPADataSetLO, JPADataSetLO, PrintStream, int, int, int, int, int, int) 
	 * @param input1 The {@link JPADataSetLO} object of the first dataset 
	 * @param input2 The {@link JPADataSetLO} object of the second dataset
	 * @param output The ouput stream, where the result is written
	 * @param XIndex1 The index of attribute from the first dataset used for the X axis
	 * @param XIndex2 The index of attribute from the second dataset used for the X axis
	 * @param YIndex1 The index of attribute from the first dataset used for the Y axis
	 * @param YIndex2 The index of attribute from the second dataset used for the Y axis
	 * @param ColorIndex1 The index of attribute from the first dataset used for Colorer
	 * @param ColorIndex2 The index of attribute from the second dataset used for Colorer
	 * @throws IOException
	 * @throws AxisNotJoinableException Exception thrown when the attributes for the same axis can't be joined e.g. because they're of different types
	 * @throws ColorerNotMergeableException Exception thrown when the attributes for colorer can't be joined e.g. because they're of different types
	 */
	public static void generatePNGComparisonDatasetChart(JPADataSetLO input1,JPADataSetLO input2,PrintStream output,int XIndex1,int XIndex2,int YIndex1,int YIndex2,int ColorIndex1,int ColorIndex2) throws IOException, AxisNotJoinableException, ColorerNotMergeableException{
		ImageRenderer ir=new ImageRenderer(SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		ir.begin();
		generateComparisonDatasetChart(input1, input2, ir, XIndex1, XIndex2, YIndex1, YIndex2, ColorIndex1, ColorIndex2);
		ir.end();
		output.close();
		ImageIO.write(ir.getImage(), "PNG", output);
		output.close();
	}
	
	
	/**
	 * Generates a Comparison Chart for two given datasets in <b>PNG</b> format.
	 * <p>
	 * Axes are autoscaled using data from the {@link JPADataSetLO} objects. After single 
	 * autoscaling (like at Single Charts) the merging the axes and colorer intervals is done, that
	 * prevents to be chart distorted in case, that the attribute intervals are dissimilar.
	 * <p>
	 * The values from the dataset input1 are drawn using circles with larger radius.
	 * <p>
	 * Variant of this function generating SVG chart is available here: {@link ChartGenerator#generateSVGComparisonDatasetChart(JPADataSetLO, JPADataSetLO, PrintStream, String, String, String, String, String, String) 
	 * @param input1 The {@link JPADataSetLO} object of the first dataset 
	 * @param input2 The {@link JPADataSetLO} object of the second dataset
	 * @param output The ouput stream, where the result is written
	 * @param XName1 The name of attribute from the first dataset used for the X axis
	 * @param XName2 The name of attribute from the second dataset used for the X axis
	 * @param YName1 The name of attribute from the first dataset used for the Y axis
	 * @param YName2 The name of attribute from the second dataset used for the Y axis
	 * @param ColorName1 The name of attribute from the first dataset used for Colorer
	 * @param ColorName2 The name of attribute from the second dataset used for Colorer
	 * @throws IOException
	 * @throws AxisNotJoinableException Exception thrown when the attributes for the same axis can't be joined e.g. because they're of different types
	 * @throws ColorerNotMergeableException Exception thrown when the attributes for colorer can't be joined e.g. because they're of different types
	 */
	public static void generatePNGComparisonDatasetChart(JPADataSetLO input1,JPADataSetLO input2,PrintStream output,String XName1,String XName2,String YName1,String YName2,String ColorName1,String ColorName2) throws IOException, AxisNotJoinableException, ColorerNotMergeableException{
		ImageRenderer ir=new ImageRenderer(SINGLE_CHART_SIZE, SINGLE_CHART_SIZE);
		ir.begin();
		generateComparisonDatasetChart(input1, input2, ir, XName1, XName2, YName1, YName2, ColorName1, ColorName2);
		ir.end();
		output.close();
		ImageIO.write(ir.getImage(), "PNG", output);
		output.close();
	}
	
	
	private static void generateComparisonDatasetChart(JPADataSetLO input1,JPADataSetLO input2,RendererInterface renderer,int XIndex1,int XIndex2,int YIndex1,int YIndex2,int ColorIndex1,int ColorIndex2) throws IOException, AxisNotJoinableException, ColorerNotMergeableException{
		
		SingleArffDataset dataset1=new SingleArffDataset(
				input1,
				XIndex1, 
				YIndex1,
				ColorIndex1);
		
		SingleArffDataset dataset2=new SingleArffDataset(
				input2,
				XIndex2, 
				YIndex2,
				ColorIndex2);
		generateComparisonDatasetChartFromDataset(dataset1, dataset2, renderer);
		
	}
	
	
	private static void generateComparisonDatasetChart(JPADataSetLO input1,JPADataSetLO input2,RendererInterface renderer,String XName1,String XName2,String YName1,String YName2,String ColorName1,String ColorName2) throws IOException, AxisNotJoinableException, ColorerNotMergeableException{
		
		SingleArffDataset dataset1=new SingleArffDataset(
				input1,
				XName1, 
				YName1,
				ColorName1);
		
		SingleArffDataset dataset2=new SingleArffDataset(
				input2,
				XName2, 
				YName2,
				ColorName2);
		generateComparisonDatasetChartFromDataset(dataset1, dataset2, renderer);
		
	}
	
	
	
	private static void generateComparisonDatasetChartFromDataset(SingleArffDataset dataset1,SingleArffDataset dataset2,RendererInterface renderer) throws IOException, AxisNotJoinableException, ColorerNotMergeableException{

		Axis yAxis=Axis.join(dataset1.getYAxis(), dataset2.getYAxis());
		Axis xAxis=Axis.join(dataset1.getXAxis(), dataset2.getXAxis());
		Colorer colorer1=dataset1.getZColorer();
		Colorer colorer2=dataset2.getZColorer();
		
		Colorer colorer=colorer1.merge(colorer2);
		
		SingleChart sch=new SingleChart(SINGLE_CHART_SIZE, SINGLE_CHART_SIZE, renderer, xAxis, yAxis);
		sch.setMargin(50);
		sch.setLabelSize(150);
		sch.setBackGroundColor(Color.getHSBColor(0.0f, 0.0f, 0.95f));
		sch.setHorizontalCaptionColor(Color.GRAY);
		sch.setVerticalCaptionColor(Color.GRAY);
		sch.setCaptionSize(35);

		sch.startChart();

		ArffXYZPoint next=null;

		while((next=dataset1.getNext())!=null){
			sch.renderPoint(next.getX(), next.getY(), next.getZ(), 17, colorer);
		}
		dataset1.close();
		
		while((next=dataset2.getNext())!=null){
			sch.renderPoint(next.getX(), next.getY(), next.getZ(), 12, colorer);
		}

		dataset2.close();
		
	}
}
