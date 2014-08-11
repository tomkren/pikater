package org.pikater.web.vaadin.gui.server.layouts.flowlayout;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.CssLayout;

@StyleSheet("flowLayout.css")
public abstract class AbstractFlowLayout extends CssLayout 
{
	private static final long serialVersionUID = -6039020584387592993L;
	
	private final IFlowLayoutStyleProvider styleProvider;
	
	public AbstractFlowLayout()
	{
		this(null);
	}
	
	/**
	 * Creates a horizontal flow layout with the given style provider for
	 * inner components.
	 */
	public AbstractFlowLayout(IFlowLayoutStyleProvider styleProvider)
	{
		super();
		setPrimaryStyleName("flowLayout");
		
		this.styleProvider = styleProvider;
	}
	
	protected IFlowLayoutStyleProvider getStyleProvider()
	{
		return styleProvider;
	}
}