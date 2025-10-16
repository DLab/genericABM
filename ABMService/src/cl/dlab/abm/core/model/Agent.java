package cl.dlab.abm.core.model;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONObject;

import cl.dlab.abm.core.kqml.KQMLMessage;
import cl.dlab.abm.core.kqml.KqmlIntegration;
import cl.dlab.abm.core.kqml.KqmlItem;
import cl.dlab.abm.core.kqml.MessageType;
import cl.dlab.abm.util.Utils;

public class Agent implements Cloneable
{
	private Integer id;
	private String name;
	private String description;
	private org.jgrapht.Graph<Integer, Agent> graph;
	public boolean removed = false;
	public boolean incubating = false;
	public double rnd;
	public String[] kqmlProperties;
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
	 * @return the graph
	 */
	public org.jgrapht.Graph<Integer, Agent> getGraph()
	{
		return graph;
	}
	/**
	 * @param graph the graph to set
	 */
	public void setGraph(org.jgrapht.Graph<Integer, Agent> graph)
	{
		this.graph = graph;
	}
	public void setValues(Model model)
	{		
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		// TODO Auto-generated method stub
		return super.clone();
	}
	/**
	 * @return the id
	 */
	public Integer getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}
	public void remove(Model model)
	{
		remove(model, true);
	}
	@SuppressWarnings("unchecked")
	private void remove(Model model, boolean remove)
	{
		ArrayList<Agent> agents = (ArrayList<Agent>)model.getAgents(getClass().getSimpleName());
		agents.remove(this);
		ArrayList<Agent> allAgents = (ArrayList<Agent>)model.getAgents("AGENTS");
		if (allAgents != null)
		{
			allAgents.remove(this);
		}
		this.removed = remove;
	}
	@SuppressWarnings("unchecked")
	public void create(Model model, Agent parent, double maxChildren) throws CloneNotSupportedException
	{
		int n = (int)maxChildren;// model.random.nextInt((int)Math.round(maxChildren) + 1);
		ArrayList<Agent> newAgents = new ArrayList<Agent>();
		if (n > 0)
		{
			ArrayList<Agent> agents = (ArrayList<Agent>)model.getTmpAgents(getClass().getSimpleName());
			ArrayList<Agent> allAgents = (ArrayList<Agent>)model.getTmpAgents("AGENTS");
			Agent agent;
			//if (this.getClass().getSimpleName().equals("Presa"))
			//System.out.println("add:" + this.getClass().getSimpleName() + "**" + n + "**");
			for (int i = 0; i < n; i++)
			{
				newAgents.add(agent = (Agent)clone());
				agents.add(agent);
				
				//se saca de manera temporal, a this y parent ya que están incuvando
				this.remove(model, false);
				parent.remove(model, false);
				agents.add(this);
				agents.add(parent);
				if (allAgents != null)
				{
					allAgents.add(agent);
				}
			}
			parent.incubating = true;
			this.incubating = true;
		}
		model.newAgents = newAgents;
	}
	private JSONObject getProperties() throws Exception
	{
		JSONObject obj = new JSONObject();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields)
		{
			obj.put(field.getName(), field.get(this));
		}
		
		return obj;
	}
	private void setProperties(JSONObject properties) throws Exception
	{
		Utils.setProperties(properties, this);		
	}
	public KQMLMessage receiveMessage(Model model, KQMLMessage msg) throws Exception
	{
		//System.out.println(model.getName() + "**" + model.getIdProcess() + "**" + model.getNumCombination() + "**" + model.getNumSimulation() + "**" + model.getNumStep() );
		if (model.isKqmlIntegration())
		{
			KqmlIntegration.getInstance().addItem(new KqmlItem(model, msg.getSender(), msg.getReceiver()));
		}
		if (msg.getPerformative() == MessageType.Ask) 
		{
			if (msg.getMessage().equals("give-me-your-properties"))
			{
				return new KQMLMessage(MessageType.Tell
						, msg.getReceiver()
						, msg.getSender()
						, getProperties()
						, "JSON"
						, msg.getOntology());
			}
		}
		else if (msg.getPerformative() == MessageType.Update) 
		{
			setProperties(msg.getContent());
			return null;
		}
		return null;
	}
}
