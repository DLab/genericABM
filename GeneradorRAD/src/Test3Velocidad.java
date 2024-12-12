import java.util.ArrayList;

public class Test3Velocidad implements Cloneable
{
	protected double pro1;
	protected double pro2;
	protected double pro3;
	protected double pro4;
	protected String name;
	protected ArrayList<Test2Item> items;
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		ArrayList<Test2Item> items = this.items;
		this.items = null;
		Test3Velocidad obj = (Test3Velocidad) super.clone();
		//System.out.println(items == obj.items);
		obj.items = new ArrayList<Test2Item>();
		for (Test2Item item : items)
		{
			obj.items.add((Test2Item)item.clone());
		}
		this.items = items;
		return obj;
	}
	
	public static void main(String[] args) throws CloneNotSupportedException
	{
		long t = System.nanoTime();
		Test3Velocidad test = new Test3Velocidad();
		test.pro1 = 1;
		test.pro2 = 1;
		test.pro3 = 1;
		test.pro4 = 1;
		test.name = "Test";
		test.items = new ArrayList<Test2Item>();
		Test2Item item = new Test2Item();
		item.a1 = 1;
		item.a2 = 1;
		test.items.add(item);
		double t2, t3;
		System.out.println("tiempo:" + (t2 = (System.nanoTime() -t)));
		t = System.nanoTime();
		test = (Test3Velocidad)test.clone();
		System.out.println("tiempo:" + (t3 = (System.nanoTime() -t)));
		System.out.println(t2 /t3);
		
	}
}
