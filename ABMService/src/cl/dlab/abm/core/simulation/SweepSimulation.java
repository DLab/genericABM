package cl.dlab.abm.core.simulation;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import cl.dlab.abm.core.model.Agent;
import cl.dlab.abm.core.model.Data;
import cl.dlab.abm.core.model.Model;
import cl.dlab.abm.core.model.gen.JavaClassGenerator;
import cl.dlab.abm.core.parser.Util;
import cl.dlab.abm.servlet.InitializeServlet;
import cl.dlab.util.LogUtil;
import cl.dlab.util.PropertyUtil;

public class SweepSimulation extends TaskManager
{
	private static final String[] SWEEP = new String[] {"Start", "Step", "End"};
	@SuppressWarnings("unused")
	private static final int MAX_SIZE_SIMULATIONS = PropertyUtil.getIntValueSinErrores("MAX_SIZE_SIMULATIONS");
	
	private ArrayList<Model> models;
	private int numActiveThread;
	private String idProcess;
	private ResultUtil resultUtil;
	public SweepSimulation()
	{
		super(0);
	}
	private double getDouble(Object o)
	{
		return o instanceof Number ? ((Number)o).doubleValue() : Double.parseDouble(((String)o).replaceAll("[.]", "").replace(',', '.'));
	}
	private Integer getInteger(Object o)
	{
		return o == null ? null : o instanceof Number ? ((Number)o).intValue() : Integer.parseInt(((String)o).replaceAll("[.]", ""));
	}
	
	private Double[] getDoubleArray(HashMap<String, Object> params, String key)
	{
		Double[] arr = new Double[3];
		for (int i = 0; i < SWEEP.length; i++)
		{			
			Object o = params.get(key +  SWEEP[i]);
			arr[i] = getDouble(o);
		}
		ArrayList<Double> results = new ArrayList<Double>();
		double val = arr[0];
		double step = arr[1];
		double end = arr[2];
		while(val <= end)
		{
			results.add(Math.round(val * 1000000.0) / 1000000.0);
			val += step;
		}
		return results.toArray(new Double[results.size()]);
	}
	
