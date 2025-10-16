import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadContent2
{

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
	{
		String id = args[0];
		int n = Integer.parseInt(args[1]);
		System.out.println(id + "**" + n);
		n--;
		File[] files = new File(".").listFiles();
		File file = new File("sim_ss_" + id + ".csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write("conf_ini;rep_ini;rcp_ini;rh0_0;rh0_" + n + ";pozo_0;pozo_1;pozo_" + (n-1) + ";pozo_" + n);
		bw.newLine();
		for (File f : files)
		{
			System.out.println(f.getName() + "**" + f.getName().startsWith("sim_" + id));
			if (f.getName().startsWith("sim_" + id) && f.getName().endsWith(".out"))
			{
				try(ObjectInputStream oi = new ObjectInputStream(new FileInputStream(f)))
				{
					Object[] obj = (Object[])oi.readObject();
					HashMap<String, Object> model = (HashMap<String, Object>)obj[0];
					ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String,Object>>)obj[1];
					for (HashMap<String, Object> hs : data)
					{
						double[] rh0 = (double[])hs.get("rho");
						double[] pozo = (double[])hs.get("pozo");
						
						bw.write(model.get("initConf") 
						 + ";" + model.get("initRepu")
						 + ";" + model.get("initReci")
						 + ";" + rh0[0]
						 + ";" + rh0[n]
						 + ";" + pozo[0]
						 + ";" + pozo[1]
						 + ";" + pozo[n - 1]
						 + ";" + pozo[n]
						 );
						bw.newLine();
						bw.flush();
					}
				}
			}
		}
			
		bw.close();
	}
}
