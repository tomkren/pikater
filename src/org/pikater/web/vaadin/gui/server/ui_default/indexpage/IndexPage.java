package org.pikater.web.vaadin.gui.server.ui_default.indexpage;

import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.gui.server.components.borderlayout.AutoVerticalBorderLayout;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.DefaultFeature;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IWebFeatureSet;
import org.pikater.web.vaadin.gui.server.ui_expeditor.ExpEditorUI.ExpEditorUIServlet;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionMode;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionUnit;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Row;

import com.vaadin.annotations.StyleSheet;

@StyleSheet("indexPage.css")
public class IndexPage extends AutoVerticalBorderLayout
{
	private static final long serialVersionUID = -5286072324040828820L;
	
	private final ContentArea contentArea;
	
	public IndexPage()
	{
		super();
		setStyleName("topLevelElement");
		
		this.contentArea = new ContentArea();
		
		setBorderSpacing(0);
		setCellPadding(0);
		setCellSpacing(0);
		setComponent(Border.NORTH, new BannerArea(this));
		setComponent(Border.WEST, new LeftMenu(this));
		setComponent(Border.CENTER, contentArea); 
		setComponentVisible(Border.EAST, false);
		setColumnWidth(Column.CENTER, 100, DimensionUnit.PCT);
		
		setRowVisible(Row.SOUTH, false);
		setRowHeight(Row.CENTER, DimensionMode.MAX);
		
		openContent(DefaultFeature.WELCOME);
		// openContent(DefaultFeature.TEST);
	}
	
	public void openContent(IWebFeatureSet feature)
	{
		if(feature.shouldOpenInSeperateTab())
		{
			/*
			 * TODO: if already exists... set focus to the right tab? Unfortunately, GWT (or JS)
			 * doesn't support that because of security reasons.
			 */
			
			String urlToOpen = CustomConfiguredUI.getBaseAppURLFromLastRequest() + ExpEditorUIServlet.mainURLPattern; 
			getUI().getPage().setLocation(urlToOpen);
		}
		else
		{
			this.contentArea.setContentView(feature);
		}
	}
}