	private Integer[] getIntArray(HashMap<String, Object> params, String key)
	{
		int[] arr = new int[3];
		for (int i = 0; i < SWEEP.length; i++)
		{
			Object o = params.get(key +  SWEEP[i]);
			arr[i] = getInteger(o);	
		}
		ArrayList<Integer> results = new ArrayList<Integer>();
		int val = arr[0];
		int step = arr[1];
		int end = arr[2];
		while(val <= end)
		{
			results.add(val);
			val += step;
		}
		return results.toArray(new Integer[results.size()]);
		
	}	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String, Object> execute(HashMap<String, Object> params) throws Exception
	{
		HashMap<String, Object> modelValues = (HashMap<String, Object>)params.get("modelSelected");
		ArrayList<HashMap<String, Object>> variables = (ArrayList<HashMap<String,Object>>)modelValues.get("variables");
		ArrayList<PropertyValue<?>> rangesValues = new ArrayList<PropertyValue<?>>();
		String zAxisName = null;
		for (HashMap<String, Object> prop : variables)
		{
			Boolean rangeValue = (Boolean)prop.get("rangeValues");
			
			if (rangeValue) 
			{
				int type = (Integer)prop.get("variableTypeId");
				String _propertyName = (String)prop.get("name");
				String propertyName = Util.getPropertyName(_propertyName);
				if (type == 1)
				{
					rangesValues.add(new PropertyValue<Integer>(propertyName, getIntArray(params, _propertyName)));
				}
				else if (type == 2 || type == 5)
				{
					rangesValues.add(new PropertyValue<Double>(propertyName, getDoubleArray(params, _propertyName)));
				}
			}
			String generalAnalysisId = (String)prop.get("generalAnalysisId");
			if (generalAnalysisId != null && generalAnalysisId.equals("Z"))
			{
				zAxisName = Util.getPropertyName((String)prop.get("name"));
			}
		}
		//System.out.println("zname:" + zAxisName);
		idProcess = (String)params.get("idProcess");
		ArrayList<HashMap<String, Object>> allValues = new PropertyValueObject(rangesValues).getAllValues();
		String _modelName = (String) modelValues.get("name");
		String modelName = Util.getJavaName(_modelName);
		boolean singleAgentList = (Boolean)modelValues.get("singleAgentsList");
		Boolean value = (Boolean)modelValues.get("kqmlIntegration");
		boolean kqmlIntegration = value == null ? false : value;
		HashMap<String, Object> numAgents = (HashMap<String, Object>)params.get("numAgents");
		Integer numSim = getInteger(params.get("numSim"));
		Integer numSteps = getInteger(params.get("numSteps"));
		double probMeet = getDouble(params.get("probMeet"));
		String packageName = JavaClassGenerator.getPackageName(modelName);
		String modelClassName = packageName + ".Model";
		String resultUtilClassName = packageName + ".ResultUtil";
		
		File jar = new File(InitializeServlet.REAL_PATH + "/jars/" + modelName + ".jar");
		
		URLClassLoader classLoader = new URLClassLoader(new URL[]{jar.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
		Class<? extends Model> modelClass = (Class<? extends Model>)classLoader.loadClass(modelClassName);

		HashMap<String, Class<? extends Agent>> agentClass = new HashMap<String, Class<? extends Agent>>();
		for (String name : numAgents.keySet())
		{
			String agentClassName = packageName + "." + Util.getJavaName(name);
			agentClass.put(name, (Class<? extends Agent>)classLoader.loadClass(agentClassName));
		}
		
		Class<? extends ResultUtil> resultUtilClass = (Class<? extends ResultUtil>)classLoader.loadClass(resultUtilClassName);
		models = new ArrayList<Model>();
		long t = System.currentTimeMillis();
		double totalAgents = 0;
		int n = 0;
		System.out.println("kqmlIntegration:" + kqmlIntegration);
		for (HashMap<String, Object> values : allValues)
		{
			Model model = modelClass.getConstructor().newInstance();
			model.setIdProcess(idProcess);
			model.setProbMeet(probMeet);
			model.setName(_modelName);
			model.setValues(values);
			model.setKqmlIntegration(kqmlIntegration);
			for (String name : numAgents.keySet())
			{
				n = getInteger(numAgents.get(name));
				if (n % 2 == 1)
				{
					n++;
				}
				totalAgents += n;
				model.population(n, name, agentClass.get(name));				
			}
			if (singleAgentList)
			{
				ArrayList<Agent> agents = new ArrayList<Agent>(); 
				for (String name : numAgents.keySet())
				{
					agents.addAll(model.getAgents(name));				
				}
				Collections.shuffle(agents);
				model.setAgents("AGENTS", agents);
			}
			//System.out.println("init:" + model.getAgents("AGENTS").size() + "**" + model.getAgents("Depredador").size() + "**" + model.getAgents("Presa").size());
			models.add(model);
			model.setNumCombination(models.size());
		}
		if (totalAgents == 0)
		{
			totalAgents = models.size();
		}
		System.out.println("Total de combinaciones generadas..:" + allValues.size() + ", numsim:" + numSim + ", numAgents:" + n);
		//long total = allValues.size() * numSim * n;
		boolean distribute = false;//total > MAX_SIZE_SIMULATIONS;
		if (distribute)
		{
			int numNodes = System.getProperty("CLUSTER-NODES").split(";").length + 1;
			if (numNodes > 1)
			{
				System.out.println("Se distribuye tareas en " + numNodes);
				ArrayList[] nodeModels = new ArrayList[numNodes];
				for (int i = 0; i < numNodes; i++)
				{
					nodeModels[i] = new ArrayList<Model>();				
				}
				int index = 0;
				for (int i = 0; i < models.size(); i++)
				{
					nodeModels[index].add(models.get(i));
					if (++index == numNodes)
					{
						index = 0;
					}
				}
				models = nodeModels[0];
			}
		}
		resultUtil = resultUtilClass.getConstructor().newInstance();
		resultUtil.idProcess = idProcess;
		classLoader.close();
		System.out.println("Tiempo en crear " + models.size() + " modelos en:" + (System.currentTimeMillis() - t));
		setNumTask(models.size());
		t = System.currentTimeMillis();
		
		initActiveProcess(idProcess, models.size());
		int numMaxThread = ThreadController.NUM_MAX_THREAD / numSim;
		if (numMaxThread == 0)
		{
			numMaxThread = 1;
		}
		this.numActiveThread = 0;
		try 
		{
			for (Model model : models)
			{
				boolean activeProcess = true;
				while(true)
				{
					if (!(activeProcess = isActiveProcess(idProcess)))
					{
						activeProcess = false;
						break;
					}
		
					if (numActiveThread >= numMaxThread)
					{
						synchronized (this.resultUtil)
						{
							this.resultUtil.wait();
						}
					}
					else
					{
						break;
					}
				}
				if (!activeProcess)
				{
					break;
				}
				++numActiveThread;
				//System.out.println("start:" + idProcess + ", " + numActiveThread + "/" + numMaxThread + " of " + models.size());
				new ThreadController(new MultipleSim(this, model, numSim, numSteps)).start();
			}
			if (isCanceledProcess(idProcess))
			{
				System.out.println("Proceso cancelado:" + idProcess);
				HashMap<String, Object> hs = new HashMap<String, Object>();
				hs.put("idProcess", idProcess);
				hs.put("cancel", true);
				return hs;
			}
			System.out.println("termina ok, espera terminen tareas");
			super.waitForEndAllTask();
			long simulationTime = System.currentTimeMillis() - t;
			System.out.println("Simula en :" + simulationTime);
			t = System.currentTimeMillis();
			try
			{
				return getResults(zAxisName, resultUtil.results, numSteps, simulationTime, totalAgents * (double)numSim * (double)numSteps);
			}
			finally
			{
				saveFileProcess(idProcess);
				System.out.println("Estadisticas en:" + (System.currentTimeMillis() - t));
			}
		}
		catch (Throwable e) {
			System.out.println("No debería ocurrir:" + e.getMessage());
			LogUtil.error(getClass(), e, "No debería ocurrir");
			return null;
		}
	}
	public static void main(String[] args)
	{
		long simulationTime = 54005;
		double d = (double)simulationTime / ((double)14641 * (double)100* (double)7 * (double)400);
		System.out.println(d);
	}
	private HashMap<String, Object> getResults(String zAxisName, ArrayList<HashMap<String, Object>> allData, int numSteps, double simulationTime, double total)
	{
		double t = System.currentTimeMillis(); 

        HashMap<String, Object> results = new HashMap<String, Object>();
        //results.put("allData", allData);
        //results.put("lastData", resultUtil.lastData);
        results.put("results", resultUtil.results);
        results.put("meanSimulationTime", (simulationTime + (double)System.currentTimeMillis() - t) / total);
		
		return results;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void addData(Object... data)
	{
		try
		{
			this.resultUtil.meanAndStd((Model)data[0], (ArrayList<Data>)data[1]);
			//this.allResults.put(data[0], (ArrayList<Data>)data[1]);
			updateProcess(idProcess, this.models.size(), this.resultUtil.totalProcessed);
			if (this.resultUtil.totalProcessed % 100 == 0) 
			{
				System.out.println(new Date() + "Terminando comb(v2):" + this.resultUtil.totalProcessed);
			}
			synchronized (this.resultUtil)
			{
				this.numActiveThread--;
				this.resultUtil.notifyAll();
			}
		}
		catch(Throwable e)
		{
			LogUtil.error(getClass(), e, "Process error");
			try
			{
				cancelProcess(idProcess);
			}
			catch(Exception ex)
			{
				LogUtil.error(getClass(), e, "Update Process error");
			}
			
		}
	}

}
