package org.pikater.web.vaadin.gui.server.ui_visualization.components;

import java.util.Collection;

import org.pikater.web.vaadin.gui.server.StyleBuilder;
import org.pikater.web.vaadin.gui.server.layouts.flowlayout.IFlowLayoutStyleProvider;
import org.pikater.web.vaadin.gui.server.layouts.matrixlayout.IMatrixDataSource;
import org.pikater.web.vaadin.gui.server.layouts.matrixlayout.MatrixLayout;
import org.pikater.web.vaadin.gui.server.ui_visualization.VisualizationUI.DSVisOneUIArgs;
import org.pikater.web.visualisation.definition.result.DSVisOneSubresult;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@StyleSheet("visualizationComponent.css")
public class SingleDatasetVisualizer extends VerticalLayout
{
	private static final long serialVersionUID = 8665905099485047156L;
	
	public SingleDatasetVisualizer(final DSVisOneUIArgs arguments)
	{
		super();
		setSizeFull();
		setStyleName("visualizationComponent");
		setSpacing(true);
		
		// define the header
		Label lbl_visType = new Label("SINGLE DATASET VISUALIZATION");
		Label lbl_Dataset = new Label(String.format("DATASET: '%s'", arguments.getDataset().getFileName()));
		
		// define the matrix view
		Panel matrixContainer = new Panel();
		final IMatrixDataSource<String, DSVisOneSubresult> resultMatrixView = arguments.getGeneratedResult().toMatrixView();
		MatrixLayout<String> matrixLayout = new MatrixLayout<String>(new IMatrixDataSource<String, ChartThumbnail>()
		{
			@Override
			public Collection<String> getLeftIndexSet()
			{
				return resultMatrixView.getLeftIndexSet();
			}

			@Override
			public Collection<String> getTopIndexSet()
			{
				return resultMatrixView.getTopIndexSet();
			}

			@Override
			public ChartThumbnail getElement(String leftIndex, String topIndex)
			{
				return new ChartThumbnail(
						resultMatrixView.getElement(leftIndex, topIndex),
						arguments.getGeneratedResult().getImageWidth(),
						arguments.getGeneratedResult().getImageHeight()
				);
			}
		}, new IFlowLayoutStyleProvider()
		{
			@Override
			public void setStylesForInnerComponent(Component c, StyleBuilder builder)
			{
				builder.setProperty("margin", "10px");
			}
		});
		matrixLayout.setSizeFull();
		matrixContainer.setSizeFull();
		matrixContainer.setContent(matrixLayout);

		// and display it
		addComponent(lbl_visType);
		addComponent(lbl_Dataset);
		addComponent(matrixContainer);
		setExpandRatio(matrixContainer, 1);
	}
}