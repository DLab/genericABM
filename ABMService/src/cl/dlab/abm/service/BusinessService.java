package cl.dlab.abm.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import cl.dlab.abm.core.AccionService;
import cl.dlab.abm.core.AccionesxFuncionRolService;
import cl.dlab.abm.core.AnalysisMethodsService;
import cl.dlab.abm.core.AnalysisResultsService;
import cl.dlab.abm.core.FuncionService;
import cl.dlab.abm.core.FunctionTypeService;
import cl.dlab.abm.core.GeneralAnalysisTypeService;
import cl.dlab.abm.core.GraphAlgorithmService;
import cl.dlab.abm.core.GraphTopologyService;
import cl.dlab.abm.core.GraphTypeService;
import cl.dlab.abm.core.ModelService;
import cl.dlab.abm.core.RolService;
import cl.dlab.abm.core.SimulationDetailsService;
import cl.dlab.abm.core.SimulationStateService;
import cl.dlab.abm.core.SimulationsService;
import cl.dlab.abm.core.TableDefinitionService;
import cl.dlab.abm.core.UsuarioService;
import cl.dlab.abm.core.VariableTypeService;
import cl.dlab.abm.core.model.gen.JavaClassGenerator;
import cl.dlab.abm.core.sql.rad.AnalysisResultsContent;
import cl.dlab.abm.util.Param;
import cl.dlab.abm.util.ReportesUtil;
import cl.dlab.abm.util.Utils;
import cl.dlab.util.LogUtil;
import cl.dlab.util.PropertyUtil;

public class BusinessService
{
	//TIEMOP PROMEDIO ESTIMADO PARA CALCULAR EL TIEMPO DE CALCULO DE ANALISIS
	private static final double ANALYSIS_RESULTS_AVG_TIME = 1.585;
	
