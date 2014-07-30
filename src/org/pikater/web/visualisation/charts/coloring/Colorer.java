package org.pikater.web.visualisation.charts.coloring;

import java.awt.Color;

import org.pikater.web.visualisation.charts.coloring.exception.ColorerNotMergeableException;

public interface Colorer {
	public Color getColor(double value);
	public Colorer merge(Colorer colorer) throws ColorerNotMergeableException;
}
