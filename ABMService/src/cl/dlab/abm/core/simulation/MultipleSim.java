package cl.dlab.abm.core.simulation;

import java.util.ArrayList;

import cl.dlab.abm.core.model.Data;
import cl.dlab.abm.core.model.Model;
import cl.dlab.util.LogUtil;

public class MultipleSim extends TaskManager implements Runnable
{
	private TaskManager taskManager;
	private int numSim;
	private int numSteps;
	private Model model;
	private ArrayList<Data> allData;
	public MultipleSim(TaskManager taskManager, Model model, int numSim, int numSteps)
	{
		super(numSim);
		this.model = model;
		this.numSim = numSim;
		this.taskManager = taskManager;
		this.numSteps = numSteps;
	}
	@Override
	public void run()
	{
		try
		{
			allData = new ArrayList<Data>();			
			for (int i = 0; i < numSim; i++)
			{
				if (!isActiveProcess(model.getIdProcess()))
				{
					return;
				}
				model.setNumSimulation(i);
				new ThreadController(new Sim(this, (Model)model.clone(), this.numSteps)).start();
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			System.out.println("<MultipleSim> Error no controlado:" + e.getMessage());
			LogUtil.error(getClass(), e, "Error al ejecutar thread");
			System.exit(0);
		}
		waitForEndAllTask();
		try
		{
			taskManager.endTask(model, allData);
		}
		catch(Exception e)
		{
			LogUtil.error(getClass(), e, "Error al retornar data");
			System.exit(0);			
		}
		
	}
	@Override
	protected void addData(Object... data)
	{
		this.allData.add((Data)data[0]);
	}

}
