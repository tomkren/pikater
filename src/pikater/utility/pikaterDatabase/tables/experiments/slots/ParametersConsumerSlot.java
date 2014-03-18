package pikater.utility.pikaterDatabase.tables.experiments.slots;

public class ParametersConsumerSlot extends UniversalConsumerSlot {

	public boolean connect(UniversalProviderSlot providerSlot) {

		if (providerSlot instanceof ParametersProviderSlot) {
			this.setProviderSlot(providerSlot);
			return true;
		} else {
			return false;
		}

	}

}
