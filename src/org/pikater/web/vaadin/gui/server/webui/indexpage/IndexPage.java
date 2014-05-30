package org.pikater.web.vaadin.gui.server.webui.indexpage;

import org.pikater.web.vaadin.gui.server.components.borderlayout.AutoVerticalBorderLayout;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.DefaultFeature;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.IWebFeature;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionMode;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Row;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractComponent;

@StyleSheet("indexPage.css")
public class IndexPage extends AutoVerticalBorderLayout
{
	private static final long serialVersionUID = -5286072324040828820L;
	
	private final ContentArea contentArea;
	
	/*
	 * TODO: add a close handler to this UI and ask the content component if it has unsaved progress...
	 * 
	 * TODO: box definition changes will take an application restart... only make the RPC method a one-time push?
	 * TODO: BoxInfo reference should be a reversible IDs... since box definitions have no decent IDs, we have to make
	 * them in a fashion that will allow us to find the substitute, unless referenced directly, as it is now. In that
	 * case we will have to manually check for newer versions when validating the experiments.
	 * TODO: cellBrowserDnD drags the label component instead of the custom inner layout sometimes and class cast issues occur...
	 * TODO: adding datasets: ARFF, CSV, XLS
	 */
	
	public IndexPage()
	{
		super();
		setSizeFull();
		
		this.contentArea = new ContentArea();
		
		setBorderSpacing(0);
		setCellPadding(0);
		setCellSpacing(0);
		setComponent(Border.NORTH, new BannerArea(this));
		setComponent(Border.WEST, new LeftMenu(this));
		setComponent(Border.CENTER, contentArea); 
		setComponentVisible(Border.EAST, false);
		setColumnWidth(Column.CENTER, DimensionMode.MAX);
		
		setRowVisible(Row.SOUTH, false);
		setRowHeight(Row.CENTER, DimensionMode.MAX);
		
		setContentAreaComponent(DefaultFeature.DEFAULT);
		// setTestContentAreaComponent(TestContent.testMultiFileUpload());
		// setTestContentAreaComponent(TestContent.testJSCH());
		// setTestContentAreaComponent(TestContent.testEditor(!getSession().getConfiguration().isProductionMode()));
	}
	
	public void setContentAreaComponent(IWebFeature feature)
	{
		this.contentArea.setContent(feature);
	}
	
	/**
	 * <font color="red">RED ALERT:</font> use this really wisely because it forcibly override the content
	 * without any add-on features that {@link #setContentAreaComponent} provides.
	 * @param component
	 */
	@SuppressWarnings("unused")
	private void setTestContentAreaComponent(AbstractComponent component)
	{
		this.contentArea.setContent(component);
	}
}