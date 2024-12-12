package cl.dlab.abm.core.model.function;

public interface Function {

	public double getValue(double... params);
	public void setValues(double... params);
	public void clear();
	public String getName();
	public double[] getParams();
}
