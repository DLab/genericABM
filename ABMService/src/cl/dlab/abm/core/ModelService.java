package cl.dlab.abm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import cl.dlab.abm.core.model.gen.JavaClassGenerator;
import cl.dlab.abm.core.parser.Util;
import cl.dlab.abm.core.sql.rad.Action;
import cl.dlab.abm.core.sql.rad.Agent;
import cl.dlab.abm.core.sql.rad.AgentSites;
import cl.dlab.abm.core.sql.rad.Graph;
import cl.dlab.abm.core.sql.rad.Model;
import cl.dlab.abm.core.sql.rad.ModelVariables;
import cl.dlab.abm.core.sql.rad.Rule;
import cl.dlab.abm.service.vo.InputVO;
import cl.dlab.abm.service.vo.ModelOutputVO;
import cl.dlab.abm.service.vo.RespuestaVO;

public class ModelService extends BaseService {

	public ModelService() {
		super();
	}

	public ModelService(Connection con) {
		super(con);
	}

	public RespuestaVO<ModelOutputVO> consultar(InputVO input) throws Exception {
		return new Model(con, con == null).consultar(input);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> consultar(java.util.HashMap<String, Object> input) throws Exception 
	{	
		Model model = new Model(con, false);
		try
		{
			String queryId = (String)input.get("query_id");
			System.out.println(queryId);
			if (queryId != null && queryId.equals("activate"))
			{
				ModelVariables modelVariablesService = new ModelVariables(model.getConnection(), false);
				Agent agentsService = new Agent(model.getConnection(), false);
				AgentSites agentSitesService = new AgentSites(model.getConnection(), false);
				HashMap<String, Object> hs = model.consultar(input);
				ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>)hs.get("listData");
				for (HashMap<String, Object> item : list)
				{
					item.put("modelName", item.get("name"));
					item.put("variables", getVariables((ArrayList<HashMap<String, Object>>)modelVariablesService.consultar(item).get("listData")));
					ArrayList<HashMap<String, Object>> agents = (ArrayList<HashMap<String, Object>>)agentsService.consultar(item).get("listData");
					item.put("agents", agents);
					for (HashMap<String, Object> agent : agents)
					{
						agent.put("agentName", agent.get("name"));
						agent.put("variables", getVariables((ArrayList<HashMap<String, Object>>)agentSitesService.consultar(agent).get("listData")));
						//System.out.println(agent);
					}
					
				}
				return hs;
			}
			return model.consultar(input);
		}
		finally
		{
			model.getConnection().close();
		}
	}
	private ArrayList<HashMap<String, Object>> getVariables(ArrayList<HashMap<String, Object>> variables)
	{
		for (HashMap<String, Object> variable : variables)
		{
			variable.put("name", Util.getPropertyName((String)variable.get("name")));
		}
		return variables;
	}

