package pikater.utility.pikaterDatabase.tables.experiments.slots;

public class DataConsumerSlot extends UniversalConsumerSlot {

	public boolean connect(UniversalProviderSlot providerSlot) {

		if (providerSlot instanceof DataProviderSlot) {
			this.setProviderSlot(providerSlot);
			return true;
		} else {
			return false;
		}

	}

}
