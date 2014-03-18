package pikater.utility.pikaterDatabase.tables.experiments.parameters;

import java.util.ArrayList;


public class UDoubleParameter extends UniversalParameter {

    private double value;
    private UDoubleInterval range;
    private ArrayList<Double> set;
    
    public void setValue(double value) {
    	this.value = value;
    }
    public double getValue() {
    	return this.value;
    }

    public void setRange(UDoubleInterval range) {
    	this.range = range;
    }
    public UDoubleInterval getRange() {
    	return this.range;
    }

    public void setSet(ArrayList<Double> set) {
    	this.set = set;
    }
    public ArrayList<Double> getSet() {
    	return this.set;
    }

}