	public void eliminar(java.util.HashMap<String, Object> input) throws Exception {
		Model service = new Model(con, false);
		try
		{
			service.eliminar(input);
			service.getConnection().commit();
		}
		finally
		{
			service.getConnection().close();
		}
	}
	@SuppressWarnings("unchecked")
	private void saveAgents(HashMap<String, Object> input, Agent agentService, AgentSites agentSitesService) throws Exception
	{
		ArrayList<HashMap<String, Object>> agents = (ArrayList<HashMap<String,Object>>)input.get("agents");
		for (HashMap<String, Object> hsAgents : agents) {
			hsAgents.put("isNew", true);
			hsAgents.put("modelName", input.get("name"));
			String extendsFrom = (String)hsAgents.get("extendsFrom");
			if (extendsFrom != null) {
				hsAgents.put("_extendsFrom", extendsFrom);
				hsAgents.put("extendsFrom", null);
			}
			agentService.guardar(hsAgents);
			
			ArrayList<HashMap<String, Object>> variables = (ArrayList<HashMap<String,Object>>)hsAgents.get("variables");
			for (HashMap<String, Object> variable : variables) {
				variable.put("isNew", true);
				variable.put("modelName", input.get("name"));
				variable.put("agentName", hsAgents.get("name"));
				validateNullBoolean(variable, "detailedAnalysis");
				agentSitesService.guardar(variable);
				
			}
			
		}
		for (HashMap<String, Object> hsAgents : agents) {
			String extendsFrom = (String)hsAgents.get("_extendsFrom");
			if (extendsFrom != null) {
				hsAgents.put("isNew", false);
				hsAgents.put("extendsFrom", extendsFrom);
				agentService.guardar(hsAgents);
			}
		}
		
	}
	public void updateMeanSimulationTime(String name, double meanSimulationTime) throws Exception
	{
		Model model = new Model();
		System.out.println("se actualiza model:" + name + ":" + meanSimulationTime);
		try(PreparedStatement stmt = model.getConnection().prepareStatement("update model set mean_simulation_time=? where name = ?"))
		{
			stmt.setDouble(1, meanSimulationTime);
			stmt.setString(2, name);
			stmt.executeUpdate();
			model.getConnection().commit();
		}
		model.getConnection().close();
		
	}
	@SuppressWarnings("unchecked")
	private void saveRules(HashMap<String, Object> input, Rule ruleService, Action actionService) throws Exception
	{
		ArrayList<HashMap<String, Object>> actions = (ArrayList<HashMap<String,Object>>)input.get("actions");
		for (HashMap<String, Object> hsAction : actions) {
			hsAction.put("isNew", true);
			hsAction.put("modelName", input.get("name"));
			actionService.guardar(hsAction);
			
		}

		ArrayList<HashMap<String, Object>> rules = (ArrayList<HashMap<String,Object>>)input.get("rules");
		for (HashMap<String, Object> hsRule : rules) {
			hsRule.put("isNew", true);
			hsRule.put("modelName", input.get("name"));
			ruleService.guardar(hsRule);
		}
						
	}
	private void validateNullBoolean(HashMap<String, Object> input, String property) 
	{
		Boolean value = (Boolean)input.get(property);
		if (value == null)
		{
			input.put(property, false);
		}
		
	}
	@SuppressWarnings("unchecked")
	public void guardar(HashMap<String, Object> input) throws Exception {
		Model modelService = new Model(con, false);
		try
		{
			Agent agentService = new Agent(modelService.getConnection(), false);
			Rule ruleService = new Rule(agentService.getConnection(), false);
			Action actionService = new Action(agentService.getConnection(), false);
			ModelVariables modelVariablesService = new ModelVariables(agentService.getConnection(), false);
			AgentSites agentSitesService = new AgentSites(agentService.getConnection(), false);
			Graph graphService = new Graph(agentService.getConnection(), false);
			validateNullBoolean(input, "activate");
			validateNullBoolean(input, "private");
			validateNullBoolean(input, "singleAgentsList");
			
			
			input.put("modelName", input.get("name"));
			try(PreparedStatement del = agentSitesService.getConnection().prepareStatement("delete from AgentSites where model_name = ?"))
			{
				del.setString(1, (String)input.get("name"));
				del.executeUpdate();
			}
			
			agentService.eliminarAll(input);
			ruleService.eliminarAll(input);
			actionService.eliminarAll(input);
			modelVariablesService.eliminarAll(input);
			graphService.eliminarAll(input);
			
			modelService.guardar(input);
			
			ArrayList<HashMap<String, Object>> variables = (ArrayList<HashMap<String,Object>>)input.get("variables");
			for (HashMap<String, Object> variable : variables) {
				variable.put("isNew", true);
				variable.put("modelName", input.get("name"));
				validateNullBoolean(variable, "detailedAnalysis");
				modelVariablesService.guardar(variable);
				
			}
			ArrayList<HashMap<String, Object>> graphs = (ArrayList<HashMap<String,Object>>)input.get("graphs");
			for (HashMap<String, Object> graph : graphs) {
				graph.put("isNew", true);
				graph.put("modelName", input.get("name"));
				graphService.guardar(graph);
				
			}
			
			saveAgents(input, agentService, agentSitesService);
			saveRules(input, ruleService, actionService);
			
			if ((Boolean)input.get("activate"))
			{
				new JavaClassGenerator().generateJar(input);
			}
			modelService.getConnection().commit();
		}
		finally
		{
			modelService.getConnection().close();
		}
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> detailQuery(HashMap<String, Object> input) throws Exception
	{
		Agent agentService = new Agent(con, false);
		try
		{
			Rule ruleService = new Rule(agentService.getConnection(), false);
			Action actionService = new Action(agentService.getConnection(), false);
			ModelVariables modelVariablesService = new ModelVariables(agentService.getConnection(), false);
			AgentSites agentSitesService = new AgentSites(agentService.getConnection(), false);
			Graph graphService = new Graph(agentService.getConnection(), false);
			
			input.put("modelName", input.get("name"));
			
			ArrayList<HashMap<String, Object>> agents = (ArrayList<HashMap<String, Object>>)agentService.consultar(input).get("listData");
			for (HashMap<String, Object> agent : agents) {
				agent.put("agentName", agent.get("name"));
				agent.put("variables", agentSitesService.consultar(agent).get("listData"));
			}
			input.put("agents", agents);
			input.put("rules", ruleService.consultar(input).get("listData"));
			input.put("actions", actionService.consultar(input).get("listData"));
			input.put("variables", modelVariablesService.consultar(input).get("listData"));
			input.put("graphs", graphService.consultar(input).get("listData"));
		}
		finally
		{
			agentService.getConnection().close();
		}
		return input;
	}
	
}