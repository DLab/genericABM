package cl.dlab.abm.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import cl.dlab.abm.core.model.function.Function;
import cl.dlab.abm.util.Param;
import cl.dlab.abm.util.Utils;
import cl.dlab.util.PropertyUtil;

public class Model implements Cloneable
{
	private static final UmbralGenerationType umbralGenerationType = UmbralGenerationType.valueOf(PropertyUtil.getPropertySinErrores("model.generatesthreshold"));
	private static final String URL_TGATNN = PropertyUtil.getPropertySinErrores("URL-TGATNN-INTEGRATION");
	private static final String URL_TGATNN_ONE = PropertyUtil.getPropertySinErrores("URL-TGATNN-ONE-INTEGRATION");
	
	private String idProcess;
	private int numCombination;
	private int numSimulation;
	private int numStep;
	private String name;
	private String description;
	private double probMeet;
	private Model runWith;
	private boolean kqmlIntegration;
	private ArrayList<Graph> graphs;
	private ArrayList<Rule> rules;
	private ArrayList<Rule> initialRules;
	protected HashMap<String, ArrayList<? extends Agent>> agents;
	protected Random random;
	protected int numInteractionAgents;
	protected int numNoInteractionAgents;
	public HashMap<String, ArrayList<? extends Agent>> tmpAgents;
	public ArrayList<Agent> newAgents;
	public String[] kqmlProperties;

	public Model() 
	{
		random = new Random();
		this.rules = new ArrayList<Rule>();
		this.initialRules = new ArrayList<Rule>();
		this.agents = new HashMap<String, ArrayList<? extends Agent>>();
	}
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**
	 * @return the runWith
	 */
	public Model getRunWith()
	{
		return runWith;
	}
	/**
	 * @param runWith the runWith to set
	 */
	public void setRunWith(Model runWith)
	{
		this.runWith = runWith;
	}
	/**
	 * @return the graphs
	 */
	public ArrayList<Graph> getGraphs()
	{
		return graphs;
	}
	/**
	 * @param graphs the graphs to set
	 */
	public void setGraphs(ArrayList<Graph> graphs)
	{
		this.graphs = graphs;
	}
	/**
	 * @return the agents
	 */
	public ArrayList<? extends Agent> getAgents(String name)
	{
		return agents.get(name);
	}
	@SuppressWarnings("unchecked")
	public ArrayList<? extends Agent> getTmpAgents(String name)
	{
		ArrayList<Agent> list = (ArrayList<Agent>)tmpAgents.get(name);
		if (list == null)
		{
			tmpAgents.put(name, list = new ArrayList<Agent>());
		}
		return list;
	}
	/**
	 * @param agents the agents to set
	 */
	public void setAgents(String name, ArrayList<Agent> agents)
	{
		this.agents.put(name, agents);
	}
	/**
	 * @return the rules
	 */
	private double logistic(double x, double x0, int L, int K)
	{
		return L / (1 + Math.exp(-K * (x - x0)));
	}
	public double logistic(double x)
	{
		return normalizer(x);
	}
	public double normalizer(double x)
	{
		if (x >= 0.0 && x <= 1.0)
		{
			return x;
		}
		return logistic(x, 0.5, 1, 3);
	}
	
	/**
	 * @return the pronMeet
	 */
	public double getProbMeet()
	{
		return probMeet;
	}
	/**
	 * @param pronMeet the pronMeet to set
	 */
	public void setProbMeet(double probMeet)
	{
		this.probMeet = probMeet;
	}
	
	public void setValues(HashMap<String, Object> values)
	{		
	}
	@SuppressWarnings("unchecked")
	public void population(int numAgents, String name, Class<? extends Agent> agentClass) throws Exception
	{
		ArrayList<? extends Agent> agents = this.getAgents(name);
		if (agents == null)
		{
			this.agents.put(name, agents = new ArrayList<Agent>());
		}
		for (int i = 0; i < numAgents; i++)
		{
			Agent agent = agentClass.getConstructor().newInstance();
			agent.setId(i);
			agent.setValues(this);
			((ArrayList<Agent>)agents).add(agent);
		}
		this.agents.put(agentClass.getSimpleName(), agents);
	}
	public cl.dlab.abm.core.model.Data newData(int numPasos)
	{
		return new Data(numPasos);
	}
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		HashMap<String, ArrayList<? extends Agent>> agents = this.agents;
		this.agents = null;
		Model model = (Model)super.clone();
		HashMap<String, ArrayList<? extends Agent>> hs = new HashMap<String, ArrayList<? extends Agent>>();
		boolean isSingleList = agents.get("AGENTS") != null;
		ArrayList<Agent> allList = new ArrayList<Agent>();
		for (String name : agents.keySet())
		{
			if (name.equals("AGENTS"))
			{
				continue;
			}
			ArrayList<Agent> newAgents = new ArrayList<Agent>();
			ArrayList<? extends Agent> list = agents.get(name);
			if (isSingleList)
			{
				for (Agent agent : list)
				{
					if (agent.removed)
					{
						continue;
					}
					Agent ag = (Agent)agent.clone();
					newAgents.add(ag);
					allList.add(ag);
				}
			}
			else
			{
				for (Agent agent : list)
				{
					newAgents.add((Agent)agent.clone());
				}				
			}
			hs.put(name, newAgents);
		}
		if (allList != null)
		{
			hs.put("AGENTS", allList);
		}
		
