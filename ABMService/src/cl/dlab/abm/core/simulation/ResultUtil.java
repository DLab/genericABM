package cl.dlab.abm.core.simulation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cl.dlab.abm.core.SimulationDetailsService;
import cl.dlab.abm.core.model.Data;
import cl.dlab.abm.core.model.Model;
import cl.dlab.abm.servlet.InitializeServlet;
import cl.dlab.util.PropertyUtil;

public class ResultUtil
{
	protected ArrayList<HashMap<String, Object>> results;
	protected int totalProcessed;
	protected String idProcess;
	public ResultUtil()
	{
		this.results = new ArrayList<HashMap<String,Object>>();
	}
	public static double mean(double[] data)
	{
		double sum = 0;
		for (double d : data)
		{
			sum += d;
		}
		return sum / data.length;
	}
	public static double median(double[] data)
	{
		Arrays.sort(data);
		int n = data.length;
		return n % 2 != 0 ? (double)data[n/2] : (double)(data[(n-1)/2] + data[n/2])/2.0;  
	}
	public static double std(double[] data, double mean)
	{
		double sum = 0;
		for (double d : data)
		{
			sum += Math.pow(d - mean, 2);
		}
		return Math.sqrt(sum / data.length);
	}
	protected void addStatistics(Data _mean, Data _stdDev, Data _median, ArrayList<Data> allData, int numSim, int step)
	{
		
	}
	protected void addStatistics(Data _data, ArrayList<Data> _allData, int numStep)
	{
		
	}
	public synchronized void meanAndStd(Model model, ArrayList<Data> data) throws Exception
	{
		System.out.println("Alldata:" + data.size());
		if (PropertyUtil.getProperty("save-all-data").equals("true"))
		{
			File file = new File(InitializeServlet.REAL_PATH + "/sim/sim_" + idProcess + "_" + totalProcessed + ".out");
			ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file));
			ArrayList<HashMap<String, double[]>> propData = new ArrayList<HashMap<String, double[]>>();
			for (Data d : data)
			{
				propData.add(d.getProperties());
			}
			oo.writeObject(new Object[] {model.getProperties(), propData});
			oo.close();
		}
		int numStep = data.get(0).getNumPasos();
		Constructor<? extends Data> constructor = data.get(0).getClass().getConstructor(int.class);
		int numSim = data.size();
		
		Data meanData = constructor.newInstance(numStep);
		Data stdDevData = constructor.newInstance(numStep);
		Data median = constructor.newInstance(numStep);
		for (int j = 0; j < numStep; j++)
		{
			addStatistics(meanData, stdDevData, median, data, numSim, j);
		}
		Data lastData = constructor.newInstance(numSim);
		addStatistics(lastData, data, numStep - 1);
		processData(getHashValue(model, meanData, stdDevData, median, lastData), numStep);
		totalProcessed++;		
	}
	@SuppressWarnings("unchecked")
	private void processData(HashMap<String, Object> data, int numSteps) throws Exception
	{
		HashMap<String, Object> mean = (HashMap<String, Object>)data.get("mean");
		HashMap<String, Object> stdDev = (HashMap<String, Object>)data.get("stdDev");
		HashMap<String, Object> median = (HashMap<String, Object>)data.get("median");
		HashMap<String, Object> model = (HashMap<String, Object>)data.get("model");
		HashMap<String, Object> hsLastData = (HashMap<String, Object>)data.get("lastData");
		hsLastData.put("model", model);
		//lastData.add(hsLastData);
		model.put("numSteps", numSteps);

		HashMap<String, Object> hs = new HashMap<String, Object>(model);
		data.put("index", totalProcessed);
		hs.put("index", totalProcessed);
			
		for (String keyName : mean.keySet())
		{
			if (hs.get(keyName) != null)
			{
				continue;
			}
			double[] zmean = (double[])mean.get(keyName);
			double[] zstd = (double[])stdDev.get(keyName);
			double[] zmedian = (double[])median.get(keyName);
			
			//System.out.println("zmean:" + zmean + "**" + keyName + "**" + mean.keySet());
			if (zmean != null)
			{
				HashMap<String, Double> hsAxis = new HashMap<String, Double>();
				hsAxis.put("mean", zmean[zmean.length -1]);
				hsAxis.put("std", zstd[zstd.length -1]);
				hsAxis.put("median", zmedian[zmedian.length -1]);
				hs.put(keyName, hsAxis);
			}
			
		}
		this.results.add(hs);
		hs.put("id", idProcess);
		data.put("id", idProcess);
		new SimulationDetailsService().guardarAsync(data);
	}
	private static HashMap<String, Object> getHashValue(Model model, Data mean, Data stdDev, Data median, Data lastData)
	{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		hs.put("model", model.getProperties());
		hs.put("mean", mean.getProperties());
		hs.put("stdDev", stdDev.getProperties());
		hs.put("median", median.getProperties());
		hs.put("lastData", lastData.getLastProperties());
		return hs;
	}
}
