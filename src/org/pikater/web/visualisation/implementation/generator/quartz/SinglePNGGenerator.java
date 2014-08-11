package org.pikater.web.visualisation.implementation.generator.quartz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.visualisation.definition.result.AbstractDSVisResult;
import org.pikater.web.visualisation.implementation.generator.ChartGenerator;
import org.pikater.web.visualisation.implementation.renderer.ImageRenderer;

public class SinglePNGGenerator extends SingleGenerator {
	
	public SinglePNGGenerator(AbstractDSVisResult progressListener, JPADataSetLO dslo,PrintStream output, int XIndex, int YIndex, int ColorIndex){
		super(progressListener,dslo,output,XIndex,YIndex,ColorIndex);
		initRenderer();
	}
	
	public SinglePNGGenerator(AbstractDSVisResult progressListener, JPADataSetLO dslo,PrintStream output, String XName, String YName, String ColorName){
		super(progressListener,dslo,output,XName,YName,ColorName);
		initRenderer();
	}
	
	public SinglePNGGenerator(AbstractDSVisResult progressListener, JPADataSetLO dslo,File datasetCachedFile, PrintStream output, int XIndex, int YIndex, int ColorIndex){
		super(progressListener,dslo, datasetCachedFile, output,XIndex,YIndex,ColorIndex);
		initRenderer();
	}
	
	public SinglePNGGenerator(AbstractDSVisResult progressListener, JPADataSetLO dslo, File datasetCachedFile, PrintStream output, String XName, String YName, String ColorName) {
		super(progressListener, dslo, datasetCachedFile, output, XName, YName, ColorName);
		initRenderer();
	}

	private void initRenderer(){
		this.renderer=new ImageRenderer(ChartGenerator.SINGLE_CHART_SIZE, ChartGenerator.SINGLE_CHART_SIZE);
	}

	@Override
	public void create() throws IOException {
		super.create();
		BufferedImage outIm=((ImageRenderer)renderer).getImage();
		ImageIO.write(outIm, "PNG", output);
		output.close();
		dataset.close();
	}

}
