package cl.dlab.abm.core.kqml;

import java.util.ArrayList;

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
	private ArrayList<KqmlItem> listFifo;
	private String url;
	private KqmlIntegration() throws Exception
	{		
		this.lock = new Object();
		this.listFifo = new ArrayList<KqmlItem>();
		this._quit = false;
		this.url = PropertyUtil.getProperty("URL-KQML-INTEGRATION");
		Thread thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}
	
	public void addItem(KqmlItem item)
	{
		synchronized (lock)
		{
			this.listFifo.add(item);
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
	
	public void run()
	{
		try
		{
			while(!_quit)
			{
				KqmlItem item = null;
				synchronized (lock)
				{
					if (listFifo.size() > 0)
					{
						item = listFifo.remove(0);
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
	private void processIntegration(KqmlItem item) throws Exception
	{
		Utils.sendData(url, "POST", new Param("test", ""));
	}
	
}
