package pikater.utility.pikaterDatabase.tables.experiments.slots;

public abstract class UniversalConsumerSlot extends UniversalSlot {

	private UniversalProviderSlot providerSlot = null;
	
	public abstract boolean connect(UniversalProviderSlot providerSlot);

	public void setProviderSlot(UniversalProviderSlot providerSlot) {
		this.providerSlot = providerSlot;
	}
	public UniversalProviderSlot getProviderSlot() {
		return this.providerSlot;
	}

	public boolean disCconnect(UniversalProviderSlot providerSlot) {
		
		if (this.providerSlot == providerSlot) {
			this.providerSlot = null;
			return true;
		}
		return false;
	}

}
