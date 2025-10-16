package cl.dlab.abm.core.simulation;

import cl.dlab.util.LogUtil;
import cl.dlab.util.PropertyUtil;

public class ThreadController extends Thread
{
	public static final int NUM_MAX_THREAD = PropertyUtil.getIntValueSinErrores("num-max-threads");

	private static int numThread = 0;
	private static Object lock = new Object();
	

	public ThreadController(Runnable runnable)
	{
		super(runnable);
		synchronized (lock)
		{
			ThreadController.numThread++;
		}
	}
	

	@Override
	public synchronized void start()
	{
		synchronized (lock)
		{
			if (ThreadController.numThread >= NUM_MAX_THREAD)
			{
				try
				{
					lock.wait();
				} catch (Throwable e)
				{
					LogUtil.error(getClass(), e, "Error al tratar de hacer wait");
				}
			}
		}
		super.start();
	}

	public void endTask()
	{
		synchronized (lock)
		{
			ThreadController.numThread--;
			lock.notify();
		}
	}
}
