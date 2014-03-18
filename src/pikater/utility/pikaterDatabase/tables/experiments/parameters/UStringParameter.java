package pikater.utility.pikaterDatabase.tables.experiments.parameters;

import java.util.ArrayList;

public class UStringParameter extends UniversalParameter {

	private String value;
    private ArrayList<String> set;


	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return this.value;
	}

	public void setSet(ArrayList<String> set) {
		this.set = set;
	}
	public ArrayList<String> getSet() {
		return this.set;
	}

}
