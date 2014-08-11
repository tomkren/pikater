package org.pikater.web.vaadin.gui.server.layouts.flowlayout;

import org.pikater.web.vaadin.gui.server.StyleBuilder;

import com.vaadin.ui.Component;

public class VerticalFlowLayout extends AbstractFlowLayout
{
	private static final long serialVersionUID = 7467817672610031711L;

	/**
	 * Creates a vertical flow layout with the given style provider for
	 * inner components.
	 */
	public VerticalFlowLayout(IFlowLayoutStyleProvider styleProvider)
	{
		super(styleProvider);
	}
	
	@Override
	protected String getCss(Component c)
	{
		StyleBuilder builder = new StyleBuilder();
		if(getStyleProvider() != null)
		{
			getStyleProvider().setStylesForInnerComponent(c, builder);
		}
		builder.setProperty("display", "block");
		return builder.build();
	}
}