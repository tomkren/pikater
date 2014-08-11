package org.pikater.web.vaadin.gui.server.layouts.flowlayout;

import java.util.HashSet;
import java.util.Set;

import org.pikater.web.vaadin.gui.server.StyleBuilder;

import com.vaadin.ui.Component;

public class HorizontalFlowLayout extends AbstractFlowLayout
{
	private static final long serialVersionUID = 6951568571587805444L;
	
	private final Set<Component> componentsAddedToRight;

	/**
	 * Creates a horizontal flow layout with the given style provider for
	 * inner components.
	 */
	public HorizontalFlowLayout(IFlowLayoutStyleProvider styleProvider)
	{
		super(styleProvider);
		
		this.componentsAddedToRight = new HashSet<Component>();
	}
	
	/**
	 * Adds the specified component from right (applies a float: right property).
	 * @param c
	 */
	public void addComponentToRight(Component c)
	{
		super.addComponent(c);
		componentsAddedToRight.add(c);
	}
	
	@Override
	public void removeComponent(Component c)
	{
		// TODO: this is not resistant to multiple addition
		super.removeComponent(c);
		componentsAddedToRight.remove(c);
	}
	
	@Override
	public void removeAllComponents()
	{
		super.removeAllComponents();
		componentsAddedToRight.clear();
	}
	
	@Override
	protected String getCss(Component c)
	{
		StyleBuilder builder = new StyleBuilder();
		if(getStyleProvider() != null)
		{
			getStyleProvider().setStylesForInnerComponent(c, builder);
		}
		builder.setProperty("float", componentsAddedToRight.contains(c) ? "right" : "left");
		return builder.build();
	}
}