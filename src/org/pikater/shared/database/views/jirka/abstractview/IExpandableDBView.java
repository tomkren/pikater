package org.pikater.shared.database.views.jirka.abstractview;

public interface IExpandableDBView
{
	/**
	 * Expand this row view into a full-fledged detailed table view.
	 * @return null, if no further view can be constructed/displayed
	 */
	AbstractTableDBView getNextView();
}
