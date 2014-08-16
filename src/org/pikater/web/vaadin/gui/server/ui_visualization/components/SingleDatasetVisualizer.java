package org.pikater.web.vaadin.gui.server.ui_visualization.components;

import java.util.Collection;

import org.pikater.web.vaadin.gui.server.StyleBuilder;
import org.pikater.web.vaadin.gui.server.layouts.flowlayout.IFlowLayoutStyleProvider;
import org.pikater.web.vaadin.gui.server.layouts.matrixlayout.IMatrixDataSource;
import org.pikater.web.vaadin.gui.server.layouts.matrixlayout.MatrixLayout;
import org.pikater.web.vaadin.gui.server.ui_visualization.VisualizationUI.DSVisOneUIArgs;
import org.pikater.web.visualisation.definition.result.DSVisOneSubresult;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class SingleDatasetVisualizer extends VerticalLayout
{
	private static final long serialVersionUID = 8665905099485047156L;
	
	public SingleDatasetVisualizer(final DSVisOneUIArgs arguments)
	{
		super();
		setSizeFull();
		
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

		// and display it
		addComponent(matrixLayout);
		setExpandRatio(matrixLayout, 1);
	}
}