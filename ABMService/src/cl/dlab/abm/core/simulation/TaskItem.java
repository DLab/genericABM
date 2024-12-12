package cl.dlab.abm.core.simulation;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class TaskItem implements Serializable
{
	private String idProcess;
	private int numCombinations;
	private int executedCombinations;
	private int stateId;
	private Date startTime;
	private Date lastUpdate;

	public TaskItem(String idProcess, int numCombinations, int executedCombinations, Date startTime, Date lastUpdate, int stateId)
	{
		this.idProcess = idProcess;
		this.numCombinations = numCombinations;
		this.executedCombinations = executedCombinations;
		this.startTime = startTime;
		this.lastUpdate = lastUpdate;
		this.stateId = stateId; 
	}
	public TaskItem(String idProcess)
	{
		this.idProcess = idProcess;
		this.lastUpdate = this.startTime = new Date();	
	}
	public void init(int numCombinations)
	{
		this.numCombinations = numCombinations;
		this.lastUpdate = this.startTime = new Date();		
	}

	/**
	 * @return the idProcess
	 */
	public String getIdProcess()
	{
		return idProcess;
	}

	/**
	 * @return the numCombinations
	 */
	public int getNumCombinations()
	{
		return numCombinations;
	}


	/**
	 * @return the executedCombinations
	 */
	public int getExecutedCombinations()
	{
		return executedCombinations;
	}

	/**
	 * @param executedCombinations the executedCombinations to set
	 */
	public void setExecutedCombinations(int executedCombinations)
	{
		this.executedCombinations = executedCombinations;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime()
	{
		return startTime;
	}


	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate()
	{
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}
	/**
	 * @return the stateId
	 */
	public int getStateId()
	{
		return stateId;
	}
}
