package org.pikater.web.vaadin.gui.server.ui_visualization.components;

import java.util.Collection;

import org.pikater.web.vaadin.gui.server.StyleBuilder;
import org.pikater.web.vaadin.gui.server.layouts.flowlayout.IFlowLayoutStyleProvider;
import org.pikater.web.vaadin.gui.server.layouts.matrixlayout.IMatrixDataSource;
import org.pikater.web.vaadin.gui.server.layouts.matrixlayout.MatrixLayout;
import org.pikater.web.vaadin.gui.server.ui_visualization.VisualizationUI.DSVisTwoUIArgs;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.result.DSVisTwoSubresult;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@StyleSheet("visualizationComponent.css")
public class CompareDatasetsVisualizer extends VerticalLayout
{
	private static final long serialVersionUID = 3682122092659178186L;

	public CompareDatasetsVisualizer(final DSVisTwoUIArgs arguments)
	{
		super();
		setSizeFull();
		setStyleName("visualizationComponent");
		setSpacing(true);
		
		// define the header
		Label lbl_visType = new Label("DATASET COMPARISON");
		Label lbl_leftDataset = new Label(String.format("LEFT ATTRIBUTES: '%s'", arguments.getDataset1().getFileName()));
		Label lbl_topDataset = new Label(String.format("TOP ATTRIBUTES: '%s'", arguments.getDataset2().getFileName()));
		
		final IMatrixDataSource<AttrMapping, DSVisTwoSubresult> resultMatrixView = arguments.getGeneratedResult().toMatrixView();
		MatrixLayout<AttrMapping> matrixLayout = new MatrixLayout<AttrMapping>(new IMatrixDataSource<AttrMapping, ChartThumbnail>()
		{
			@Override
			public Collection<AttrMapping> getLeftIndexSet()
			{
				return resultMatrixView.getLeftIndexSet();
			}

			@Override
			public Collection<AttrMapping> getTopIndexSet()
			{
				return resultMatrixView.getTopIndexSet();
			}

			@Override
			public ChartThumbnail getElement(AttrMapping leftIndex, AttrMapping topIndex)
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

		// and display it
		addComponent(lbl_visType);
		addComponent(lbl_leftDataset);
		addComponent(lbl_topDataset);
		addComponent(matrixLayout);
		setExpandRatio(matrixLayout, 1);
	}
}