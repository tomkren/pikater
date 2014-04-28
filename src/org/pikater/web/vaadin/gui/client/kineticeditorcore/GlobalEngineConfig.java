package org.pikater.web.vaadin.gui.client.kineticeditorcore;

public class GlobalEngineConfig
{
	// *************************************************************************************
	// COMPONENT NAMES - ALL MUST BE UNIQUE
	
	public static final String name_box_container = "boxContainer";
	public static final String name_box_masterRectangle = "boxMasterShape";
	public static final String name_box_textLabel = "boxTextLabel";
	
	public static final String name_edge_container = "edgeContainer";
	public static final String name_edge_arrrow = "edgeArrow";
	public static final String name_edge_fromDrag = "edgeFDM";
	public static final String name_edge_toDrag = "edgeTDM";
	public static final String name_edge_baseLine = "edgeBaseline";
	
	// *************************************************************************************
	// Z-INDEX ASSIGNMENT
	
	public static final int zIndex_box_masterRectangle = 2;
	public static final int zIndex_edge_baseLine = 3;
	public static final int zIndex_edge_container = 3;
	
	// *************************************************************************************
	// SOME ROUTINES
	
	public static String getBoxIDFromNumber(int number)
	{
		return "box" + number;
	}
}
