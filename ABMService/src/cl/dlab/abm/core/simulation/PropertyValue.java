package cl.dlab.abm.core.simulation;

public class PropertyValue<T> {
	private String property;
	private T[] value;
	
	public PropertyValue(String property, T[] value)
	{
		this.property = property;
		this.value = value;
	}
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public T[] getValue() {
		return value;
	}
	public void setValue(T[] value) {
		this.value = value;
	}
}
