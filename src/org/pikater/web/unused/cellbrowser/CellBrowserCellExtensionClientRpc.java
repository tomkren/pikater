package org.pikater.web.unused.cellbrowser;

import com.vaadin.shared.communication.ClientRpc;

/** 
 * @author SkyCrawl 
 */
public interface CellBrowserCellExtensionClientRpc extends ClientRpc
{
	void select();
	void deselect();
}