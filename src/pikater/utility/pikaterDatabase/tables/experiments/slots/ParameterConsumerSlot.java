package pikater.utility.pikaterDatabase.tables.experiments.slots;

import pikater.utility.pikaterDatabase.tables.experiments.parameters.UniversalParameter;

public class ParameterConsumerSlot extends ParametersConsumerSlot {
	
	private UniversalParameter parameter;

	public UniversalParameter getParameter() {
		return parameter;
	}
	public void setParameter(UniversalParameter parameter) {
		this.parameter = parameter;
	}

}
