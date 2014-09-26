package org.pikater.web.visualisation.implementation.charts.coloring;

import java.awt.Color;

import org.pikater.web.visualisation.implementation.exceptions.ColorerNotMergeableException;

public interface Colorer {
	public Color getColor(double value);
	public Colorer merge(Colorer colorer) throws ColorerNotMergeableException;
}