	public HashMap<Integer, HashMap<Integer, HashMap<String, Object>>> obtenerAccionesxFuncionRol(HashMap<String, Object> input) throws Exception
	{
		return new AccionesxFuncionRolService().consultar(input);
	}
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> obtenerAcciones(HashMap<String, Object> input) throws Exception
	{
		return (ArrayList<HashMap<String, Object>>)new AccionService().consultar(input).get("listData");
	}
	public HashMap<String, Object> consultarRoles(HashMap<String, Object> input) throws Exception
	{
		return new RolService().consultar(input);
	}
	public HashMap<String, Object> guardarRol(HashMap<String, Object> input) throws Exception
	{
		return new RolService().guardar(input);
	}
	public void eliminarRol(HashMap<String, Object> input) throws Exception
	{
		new RolService().eliminar(input);
	}
	public HashMap<String, Object> consultaDetalleRol(HashMap<String, Object> input) throws Exception
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("accionesxFuncion", new AccionesxFuncionRolService().consultar(input));
		System.out.println("result:" + result);
		//POR PVG
		result.put("tiposDeProductos", new HashMap<>());// new TipoProductoRolService().consultar(input));
		return result;
	}
	public ArrayList<HashMap<String, Object>> consultaFunciones() throws Exception
	{
		return new FuncionService().consultar(new HashMap<String, Object>());
	}

	public HashMap<String, Object> consultarUsuarios(HashMap<String, Object> input) throws Exception
	{
		return new UsuarioService().consultar(input);
	}
	public void guardarUsuario(HashMap<String, Object> input) throws Exception
	{
		new UsuarioService().guardar(input);
	}
	public void eliminarUsuario(HashMap<String, Object> input) throws Exception
	{
		new UsuarioService().eliminar(input);
	}
	
	public HashMap<String, Object> validaUsuario(HashMap<String, Object> input) throws Exception
	{
		return new UsuarioService().validaUsuario(input);
	}
	public HashMap<String, Object> changePassword(HashMap<String, Object> input) throws Exception
	{
		return new UsuarioService().changePassword(input);
	}
	public HashMap<String, Object> saveUserConfiguration(HashMap<String, Object> input) throws Exception
	{	
        return new UsuarioService().saveUserConfiguration(input);
		
	}

	
	public ArrayList<HashMap<String, Object>> tableTypeQuery() throws Exception
	{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		hs.put("parentId", null);
		return tableQuery(hs);
	}
	public ArrayList<HashMap<String, Object>> allTablesQuery() throws Exception
	{
		return new TableDefinitionService().allTablesQuery();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> tableQuery(HashMap<String, Object> input) throws Exception
	{
		return (ArrayList<HashMap<String, Object>>)new TableDefinitionService().consultar(input).get("listData");
	}
	public HashMap<String, Object> tableDelete(HashMap<String, Object> input) throws Exception
	{
		new TableDefinitionService().eliminar(input);
		return input;
	}
	public HashMap<String, Object> tableSave(HashMap<String, Object> input) throws Exception
	{
		new TableDefinitionService().guardar(input);
		return input;
	}
	public HashMap<String, Object> tableDetailQuery(HashMap<String, Object> input) throws Exception
	{
		return input;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> functionTypeQuery(HashMap<String, Object> input) throws Exception
	{
		return (ArrayList<HashMap<String, Object>>)new FunctionTypeService().consultar(input).get("listData");
	}
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> variableTypeQuery(HashMap<String, Object> input) throws Exception
	{
		return (ArrayList<HashMap<String, Object>>)new VariableTypeService().consultar(input).get("listData");
	}
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> graphTypeQuery(HashMap<String, Object> input) throws Exception
	{
		return (ArrayList<HashMap<String, Object>>)new GraphTypeService().consultar(input).get("listData");
	}
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> graphTopologysQuery(HashMap<String, Object> input) throws Exception
	{
		return (ArrayList<HashMap<String, Object>>)new GraphTopologyService().consultar(input).get("listData");
	}
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> simulationStateQuery(HashMap<String, Object> input) throws Exception
	{
		return (ArrayList<HashMap<String, Object>>)new SimulationStateService().consultar(input).get("listData");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> analysisMethodsQuery(HashMap<String, Object> input) throws Exception
	{
		return (ArrayList<HashMap<String, Object>>)new AnalysisMethodsService().consultar(input).get("listData");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> graphAlgorithmsQuery(HashMap<String, Object> input) throws Exception
	{
		return (ArrayList<HashMap<String, Object>>)new GraphAlgorithmService().consultar(input).get("listData");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> generalAnalysisTypeQuery(HashMap<String, Object> input) throws Exception
	{
		return (ArrayList<HashMap<String, Object>>)new GeneralAnalysisTypeService().consultar(input).get("listData");
	}

	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> modelQuery(HashMap<String, Object> input) throws Exception
	{
		ArrayList<HashMap<String, Object>> result = (ArrayList<HashMap<String, Object>>)new ModelService().consultar(input).get("listData");
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		
		String userName = (String)((HashMap<String, Object>)((HashMap<String, Object>)input.get("datosUsuario")).get("user")).get("id");
		for (HashMap<String, Object> hs : result)
		{
			if ((Boolean)hs.get("private") && !hs.get("userId").equals(userName))
			{
				continue;
			}
			list.add(hs);
		}
		return list;
	}
	public HashMap<String, Object> modelDelete(HashMap<String, Object> input) throws Exception
	{
		new ModelService().eliminar(input);
		return input;
	}
	public HashMap<String, Object> modelSave(HashMap<String, Object> input) throws Exception
	{
		new ModelService().guardar(input);
		return input;
	}
	public HashMap<String, Object> modelDetailQuery(HashMap<String, Object> input) throws Exception
	{
		new ModelService().detailQuery(input);
		return input;
	}
	public byte[] downloadSourceFile(HashMap<String, Object> input) throws Exception
	{
		return JavaClassGenerator.downloadSourceFile(input);
	}
	public ArrayList<HashMap<String, Object>> simulationsQuery(HashMap<String, Object> input) throws Exception
	{
		return new SimulationsService().consultar(input);
	}
	public void simulationDelete(HashMap<String, Object> input) throws Exception
	{
		new SimulationsService().eliminar(input);
	}
	public HashMap<String, Object> simulationSave(HashMap<String, Object> input) throws Exception
	{
		return new SimulationsService().guardar(input);
	}
	public HashMap<String, Object> simulationScenaryQuery(HashMap<String, Object> input) throws Exception
	{
		return new SimulationsService().details(input);
	}
	public HashMap<String, Object> simulationDetailQuery(HashMap<String, Object> input) throws Exception
	{
		return new SimulationDetailsService().consultar(input);
	}
	public HashMap<String, Object> simulate(HashMap<String, Object> params) throws Exception
	{
		return new SimulationsService().execute(params);
	}
	public HashMap<String, Object> cancelSimulation(HashMap<String, Object> params) throws Exception
	{
		return new SimulationsService().cancelSimulation(params);
	}
	public HashMap<String, Object> getProgressSimulation(HashMap<String, Object> params) throws Exception
	{
		return new SimulationsService().getProgressSimulation(params);
	}
	
	public HashMap<String, Object> downloadSimulation(HashMap<String, Object> input) throws Exception
	{
		return new ReportesUtil().downloadSimulation(input);
	}
	@SuppressWarnings("unchecked")
	private Param getParam(String key, String subKey, HashMap<String, Object> input)
	{
		Object obj = input.get(key);
		HashMap<String, Object> hs = obj instanceof String || obj == null ? null : (HashMap<String, Object>)obj;
		return new Param(key, hs == null || subKey == null ? hs : hs.get(subKey));
	}
	@SuppressWarnings({ "unchecked", "unused" })
	public String getAnalytics(final HashMap<String, Object> input) throws Exception
	{
		//System.out.println(input);
		AnalysisResultsContent analysis = new AnalysisResultsContent(null, false);
		int id;
		try
		{
			HashMap<String, Object> linearityAnalysisMethod = (HashMap<String, Object>)input.get("linearityAnalysisMethod");
			HashMap<String, Object> dimensionalityReductionMethod = (HashMap<String, Object>)input.get("dimensionalityReductionMethod");
			HashMap<String, Object> normalityAnalysisMethod = (HashMap<String, Object>)input.get("normalityAnalysisMethod");
			HashMap<String, Object> clusteringMethod = (HashMap<String, Object>)input.get("clusteringMethod");
						
			String description = "";
			String sep = "";
			int index = 0;
			if (linearityAnalysisMethod != null)
			{
				ArrayList<Object> additionalData = (ArrayList<Object>)((ArrayList<Object>)input.get("additionalData")).get(index++);
				description = description + linearityAnalysisMethod.get("name");
			}
			if (dimensionalityReductionMethod != null)
			{
				ArrayList<Object> additionalData = (ArrayList<Object>)((ArrayList<Object>)input.get("additionalData")).get(index++);
				description = (String)dimensionalityReductionMethod.get("name");
				
				if (description.equals("U_map"))
				{
					description = description + " (n_neighbors:" + additionalData.get(0) 
							+ ", min_dist:" + additionalData.get(1)
							+ ", random_state:" + additionalData.get(2)
							+ ", Data type:" + additionalData.get(3) 
							+ ")";
				}
				else
				{
					description = description + " (n_components:" + additionalData.get(0)
							+ ", Data type:" + additionalData.get(1)
							+ ")"; 
				}
				sep = "/";
			}
			if (normalityAnalysisMethod != null)
			{
				ArrayList<Object> additionalData = (ArrayList<Object>)((ArrayList<Object>)input.get("additionalData")).get(index++);
				description = description + normalityAnalysisMethod.get("name");
			}
			if (clusteringMethod != null)
			{
				ArrayList<Object> additionalData = (ArrayList<Object>)((ArrayList<Object>)input.get("additionalData")).get(index++);
				description = description + sep + clusteringMethod.get("name")
							+ " (k:" + additionalData.get(0) 
							+ ")";
				
			}
			id = analysis.create((String)input.get("id"), description);
			analysis.getConnection().commit();
		}
		catch(Exception e)
		{
			String msg = e.getMessage();
			if (msg.contains("xak1analysisresults"))
			{
				return "{\"result\":\"RECORD ALREADY EXIST\"}";
			}
			throw e;
		}
		finally
		{
			analysis.getConnection().close();
		}
		final int index = id;
		final ArrayList<HashMap<String, Object>> data = new SimulationsService().getData2D(input);
		double time = ANALYSIS_RESULTS_AVG_TIME * data.size() *  ((HashMap<String, Object>)data.get(0).get("model")).size();
		//if (((ArrayList<Object>)((ArrayList<Object>)input.get("additionalData")).get(0)).get(3).toString().toUpperCase().equals("All Data"))
		{
			time = time * ((double[])data.get(0).get("pozo")).length;
		}
		System.out.println(time + "**" + data.size() + "**" + ((double[])data.get(0).get("pozo")).length + "**" + ((ArrayList<Object>)input.get("additionalData")).get(0));
		if (time > 120000)
		{
			new Thread(new Runnable()
			{
				
				@Override
				public void run()
				{
					try
					{
						runAnalytics(input, data, index);
					} 
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
				}
			}).start();
			return "{\"result\":\"RUN BACKGROUND\"}";
		}
		else
		{
			try
			{
				return runAnalytics(input, data, index);
			}
			catch(Exception e)
			{	
				analysis = new AnalysisResultsContent(null, false);
				analysis.deleteItem((String)input.get("id"), id);
				analysis.getConnection().commit();
				throw e;
				
			}
		}
	}
	private String runAnalytics(HashMap<String, Object> input, ArrayList<HashMap<String, Object>> data, int id) throws Exception
	{
		long t = System.currentTimeMillis();
		String url = PropertyUtil.getProperty("URL_ANALITICS");
		
		try
		{
			String s = Utils.sendPostData(url, getParam("normalityAnalysisMethod", null, input)
									 , getParam("linearityAnalysisMethod", null, input)
									 , getParam("dimensionalityReductionMethod", null, input)
									 , getParam("clusteringMethod", null, input)
									 , new Param("additionalData", input.get("additionalData"))
									 , new Param("graph2DProperties", input.get("graph2DProperties"))
									 , new Param("data", data));
			
			AnalysisResultsContent analysis = new AnalysisResultsContent(null, false);
			analysis.update((String)input.get("id"), id, s);
			analysis.getConnection().commit();
			analysis.getConnection().close();
			return s;
		}
		catch(Exception e)
		{
			AnalysisResultsContent analysis = new AnalysisResultsContent(null, false);
			analysis.updateState((String)input.get("id"), id, 3);
			analysis.getConnection().commit();
			analysis.getConnection().close();
			LogUtil.error(getClass(), e, "Error al invocar servicio python");
			throw e;
		}
		finally
		{
			System.out.println("Tiempo en ejecutar analisis:" + (System.currentTimeMillis() - t));			
		}
		
	}
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> getAnalysisResults(HashMap<String, Object> input) throws Exception
	{
		input.put("idSimulation", input.get("id"));
		return (ArrayList<HashMap<String, Object>>)new AnalysisResultsService().consultar(input).get("listData");
	}
	@SuppressWarnings("unused")
	private int compare(JSONArray o1, JSONArray o2)
	{
		double diff = o1.getDouble(0) - o2.getDouble(0);
		if (diff < 0D) {
			return -1;
		}
		else if (diff > 0) {
			return 1;
		}
		diff = o1.getDouble(1) - o2.getDouble(1);
		if (diff < 0D) {
			return -1;
		}
		else if (diff > 0) {
			return 1;
		}
		diff = o1.getDouble(2) - o2.getDouble(2);
		if (diff < 0D) {
			return -1;
		}
		else if (diff > 0) {
			return 1;
		}
		return 0;		
	}
	public String getAnalysisResultContent(HashMap<String, Object> input) throws Exception
	{
		return new AnalysisResultsContent().getContent((String)input.get("idSimulation"), (Integer)input.get("id"));
		/*JSONObject json = new JSONObject(s); 
		JSONArray result = json.getJSONArray("result");
		if (result.length() > 1)
		{
			System.out.println("length:" + result.length());
			ArrayList<JSONArray> sResult = new ArrayList<JSONArray>();
			for (int i = 0; i < result.length(); i++)
			{
				JSONArray jarray = result.getJSONArray(i);
				jarray.put(0, Math.round(jarray.getDouble(0) * 10.0) / 10.0);
				jarray.put(1, Math.round(jarray.getDouble(1) * 10.0) / 10.0);
				jarray.put(2, Math.round(jarray.getDouble(2) * 0.1) / 0.1);
				sResult.add(jarray);			
			}
			Collections.sort(sResult, new Comparator<JSONArray>()
			{
	
				@Override
				public int compare(JSONArray o1, JSONArray o2)
				{
					return BusinessService.this.compare(o1, o2);
				}
			});
			result = new JSONArray();
			JSONArray oldValue = sResult.get(0);
			result.put(oldValue);
			for (int i = 1; i < sResult.size(); i++)
			{
				JSONArray value = sResult.get(i);
				if (compare(oldValue, value) != 0)
				{
					result.put(value);
					oldValue = value;
				}
			}	
			json.put("result", result);
			System.out.println("length2:" + result.length());
			return json.toString();
		}
		return s;*/
	}
	public void deleteAnalysisResult(HashMap<String, Object> input) throws Exception
	{
		new AnalysisResultsService().eliminar(input);
	}
	
}