		model.agents = hs;
		model.random = new Random();
		this.agents = agents;
		return model;
	}
	protected HashMap<String, Object> getFunctionProperty(Function f)
	{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		hs.put("name", f.getName());
		hs.put("params", f.getParams());
		return hs;
	}
	protected void clearFunctionValues()
	{
		
	}
	public void clearUmbral(UmbralGenerationType umbralType)
	{
		if(umbralGenerationType == umbralType) 
		{
			clearFunctionValues();
		}
	}
	
	public HashMap<String, Object> getProperties()
	{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		//hs.put("numSteps", numSteps);
		hs.put("probMeet", probMeet);
		ArrayList<String> keys = new ArrayList<String>(agents.keySet());
		if (keys.size() == 1)
		{
			hs.put("numAgents", agents.get(keys.get(0)).size());
		}
		else
		{
			for (String key : keys)
			{
				hs.put("numAgents_" + key, agents.get(key).size());
			}
		}
		
		return hs;
		
	}
	/**
	 * @return the idProcess
	 */
	public String getIdProcess()
	{
		return idProcess;
	}
	/**
	 * @param idProcess the idProcess to set
	 */
	public void setIdProcess(String idProcess)
	{
		this.idProcess = idProcess;
	}
	/**
	 * @return the rules
	 */
	public ArrayList<Rule> getRules()
	{
		return rules;
	}
	/**
	 * @param rules the rules to set
	 */
	public void setRules(ArrayList<Rule> rules)
	{
		this.rules = new ArrayList<Rule>();
		this.initialRules = new ArrayList<Rule>();
		for (Rule rule : rules)
		{
			if (rule.initial)
			{
				this.initialRules.add(rule);
			}
			else
			{
				this.rules.add(rule);
			}
		}
	}
	public void addRule(Rule rule)
	{
		if (rule.initial)
		{
			this.initialRules.add(rule);
		}
		else
		{
			this.rules.add(rule);
		}
	}

	public double random()
	{
		return random.nextDouble();
	}

	/**
	 * @return the initialRules
	 */
	public ArrayList<Rule> getInitialRules()
	{
		return initialRules;
	}

	/**
	 * @return the numInteractionAgents
	 */
	public int getNumInteractionAgents()
	{
		return numInteractionAgents;
	}

	/**
	 * @param numInteractionAgents the numInteractionAgents to set
	 */
	public void setNumInteractionAgents(int numInteractionAgents)
	{
		this.numInteractionAgents = numInteractionAgents;
	}

	/**
	 * @return the numNoInteractionAgents
	 */
	public int getNumNoInteractionAgents()
	{
		return numNoInteractionAgents;
	}

	/**
	 * @param numNoInteractionAgents the numNoInteractionAgents to set
	 */
	public void setNumNoInteractionAgents(int numNoInteractionAgents)
	{
		this.numNoInteractionAgents = numNoInteractionAgents;
	}

	/**
	 * @return the kqmlIntegration
	 */
	public boolean isKqmlIntegration()
	{
		return kqmlIntegration;
	}

	/**
	 * @param kqmlIntegration the kqmlIntegration to set
	 */
	public void setKqmlIntegration(boolean kqmlIntegration)
	{
		this.kqmlIntegration = kqmlIntegration;
	}

	/**
	 * @return the numSimulation
	 */
	public int getNumSimulation()
	{
		return numSimulation;
	}

	/**
	 * @param numSimulation the numSimulation to set
	 */
	public void setNumSimulation(int numSimulation)
	{
		this.numSimulation = numSimulation;
	}

	/**
	 * @return the numStep
	 */
	public int getNumStep()
	{
		return numStep;
	}

	/**
	 * @param numStep the numStep to set
	 */
	public void setNumStep(int numStep)
	{
		this.numStep = numStep;
	}

	/**
	 * @return the numCombination
	 */
	public int getNumCombination()
	{
		return numCombination;
	}

	/**
	 * @param numCombination the numCombination to set
	 */
	public void setNumCombination(int numCombination)
	{
		this.numCombination = numCombination;
	}
	public void updateDataTgatnn(Agent agent1, Agent agent2, JSONObject tgannt) throws Exception
	{
		
	}
	public JSONObject sendDataTgatnn(Agent agent1, Agent agent2) throws Exception
	{
		JSONObject json = new JSONObject();
		Utils.getProperties(json, this, kqmlProperties);
		JSONArray array = new JSONArray();
		json.put("agents", array);
		
		JSONObject oAgent = new JSONObject();
		Utils.getProperties(oAgent, agent1, agent1.kqmlProperties);
		array.put(oAgent);
		
		oAgent = new JSONObject();
		Utils.getProperties(oAgent, agent2, agent2.kqmlProperties);
		array.put(oAgent);
		
		long t = System.currentTimeMillis();
		String response = Utils.sendData(URL_TGATNN_ONE, "POST", new Param("data", json.toString()));
		System.out.println("Tiempo en integrar tgatnn-one:" + (System.currentTimeMillis() - t));
		return new JSONObject(response);
	}
	
	public void sendAllDataTgatnn() throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("idProcess", this.getIdProcess());
		Utils.getProperties(json, this, kqmlProperties);
		for (String type : agents.keySet())
		{
			JSONArray array = new JSONArray();
			json.put(type, array);
			ArrayList<? extends Agent> items = agents.get(type);
			for (Agent agent : items)
			{
				JSONObject oAgent = new JSONObject();
				Utils.getProperties(oAgent, agent, agent.kqmlProperties);
				array.put(oAgent);
			}
		}
		long t = System.currentTimeMillis();
		Utils.sendData(URL_TGATNN, "POST", new Param("data", json.toString()));
		System.out.println("Tiempo en integrar tgatnn:" + (System.currentTimeMillis() - t));
	}

}
