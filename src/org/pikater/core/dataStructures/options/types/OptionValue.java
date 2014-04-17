package org.pikater.core.dataStructures.options.types;




public class OptionValue extends AbstractOption {

	private Object object = null;

	public OptionValue() {}

	public OptionValue(Object object) {
		this.object = object;
	}

	@Override
	public Class<? extends Object> getOptionClass() {
		return this.object.getClass();
	}

	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
}
