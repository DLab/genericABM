package cl.dlab.abm.core.kqml;

import java.util.ArrayList;

import org.json.JSONObject;

import cl.dlab.abm.util.Param;
import cl.dlab.abm.util.Utils;
import cl.dlab.util.LogUtil;
import cl.dlab.util.PropertyUtil;

public class KqmlIntegration implements Runnable
{
	private static KqmlIntegration instance;
	public static KqmlIntegration getInstance() throws Exception
	{
		if (instance == null)
		{
			instance = new KqmlIntegration();
		}
		return instance;
	}
	private Object lock;
	private boolean _quit;
	private ArrayList<JSONObject> listFifo;
	private String url;
	private long initialMemory;
	private long maxMemory;
	private long nTotal;
	private long initialTime;
	private KqmlIntegration() throws Exception
	{		
		this.lock = new Object();
		this.listFifo = new ArrayList<JSONObject>();
		this._quit = false;
		Runtime runtime = Runtime.getRuntime();
		this.initialMemory = runtime.totalMemory() - runtime.freeMemory();
		this.maxMemory = 0;
		this.nTotal = 0;
		this.initialTime = System.currentTimeMillis();
		this.url = PropertyUtil.getProperty("URL-KQML-INTEGRATION");
		Thread thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}
	
	public void addItem(KqmlItem item) throws Exception
	{
		JSONObject json = getJsonObject(item);
		synchronized (lock)
		{			
			++this.nTotal;
			this.listFifo.add(json);
			lock.notifyAll();
		}
	}
	public void quit()
	{
		synchronized (lock)
		{
			this._quit = true;
			lock.notifyAll();
		}
	}
	private JSONObject getJsonObject(KqmlItem item) throws Exception
	{
		JSONObject data = new JSONObject();
		JSONObject agenti = new JSONObject();
		JSONObject agentj = new JSONObject();
		agenti.put("agent_id", item.getAgent1().getId());
		Utils.getProperties(agenti, item.getAgent1(), item.getAgent1().kqmlProperties);

		agentj.put("agent_id", item.getAgent2().getId());
		Utils.getProperties(agentj, item.getAgent2(), item.getAgent2().kqmlProperties);
		
		data.put("model_id", item.getModel().getIdProcess());
		Utils.getProperties(data, item.getModel(), item.getModel().kqmlProperties);
		data.put("agenti", agenti);
		data.put("agentj", agentj);
		return data;
	}
	public void run()
	{
		try
		{
			while(!_quit)
			{
				JSONObject item = null;
				synchronized (lock)
				{
					if (listFifo.size() > 0)
					{
						item = listFifo.remove(0);
						Runtime runtime = Runtime.getRuntime();

						long totalMemory = runtime.totalMemory();   // memoria reservada por la JVM
						long freeMemory = runtime.freeMemory();     // memoria libre dentro de esa reserva
						long usedMemory = totalMemory - freeMemory - initialMemory; // memoria usada
						if (maxMemory < usedMemory)
						{
							maxMemory = usedMemory;
						}
						//System.out.println("memory:" + maxMemory + "**" + listFifo.size() + "**" + (item != null));
					}
					else
					{
						lock.wait();
					}
				}
				if (item != null)
				{
					processIntegration(item);
				}
			}
		} 
		catch (Exception e)
		{
			LogUtil.error(getClass(), e, "Error desconocido origen");
		}
	}
	private void processIntegration(JSONObject item) throws Exception
	{
		Utils.sendData(url, "POST", new Param("data", item), new Param("schema_id", "Social0_Simulations_11_JM"));
		synchronized (lock)
		{
			if (this.listFifo.size() == 0)
			{
				System.out.println("n:" + nTotal + ", MaxMemory:" + maxMemory + " bytes, time:" + (System.currentTimeMillis() - initialTime));
			}
		}
		//Utils.sendData(url, "POST", new Param("test", ""));
	}
	
}
