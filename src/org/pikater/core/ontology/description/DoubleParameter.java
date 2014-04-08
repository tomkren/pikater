package org.pikater.core.ontology.description;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class DoubleParameter extends Parameter {

    double value;
    Interval range;
    ArrayList<Double> set;

    public DoubleParameter(String name, double value) {
        this.value = value;
        setName(name);
    }

    public DoubleParameter() {}

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Interval getRange() {
        return range;
    }

    public void setRange(Interval range) {
        this.range = range;
    }

    public ArrayList<Double> getSet() {
        return set;
    }

    public void setSet(ArrayList<Double> set) {
        this.set = set;
    }
}
