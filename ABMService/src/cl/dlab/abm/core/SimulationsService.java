package cl.dlab.abm.core;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import cl.dlab.abm.core.simulation.SweepSimulation;
import cl.dlab.abm.core.simulation.TaskItem;
import cl.dlab.abm.core.simulation.TaskManager;
import cl.dlab.abm.core.sql.rad.AnalysisResultsContent;
import cl.dlab.abm.core.sql.rad.Simulations;
import cl.dlab.abm.core.sql.rad.SimulationsContent;
import cl.dlab.abm.service.vo.InputVO;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.SimulationsOutputVO;
import cl.dlab.abm.util.BytesUtil;
import cl.dlab.util.LogUtil;

public class SimulationsService extends BaseService
{
	public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	public SimulationsService()
	{
		super();
	}

	public SimulationsService(Connection con)
	{
		super(con);
	}

	public RespuestaVO<SimulationsOutputVO> consultar(InputVO input) throws Exception
	{
		return new Simulations(con, con == null).consultar(input);
	}
	
	@SuppressWarnings({ "unchecked"})
	public ArrayList<HashMap<String, Object>> consultar(java.util.HashMap<String, Object> input) throws Exception
	{
		Simulations simulations =  new Simulations(con, false);
		PreparedStatement stmt = simulations.getConnection().prepareStatement("update simulations set id_simulation_state = ? where id = ?");
		try
		{			
			ArrayList<HashMap<String, Object>> result = (ArrayList<HashMap<String, Object>>)simulations.consultar(input).get("listData");
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
			
			String userName = (String)((HashMap<String, Object>)((HashMap<String, Object>)input.get("datosUsuario")).get("user")).get("id");
			boolean update = false;
			for (HashMap<String, Object> hs : result)
			{
				if ((Boolean)hs.get("private") && !hs.get("userId").equals(userName))
				{
					continue;
				}
				int idSimulationState = (Integer)hs.get("idSimulationState");
				if (idSimulationState == 1)
				{
					TaskItem taskItem = TaskManager.getActiveProcess((String)hs.get("id"));
					if (taskItem == null)
					{
						update = true;
						stmt.setInt(1, 4);
						stmt.setString(2, (String)hs.get("id"));
						stmt.executeUpdate();
						hs.put("idSimulationState", 4);
					}
					else
					{
						double time = taskItem.getLastUpdate().getTime() - taskItem.getStartTime().getTime(); 
						hs.put("progress", taskItem.getNumCombinations() == 0 ? 0 : (double)taskItem.getExecutedCombinations() / (double)taskItem.getNumCombinations());
						hs.put("remainingTime", taskItem.getExecutedCombinations() == 0 ? 0 : (time * (double)taskItem.getNumCombinations() / taskItem.getExecutedCombinations()) - time);
					}
				}
				list.add(hs);
			}
			if (update)
			{
				simulations.getConnection().commit();
			}
			return list;
		}
		finally
		{
			stmt.close();
			simulations.getConnection().close();
		}
		
	}
	public HashMap<String, Object> getProgressSimulation(HashMap<String, Object> params) throws Exception
	{
		TaskItem taskItem = TaskManager.getActiveProcess((String)params.get("id"));
		if (taskItem == null)
		{
			params.remove("progress");
			params.remove("remainingTime");
			params.put("idSimulationState", 2);
		}
		else
		{
			double time = taskItem.getLastUpdate().getTime() - taskItem.getStartTime().getTime(); 
			params.put("progress", taskItem.getNumCombinations() == 0 ? 0 : (double)taskItem.getExecutedCombinations() / (double)taskItem.getNumCombinations());
			params.put("remainingTime", taskItem.getExecutedCombinations() == 0 ? 0 : (time * (double)taskItem.getNumCombinations() / taskItem.getExecutedCombinations()) - time);
			params.put("idSimulationState", taskItem.getStateId());
		}
		return params;
	}

