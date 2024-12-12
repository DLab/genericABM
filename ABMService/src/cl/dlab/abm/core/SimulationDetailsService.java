package cl.dlab.abm.core;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import cl.dlab.abm.core.sql.rad.SimulationDetails;
import cl.dlab.abm.service.vo.InputVO;
import cl.dlab.abm.service.vo.RespuestaVO;
import cl.dlab.abm.service.vo.SimulationDetailsOutputVO;
import cl.dlab.abm.util.BytesUtil;
import cl.dlab.util.LogUtil;

public class SimulationDetailsService extends BaseService
{

	private static ArrayList<String> ID_PENDING_LIST = new ArrayList<String>(); 
	public SimulationDetailsService()
	{
		super();
	}

	public SimulationDetailsService(Connection con)
	{
		super(con);
	}

	public RespuestaVO<SimulationDetailsOutputVO> consultar(InputVO input) throws Exception
	{
		return new SimulationDetails(con, con == null).consultar(input);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> consultar(java.util.HashMap<String, Object> input) throws Exception
	{
		String simulationId = (String)input.get("idSimulation");
		synchronized (ID_PENDING_LIST)
		{
			if (ID_PENDING_LIST.contains(simulationId))
			{
				ID_PENDING_LIST.wait();
			}
		}
		
		ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>)new SimulationDetails(con, true).consultar(input).get("listData");
		HashMap<String, Object> item = list.get(0);
        ObjectInputStream oi = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream((byte[])item.get("content"))));
        return (HashMap<String, Object>)oi.readObject();
	}
	public void eliminar(java.util.HashMap<String, Object> input) throws Exception
	{
		new SimulationDetails(con, true).eliminar(input);
	}

	@SuppressWarnings("unchecked")
	public void guardar(java.util.HashMap<String, Object> input) throws Exception 
	{
		//long t = System.currentTimeMillis();
		SimulationDetails detailService = new SimulationDetails(con, false);
		try
		{
			String id = (String)input.get("id");
			HashMap<String, Object> lastData = (HashMap<String, Object>)input.remove("lastData");
			HashMap<String, Object> hsDetail = new HashMap<String, Object>();
			hsDetail.put("idSimulation", id);
			hsDetail.put("id", input.get("index"));
			hsDetail.put("isNew", true);
			hsDetail.put("additionalData", BytesUtil.getGZipBytes(lastData));
			
			hsDetail.put("content", BytesUtil.getGZipBytes(input));
			detailService.guardar(hsDetail);				
			detailService.getConnection().commit();
		}
		finally
		{
			detailService.getConnection().close();
			//LogUtil.info(getClass(), "Termina de guardar detalle en:", (System.currentTimeMillis() - t));
		}
	}

	public void guardarAsync(java.util.HashMap<String, Object> input) throws Exception 
	{
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					String simulationId = (String)input.get("id");
					synchronized (ID_PENDING_LIST)
					{
						ID_PENDING_LIST.add(simulationId);
					}
					guardar(input);
					synchronized (ID_PENDING_LIST)
					{
						ID_PENDING_LIST.remove(simulationId);
						ID_PENDING_LIST.notifyAll();
					}
				}
				catch(Exception e)
				{
					LogUtil.error(getClass(), e, "Error al tratar de guardar detalle");
				}
				
				
			}
		}).start();
	}
}