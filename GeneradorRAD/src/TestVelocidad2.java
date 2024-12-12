public class TestVelocidad2 implements Cloneable
{
	protected double pro1;
	protected double pro2;
	protected double pro3;
	protected double pro4;
	protected String name;
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return (TestVelocidad2) super.clone();
	}
	
	public static void main(String[] args) throws CloneNotSupportedException
	{
		long t = System.nanoTime();
		TestVelocidad2 test = new TestVelocidad2();
		test.pro1 = 1;
		test.pro2 = 1;
		test.pro3 = 1;
		test.pro4 = 1;
		test.name = "Test";
		double t2, t3;
		System.out.println("tiempo:" + (t2 = (System.nanoTime() -t)));
		t = System.nanoTime();
		test = (TestVelocidad2)test.clone();
		System.out.println("tiempo:" + (t3 = (System.nanoTime() -t)));
		System.out.println(t2 /t3);
		
	}
}