	public void eliminar(java.util.HashMap<String, Object> input) throws Exception
	{
		new Simulations(con, true).eliminar(input);
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> guardar(final java.util.HashMap<String, Object> input) throws Exception
	{
		Boolean isNew = (Boolean)input.get("isNew");
		if (isNew == null)
		{
			input.put("isNew", isNew = true);
		}
		Simulations simulation = new Simulations(con, false);
		try
		{
			SimulationsContent content = new SimulationsContent(simulation.getConnection(), false);
			if (isNew)
			{
				input.put("id", UUID.randomUUID().toString());
			}
			else
			{
				AnalysisResultsContent analysisResults = new AnalysisResultsContent(simulation.getConnection(), false);
				analysisResults.delete((String)input.get("id"));
			}
			
			simulation.guardar(input);			
	        
	        input.put("isNew", false);
	        input.put("content", BytesUtil.getGZipBytes(input));
			
			content.guardar(input);
			content.getConnection().commit();
			
			if (isNew) {
				input.put("query_id", "byId");
				ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>)simulation.consultar(input).get("listData");
				return list.get(0);
			}
			return input;
		}
		finally
		{
			simulation.getConnection().close();
		}
		
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> details(HashMap<String, Object> input) throws Exception
	{	
		ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>)new SimulationsContent().consultar(input).get("listData");
		HashMap<String, Object> obj = list.get(0);
		byte[] b = (byte[])obj.get("content");
        try(ObjectInputStream oi = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(b))))
        {
        	HashMap<String, Object> hs = (HashMap<String, Object>)oi.readObject();
        	input.put("idSimulation", input.get("id"));
        	hs.put("analysisResults", new AnalysisResultsService().consultar(input).get("listData"));
        	return hs;
        }
	}	
	public HashMap<String, Object> cancelSimulation(HashMap<String, Object> params) throws Exception
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		String idProcess = (String)params.get("id");
		if (!TaskManager.isActiveProcess(idProcess))
		{
			System.out.println("proceso no activo:" + idProcess);
			result.put("status", false);
			result.put("msg", "process_is_not_active");
		}
		else
		{
			System.out.println("proceso activo!!");
			TaskManager.cancelProcess(idProcess);
			result.put("status", true);
			result.put("msg", "process_canceled_successfully");
		}
		return result;
	}
	private void cancelProcess(HashMap<String, Object> params) throws Exception
	{
		Simulations simulation = new Simulations(con, false);
		try
		{
			String idProcess = (String)params.get("idProcess");
			PreparedStatement stmt = simulation.getConnection().prepareStatement("update simulations set id_simulation_state = ? where id = ?");
			stmt.setInt(1, 3);
			stmt.setString(2, idProcess);
			stmt.executeUpdate();
			simulation.getConnection().commit();
			TaskManager.processCanceledSuccessfully(idProcess);
		}
		finally
		{
			simulation.getConnection().close();
		}
		
	}
	private void deleteSimulationDetails(String id) throws Exception
	{
		Simulations service = new Simulations(con, false);
		try(PreparedStatement stmt = service.getConnection().prepareStatement("delete from simulation_details where id_simulation = ?"))
		{
			stmt.setString(1, id);
			stmt.executeUpdate();
			service.getConnection().commit();
		}
		finally
		{
			service.getConnection().close();
		}
		
	}
	public HashMap<String, Object> execute(HashMap<String, Object> params) throws Exception
	{
		boolean async = (Boolean)params.get("background"); 
		params.put("idSimulationState", 1);
		params.put("async", async);
		HashMap<String, Object> result = guardar(params);
		params.put("idProcess", params.get("id"));
		params.put("idSimulationState", 2);
		TaskManager.addActiveProcess((String)params.get("id"));
				
		deleteSimulationDetails((String)params.get("id"));
		

		if (async)
		{
			new Thread(new Runnable()
			{
				
				@Override
				public void run()
				{
					try
					{
						_execute(params);
					}					
					catch(Exception e)
					{
						LogUtil.error(getClass(), e, "Existen problemas al crear archivo");
					}
				}
			}).start();			
			return result;
		}
		return _execute(params);
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Object> _execute(HashMap<String, Object> params) throws Exception
	{
		final HashMap<String, Object> result = new SweepSimulation().execute(params);
		Boolean cancel = (Boolean)result.get("cancel");
		if (cancel != null && cancel)
		{
			cancelProcess(params);
			return result;
		}
		double meanSimulationTime = (Double)result.get("meanSimulationTime");
		HashMap<String, Object> modelValues = (HashMap<String, Object>)params.get("modelSelected");
		new ModelService().updateMeanSimulationTime((String)modelValues.get("name"), meanSimulationTime);
		HashMap<String, Object> hs = new HashMap<String, Object>();
		params.put("isNew", false);
		//params.put("lastData", result.get("lastData"));
		params.put("details", result.get("results"));
		guardar(params);
		TaskManager.removeActiveProcess((String)params.get("idProcess"));
		hs.put("numAgents", params.get("numAgents"));
		hs.put("results", result.get("results"));
		hs.put("idProcess", params.get("idProcess"));
		return hs;
	}
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> getData2D(HashMap<String, Object> input) throws Exception
	{
		Simulations service = new Simulations(null, false);
		try
		{
			try(PreparedStatement stmt = service.getConnection().prepareStatement("select additional_data from simulation_details where id_simulation = ?"))
			{
				stmt.setString(1, (String)input.get("id"));
				try(ResultSet rset = stmt.executeQuery())
				{
					ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
					while (rset.next())
					{
						ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream((byte[])rset.getObject(1)));
						result.add((HashMap<String,Object>)BytesUtil.getObjectByZipBytes((byte[])oi.readObject()));
					}
					System.out.println("data:" + result.get(0).keySet());
					return result;
				}
			}
			
		}
		finally
		{
			service.getConnection().close();
		}
	}
	
}