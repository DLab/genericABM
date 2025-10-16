
public class TestEchoPython implements Runnable
{
	
	public static void main(String[] args) throws Exception
	{
		for (int i = 0; i < 1; i++)
		{
			new Thread(new TestEchoPython()).start();
		}
	}

	@Override
	public void run()
	{
		try
		{
			String url = "http://127.0.0.1:5000/echo";
			long t = System.currentTimeMillis();
			int n = 1;
			for (int i = 0; i < n; i++)
			{
				Utils.sendPostData(url, new Param("test", ""));
			}
			t = (System.currentTimeMillis() - t);
			System.out.println("Tiempo usado:" + t + ", promedio:" + ((double)t / (double)n));

		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
