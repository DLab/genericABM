package cl.dlab.abm.core.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cl.dlab.abm.core.model.Agent;
import cl.dlab.abm.core.model.Data;
import cl.dlab.abm.core.model.Model;
import cl.dlab.abm.core.model.Rule;
import cl.dlab.abm.core.model.UmbralGenerationType;
import cl.dlab.util.LogUtil;

public class Sim implements Runnable
{
	private Model model;
	private int numSteps;
	private TaskManager taskManager;
	private Random random;
	
	public Sim(TaskManager taskManager, Model model, int numSteps)
	{
		this.model = model;
		this.numSteps = numSteps;
		this.taskManager = taskManager;
		this.random = new Random();
	}
	private void meet(Rule rule, Agent ag1, Agent ag2) throws Exception
	{
		this.model.clearUmbral(UmbralGenerationType.BY_AGENT);
		if (rule.isOk(model, ag1, ag2))
		{
			rule.getTrueAction().execute(model, ag1, ag2);
		}
		else 
		{
			rule.getFalseAction().execute(model, ag1, ag2);
		}
	}
	@SuppressWarnings("unchecked")
	private ArrayList<Agent> neighbors(Agent agent, String meetAgent)
	{
		if (agent.getGraph() == null)
		{
			ArrayList<Agent> list = new ArrayList<Agent>((ArrayList<Agent>)model.getAgents(meetAgent));
			list.remove(agent);
			return list;
		}
		return new ArrayList<Agent>(agent.getGraph().edgesOf(agent.getId()));
	}	
	private Agent rand(ArrayList<Agent> agents)
	{
		return agents.size() == 0 ? null : agents.get(this.random.nextInt(agents.size()));
	}
	
	private void step() throws Exception
	{
		for (Rule rule : model.getRules())
		{
			if (rule.getIterate() != null)
			{
				double probMeet = model.getProbMeet();
				ArrayList<Agent> agents = new ArrayList<Agent>();
				ArrayList<? extends Agent> list = model.getAgents(rule.getIterate());
				for (Agent agent : list)
				{
					if (agent == null || agent.removed || agent.incubating)
					{
						continue;
					}
					if (random.nextDouble() < probMeet)
					{
						agents.add(agent);
					}
				}		
				model.setNumInteractionAgents(agents.size());
				model.setNumNoInteractionAgents(list.size() - agents.size());
				rule.getBeforeAction().execute(model);
				if (rule.getMeet() != null)
				{
					for (Agent agent : agents)
					{
						meet(rule, agent, rand(neighbors(agent, rule.getMeet())));
					}
					
				}
				else
				{
					for (Agent agent : agents)
					{
						if (rule.isOk(model, agent))
						{
							rule.getTrueAction().execute(model, agent);
						}
						else
						{
							rule.getFalseAction().execute(model, agent);
						}
					}
					
				}
				rule.getAfterAction().execute(model);
			}
			else
			{
				if (rule.isOk(model))
				{
					rule.getTrueAction().execute(model);
				}
				else
				{
					rule.getFalseAction().execute(model);
				}
				
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		Data data = model.newData(numSteps);
		try
		{
			for (Rule rule : this.model.getInitialRules())
			{
				if (rule.getIterate() != null)
				{
					ArrayList<Agent> agents = new ArrayList<Agent>(model.getAgents(rule.getIterate()));
					for (Agent agent : agents)
					{
						if (rule.isOk(model, agent))
						{
							rule.getTrueAction().execute(model, agent);
						}
						else
						{
							rule.getFalseAction().execute(model, agent);
						}
					}
					
				}
				else
				{
					if (rule.isOk(model))
					{
						rule.getTrueAction().execute(model);
					}
					else
					{
						rule.getFalseAction().execute(model);
					}

				}
				
			}
			for (int i = 0; i < numSteps; i++)
			{
				this.model.clearUmbral(UmbralGenerationType.BY_STEP);
				this.model.tmpAgents = new HashMap<String, ArrayList<? extends Agent>>();
				step();
				for(String key : this.model.tmpAgents.keySet())
				{
					ArrayList<Agent> list = (ArrayList<Agent>)this.model.getAgents(key);
					for (Agent ag : list)
					{
						ag.incubating = false;
					}
					synchronized (list)
					{
						list.addAll(this.model.tmpAgents.get(key));						
					}
				}
				data.addValues(i, model);			
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LogUtil.error(Sim.class, e, "Error al realizar step");
			try
			{
				TaskManager.cancelProcess(model.getIdProcess());
			} 
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			return;
		}
		taskManager.endTask(data);
	}

}
