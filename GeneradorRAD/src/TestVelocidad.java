public class TestVelocidad
{
	private double d;
	private String name;
	public static void main(String[] args)
	{
		long t2, t3;
		
		TestVelocidad model = new TestVelocidad();
		model.name = "aa";
		Long t = System.nanoTime();
		model.setName(model.getName() + "xxx");
		System.out.println(t2 = (System.nanoTime() - t));
		
		
		
		model.name = "aa";
		t = System.nanoTime();
		model.name = model.name + "www";
		System.out.println(t3 = (System.nanoTime() - t));
		System.out.println("nro:" + (t2 / t3));
		
		model.d = 0;
		t = System.nanoTime();
		model.setD(model.getD() + 1);
		System.out.println(t2 = (System.nanoTime() - t));
		model.d = 0;
		t = System.nanoTime();
		model.d = model.d + 1;
		System.out.println(t3 = (System.nanoTime() - t));
		System.out.println("nro:" + (t2 / t3));
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the d
	 */
	public double getD()
	{
		return d;
	}
	/**
	 * @param d the d to set
	 */
	public void setD(double d)
	{
		this.d = d;
	}
}
