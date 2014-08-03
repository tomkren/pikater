package org.pikater.web.vaadin.gui.server.components.flowlayout;

import org.pikater.web.vaadin.gui.server.StyleBuilder;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

@StyleSheet("flowLayout.css")
public class FlowLayout extends CssLayout 
{
	private static final long serialVersionUID = -6039020584387592993L;
	
	private final LayoutOrientation orientation;
	private final IFlowLayoutStyleProvider styleProvider;
	
	/**
	 * Creates a horizontal flow layout.
	 */
	public FlowLayout()
	{
		this(LayoutOrientation.HORIZONTAL);
	}
	
	/**
	 * Creates a horizontal flow layout with the given style provider for
	 * inner components.
	 */
	public FlowLayout(IFlowLayoutStyleProvider styleProvider)
	{
		this(LayoutOrientation.HORIZONTAL, styleProvider);
	}
	
	/**
	 * Creates a flow layout with the given flow orientation.
	 * @param orientation
	 */
	public FlowLayout(LayoutOrientation orientation)
	{
		this(orientation, null);
	}
	
	/**
	 * Creates a flow layout with the given flow orientation and style provider
	 * for inner components. 
	 * @param orientation
	 * @param styleProvider
	 */
	public FlowLayout(LayoutOrientation orientation, IFlowLayoutStyleProvider styleProvider)
	{
		super();
		setPrimaryStyleName("flowLayout");
		
		this.orientation = orientation;
		this.styleProvider = styleProvider;
	}

	@Override
	protected String getCss(Component c)
	{
		StyleBuilder builder = new StyleBuilder();
		if(styleProvider != null)
		{
			styleProvider.setStylesForInnerComponent(c, builder);
		}
		switch(orientation)
		{
			case HORIZONTAL:
				builder.setProperty("float", "left");
				break;
			case VERTICAL:
				builder.setProperty("display", "block");
				break;
			default:
				throw new IllegalStateException("Unknown state: " + orientation.name());
		}
		return builder.build();
	}
	
	//-------------------------------------------------------
	// SPECIAL TYPES
	
	public enum LayoutOrientation
	{
		HORIZONTAL,
		VERTICAL
	}
}