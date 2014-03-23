package pikater.utility.pikaterDatabase.tables.experiments.parameters;


public class UBooleanParameter extends UniversalParameter {

	private boolean value;

    public void setValue(boolean value) {
    	this.value = value;
    }
    public boolean getValue() {
    	return this.value;
    }

}
