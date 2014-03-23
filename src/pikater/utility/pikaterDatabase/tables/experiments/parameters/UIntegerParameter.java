package pikater.utility.pikaterDatabase.tables.experiments.parameters;

import java.util.ArrayList;


public class UIntegerParameter extends UniversalParameter {

    private Integer value;
    private UIntegerInterval range;
    private ArrayList<Integer> set;
    
    public void setValue(Integer value) {
    	this.value = value;
    }
    public Integer getValue() {
    	return this.value;
    }

    public void setRange(UIntegerInterval range) {
    	this.range = range;
    }
    public UIntegerInterval getRange() {
    	return this.range;
    }

    public void setSet(ArrayList<Integer> set) {
    	this.set = set;
    }
    public ArrayList<Integer> getSet() {
    	return this.set;
    }

}
