package pikater.utility.pikaterDatabase.tables.experiments;

import pikater.utility.boxTypes.Box;

public abstract class UniversalDataFlow {

	protected Box boxDescription;
	protected int id;

	public Box getBoxDescription() {
		return boxDescription;
	}
	public abstract void setBoxDescription(Box boxDescription);


}
