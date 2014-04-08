package org.pikater.core.ontology.description;

import jade.content.Concept;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class Interval implements Concept {

    double min;
    double max;

    public Interval(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public Interval(){};

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
